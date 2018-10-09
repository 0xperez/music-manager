package atunibz.dperez.approject1617.user;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import atunibz.dperez.approject1617.xml.XMLWritable;



/**
	 * Represents an user in the APManager system. Each user must have a unique suitable username, password
	 * and a profile picture. User attributes are all represented from strings and to enforce
	 * security the password is encrypted via a {@link PasswordEncryptor}.
	 * @author Davide Perez
	 * @version 1.3
	 * @since 15/05/2017
	 */
	
public class User implements XMLWritable {
	
	/**
	 * A string representing the username.
	 */
	private String username;
	
	/**
	 * A string representing the password.
	 */
	private String password;
	
	/**
	 * A string representing the path of the user's profile picture.
	 */
	private String profilePicPath;
	
	/**
	 * Default constructor which creates a user with username and password both equal
	 * to "admin". The password is encrypted by a {@link PasswordEncryptor}.
	 */
	public User(){
		this.username = "admin";
		this.password = PasswordEncryptor.encryptPassword("admin");
	}
	
	/**
	 * Constructor which creates an user with the specified username and the specified password. 
	 * @param username a string representing the username
	 * @param password a string representing a password
	 */
	public User(String username, String password){
		this.username = username;
		this.password = PasswordEncryptor.encryptPassword(password);
	}
	
	/**
	 * Getter for the username field.
	 * @return a string representing the username
	 */
	public String getUsername() {
		return username;
	}
	
	//look better at this
	
	/**
	 * Setter for the username field.
	 * @param username a string representing a username
	 */
	public void setUsername(String username){
		this.username=username;
	}

	
	/**
	 * Getter for the password field. Note that the string returned is not the actual password value, but an
	 * hashed enrypted instance of such password.
	 * @return the current value of the password string
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setter for the password field.
	 * @param password a string representing the password
	 */
	public void setPassword(String password){
			this.password = password;
		}
	
	
	/**
	 * Getter for the profile picture path field.
	 * @return a string representing the profile picture path of the user
	 */
	public String getProfilePicPath() {
		return profilePicPath;
	}
	
	/**
	 * Setter for the profile picture path field.
	 * @param profilePicPath string representing the path of user's current profile picture
	 */
	public void setProfilePicPath(String profilePicPath) {
		this.profilePicPath = profilePicPath;
	}
	
	/**
	 * Generates an Element representation of the User instance. An Element is created out of the user instance and the
	 * corresponding fields are converted as well and appended as child of such Element. 
	 * @return an Element reflecting the User object
	 */
	@Override
	public Element toElement() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		
		Document doc = db.newDocument();//must create a new document in order to generate elements
		Element userElement = doc.createElement("user");
		Element username = doc.createElement("username");
		username.setTextContent(this.getUsername());
		userElement.appendChild(username);
		Element password = doc.createElement("password");
		password.setTextContent(this.getPassword());
		userElement.appendChild(password);
		userElement.setAttribute("pic", this.getProfilePicPath());
		
		return userElement;
	}	

}
