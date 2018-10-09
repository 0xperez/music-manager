package atunibz.dperez.approject1617.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import atunibz.dperez.approject1617.exceptions.InvalidUsernameException;
import atunibz.dperez.approject1617.exceptions.UsernameAlreadyExistsException;
import atunibz.dperez.approject1617.gui.linux.MainFrame;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.user.PasswordEncryptor;
import atunibz.dperez.approject1617.user.User;
import atunibz.dperez.approject1617.user.UsernameValidator;
/**
 * Class which handles the {@code users.xml} file in which the information about this application's users are stored. Provides methods 
 * to perform various operations on such file.
 * @author Davide Perez
 * @version 1.4
 * @since 17/5/2017
 *
 */
public class XMLUsers implements XMLWriter {
	/**
	 * File reference which holds the path to users.xml file
	 */
	private static File OUTPUT_FILE = new File("xml/users.xml");
	/**
	 * Document object representing the xml document
	 */
	private Document doc;
	/**
	 * Factory to obtain DocumentBuilder instance
	 */
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	/**
	 * DocumentBuilder to instantiate Document instances
	 */
	private DocumentBuilder db;
	/**
	 * OutputStream to write the Document to a file
	 */
	private OutputStream OUTPUT_STREAM;
	
	/**
	 * Constructor of the class. Parses the data.xml file into a Document in order to allow operations to be performed on it.
	 * If the data.xml file is missing for some reason, an empty file with the same structure is created programmatically.
	 */
	XMLUsers(){
		
		//checks if the xml already exists. If not, create a new one. May move this in the above catch block
		if(!APManagerSystem.USERS_FILE.exists()){
			APManagerSystem.getSystemLogger().log(Level.WARNING, "no valid XML file found. Creating a new file");
			createXML();
		}
		
		else{
			APManagerSystem.getSystemLogger().fine("users.xml found");
			doc = parse(APManagerSystem.USERS_FILE);
		}
		
		
		
	}
	
	
	// STRUCTURAL
	private void createXML() {
		APManagerSystem.getSystemLogger().fine("creating xml");
		db = getDocumentBuilder();
		doc = db.newDocument();
		Element root = doc.createElement("users");
		doc.appendChild(root);
		doc.getDocumentElement().normalize();
		write(OUTPUT_STREAM);

	}

	
	/**
	 * Adds a user to the user file. Accepts as parameter any instance implementing {@link XMLWritable} interface, but in this 
	 * specific implementation it has to be a {@link User}.
	 * @param user the user to add
	 * @throws ClassCastException if the parameter is not an instance of {@link User}
	 * 
	 */
	public void add(XMLWritable user) {
		Element rootElement = doc.getDocumentElement();
		Element externalNode = (Element) doc.adoptNode(user.toElement());											
		//adopts the (Element) Node created by the toElement() method implementation of User class
		//(because a Document can append as childs of its root node only nodes created by the Document itself
		rootElement.appendChild(externalNode);
		write(OUTPUT_STREAM);
		APManagerSystem.getSystemLogger().fine("a new user has been added to XML file.");

	}
	
	/**
	 * Method used for the authentication during the login procedure. Given an username and a password,
	 * checks if there exist a user whose elements match with the parameters. 
	 * @param username string representing a username
	 * @param psw string representing a password
	 * @return true if such an user exists, false otherwise
	 */
	public boolean authenticate(String username, String psw) {
		APManagerSystem.getSystemLogger().fine("system received a login request");
		//retrieve all users element in form of a list of nodes
		NodeList nodes = doc.getElementsByTagName("user");
		APManagerSystem.getSystemLogger().fine("there are currently " + nodes.getLength() + " users in the xml database");
		//encrypt password, since in the xml is stored as encrypted
		psw = PasswordEncryptor.encryptPassword(psw);
		//iterate through nodes to see if there is a correpspondence
		for (int i = 0; i < nodes.getLength(); i++) {
			Node current = nodes.item(i);
			//casts the node to element and extracts username and password from the current user element
			Element userElement = (Element) current;
			String foundUsername = userElement.getElementsByTagName("username").item(0).getTextContent();
			String foundPsw = userElement.getElementsByTagName("password").item(0).getTextContent();
			String foundProfilePicPath = userElement.getAttribute("pic");
			APManagerSystem.getSystemLogger().finer("At " + i + "th iteration found username = " + foundUsername + " and password " + foundPsw);

			if (foundUsername.equals(username) && foundPsw.equals(psw)) {

				APManagerSystem.getSystemLogger().info("a user accessed the system with the following credentials. \nUsername: " + username
						+ "\nPassword: " + psw);
				User user = new User(username, psw);
				user.setProfilePicPath(foundProfilePicPath);
				APManagerSystem.getSystemLogger().fine("a user accessed the database");
				MainFrame.getMainFrame().setCurrentUser(user);
				return true;
			}

		}

		APManagerSystem.getSystemLogger().warning("a user attempted to access the system with invalid credentials");
		return false;
	}
	
	
	
	/**
	 * Permanently removes a user from the system. The user indexed by the username (every user has an unique username, so 
	 * at any time there will be at most one user with such an username) is removed from the {@code user.xml} file and won't be
	 * able to authenticate anymore.
	 * @param username the username of the user to remove
	 * @throws NullPointerException if there is no user with such a username in the system
	 */
	//removes the user indexed by the username argument
	public void remove(String username){
		
		APManagerSystem.getSystemLogger().fine(System.getProperty("user.name") + " issued a request of removing user \"" + username + "\"");
		//retrieve all users element in form of a list of nodes
		NodeList users = doc.getElementsByTagName("user");
		APManagerSystem.getSystemLogger().fine("Currently there are " + users.getLength() + " entries in the XML user file.");
		//APManagerSystem.getSystemLogger().info(users.getLength() + " users in the xml database");
		//iterates through nodelist
		for(int i = 0; i < users.getLength(); i++){
			//get user element
			Element userElement = (Element)users.item(i);
			APManagerSystem.getSystemLogger().finer("Element " + "i: " + userElement.getNodeName());
			//get username element
			Element usernameElement = (Element)userElement.getElementsByTagName("username").item(0);
			//get actual text content
			String usernameText = usernameElement.getTextContent();
			
			//if correspondence found, then remove the element and update
			if(usernameText.equals(username)){
				APManagerSystem.getSystemLogger().fine("User found. Deletion in progress...");
				userElement.getParentNode().removeChild(userElement);//delete node corresponding to the user
				removeProfilePic(userElement);
				APManagerSystem.getSystemLogger().fine("user " + "\"" + username + "\"removed.");
				format();
				write(OUTPUT_STREAM);
				return;
			}
			
			
		}
		
		throw new NullPointerException("the user " + username + " does not exist");
		
	}
	
	/**
	 * Deletes the profile picture of a user (if the user choose a customed profile picture
	 * when registering).
	 * @param userElement an Element representing a User
	 */
	private void removeProfilePic(Element userElement){
		File pic = new File(userElement.getAttribute("pic")); 
		if(pic.getPath().contains("customed")){
			APManagerSystem.getSystemLogger().info("deleting profile picture of user " + userElement.getElementsByTagName("username").item(0).getTextContent());
			try {
				Files.delete(pic.toPath());
				APManagerSystem.getSystemLogger().info("profile picture deleted");
			} catch (IOException e) {
				APManagerSystem.getSystemLogger().log(Level.SEVERE, "error while deleting user's customed profile pic. Caused by: " + e.getCause().getClass().getName());
				e.printStackTrace();
			}
		}
		else{
			APManagerSystem.getSystemLogger().info("user had not a customed profile picture");
		}
	}
	
	/**
	 * Given a string, checks if it corresponds to any user's username
	 * @param username string representing a username
	 * @return true if there exists a user with such an username, false otherwise
	 */
	public boolean contains(String username){
		APManagerSystem.getSystemLogger().fine(" searching for a user with username \"" + username + "\"");
		NodeList users = doc.getElementsByTagName("user");
		APManagerSystem.getSystemLogger().fine("there are " + users.getLength() + " users saved");
		
		for(int i = 0; i < users.getLength(); i++){
			
			Element currentUser = (Element) users.item(i);
			APManagerSystem.getSystemLogger().finer(i + "th" + " user under process");
			Element currentUsername = (Element) currentUser.getElementsByTagName("username").item(0);
			String log = currentUsername == null ? "error while searching" : "" + currentUsername.getTextContent();
			APManagerSystem.getSystemLogger().fine(log);
			String usernameContent = currentUsername.getTextContent();
			if(usernameContent.equals(username)){
				APManagerSystem.getSystemLogger().fine("there already exists an user with username " + username);
				return true;
			}
		}
		APManagerSystem.getSystemLogger().fine("no user with such an username found");
		return false;
		
	}
	
	/**
	 * Checks if the chosen username represent a valid username, i.e if it is unique and if it 
	 * satisfies the APManager specifications.
	 * @param username a string representing a username
	 * @return true if the username is valid, false otherwise
	 * @throws InvalidUsernameException if the username does not satisfy the specifications
	 * @throws UsernameAlreadyExistsException if the username is not available
	 */
	public boolean isUsernameValid(String username) throws InvalidUsernameException{
		UsernameValidator validator = new UsernameValidator();
		if(!validator.validate(username) || username.contains(" "))
			throw new InvalidUsernameException("the username inserted is not valid");
		if(contains(username))
			throw new UsernameAlreadyExistsException("username already in use");
		return true;
	}
	
	/**
	 * Utility method which formats the xml file in order to remove redundant blank spaces.
	 * It is called after an element has been removed to delete the blank space left by its deletion.
	 */
	//STRUCTURAL. removes the extra spaces between nodes when deleting an element.
	public void format(){
		XPathFactory xpathFactory = XPathFactory.newInstance();
		// XPath to find empty text nodes.
		javax.xml.xpath.XPathExpression xpathExp = null;
		try {
			//this xpathExpression strips spaces from a specific node ( because (.) refers to the current node,
			//substituting everything with '' 
			xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
		} catch (XPathExpressionException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "invalid xPath expression");
			e.printStackTrace();
		}  
		NodeList emptyNodes = null;
		try {
			//get a list of text nodes that are "empty"
			emptyNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "invalid xPath expression");
		}

		// Remove nodes which are empty
		for (int i = 0; i < emptyNodes.getLength(); i++) {
			Node emptyTextNode = emptyNodes.item(i);
			emptyTextNode.getParentNode().removeChild(emptyTextNode);
		}
	}
	
	
	/**
	 * Writes the content of the source Document into the {@code users.xml} file.
	 * @param os a valid {@link OutputStream}, in this specific case the users file
	 */
	//write content in xml file
	public void write(OutputStream os){
		APManagerSystem.getSystemLogger().fine("User issued a write request.");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer tr = null;
		
		try {
			os = new FileOutputStream(APManagerSystem.USERS_FILE, false);
			tr = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException|FileNotFoundException e) {

			APManagerSystem.getSystemLogger().log(Level.SEVERE, "An exception occoured.", e);
		}
		tr.setOutputProperty(OutputKeys.INDENT, "yes");//indent automatically
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = null;
		
		try {
			result = new StreamResult(os);
			tr.transform(source, result);
			APManagerSystem.getSystemLogger().fine("user xml file updated");
		} catch (TransformerException e) {
			
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "an exception occoured", e);
		}
		
		finally{
			try {
				os.close();
			} catch (IOException e) {
				
				APManagerSystem.getSystemLogger().log(Level.SEVERE, "an exception occoured.", e);
			}
		}
		
	}
	
	
	/**
	 * Translates an xml {@link Element} representing a {@link User} into an Object. 
	 * @param element the Element to translate into Object
	 * @return an Object representing the Element as a User
	 * @throws NullPointerException if the Element cannot be parsed into a User instance
	 */
	public Object toObject(Element element){
		
		String username = element.getElementsByTagName("username").item(0).getTextContent();
		String password = element.getElementsByTagName("password").item(0).getTextContent();
		String profilePicPath = element.getAttribute("pic");
		
		User user = new User(username, password);
		user.setProfilePicPath(profilePicPath);
		
		return user;
	}
	
	/**
	 * Helper method that returns a {@link DocumentBuilder} if the specific {@linkplain #db field} is null. 
	 * @return a DocumentBuilder instance
	 */
	private DocumentBuilder getDocumentBuilder(){
		
		if(db == null){
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				APManagerSystem.getSystemLogger().log(Level.SEVERE, "an exception occoured", e);
			}
		}
		return db;
	}
	
	/**
	 * Convenience method for {@link DocumentBuilder#parse(File)}, prior checking if the class has a valid instance of 
	 * DocumentBuilder to avoid unexpected behaviors.
	 * @param input the input file
	 * @return the relative Document object
	 */
	public Document parse(File input) {
		Document doc = null;
		try {
			doc = getDocumentBuilder().parse(input);
		} catch (SAXException | IOException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "an exception occoured", e);
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	/**
	 * Getter for the Document field of the class
	 * @return the Document object
	 */
	public Document getDocument(){
		return doc;
	}
	

}
