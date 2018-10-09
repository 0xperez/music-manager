package atunibz.dperez.approject1617.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.exceptions.InvalidXMLImportException;
import atunibz.dperez.approject1617.gui.linux.MainFrame;
import atunibz.dperez.approject1617.system.APManagerSystem;
/**
 * Class which handles the data.xml file in which the application data is stored. Provides methods 
 * to perform various operations on such file.
 * @author Davide Perez
 * @version 1.7
 * @since 20/5/2017
 *
 */
public class XMLTracks implements XMLWriter {
	
	/**
	 * The Document object representing the xml document
	 */
	private static Document doc;
	/**
	 * Factory to obtain a DocumentBuilder instance
	 */
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	/**
	 * DocumentBuilder which is used to instantiate Document instances
	 */
	private static DocumentBuilder db;
	/**
	 * OutputStream used to write the Document to a file
	 */
	private OutputStream outputStream;
	
	/**
	 * Constructor of the class. Parses the data.xml file into a Document in order to allow operations to be performed on it.
	 * If the data.xml file is missing for some reason, an empty file with the same structure is created programmatically.
	 */
	XMLTracks(){

		if(!APManagerSystem.DATA_FILE.exists()){
			APManagerSystem.getSystemLogger().warning("no valid data file found. A new xml will be created");
			createXML();
		}	
		
		else{
			doc = parse(APManagerSystem.DATA_FILE);
		}
	}
	
	/**
	 * Convenience method for {@link DocumentBuilder#parse(File)}, prior checking if the class has a valid instance of 
	 * DocumentBuilder to avoid unexpected behaviors.
	 * @param input the input file
	 * @return the relative Document object
	 */
	public static Document parse(File input) {
		Document doc = null;
		try {
			doc = getDocumentBuilder().parse(input);
		} catch (SAXException | IOException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "error while parsing the document");
		}
		doc.getDocumentElement().normalize();
		return doc;
	}
	/**
	 * Adds a track to the data file. Accepts as parameter any instance implementing {@link XMLWritable} interface, but in this 
	 * specific implementation it has to be a {@link Track}.
	 * If the file contains a track which is equal to the one that is about to be inserted accorded to the definition,
	 * then a JDialog is shown and the track is not added. If not, the track is added and the file is updated.
	 * @param track the track to add
	 * @throws ClassCastException if the parameter is not an instance of {@link Track}
	 * 
	 */
	@Override
	public void add(XMLWritable track){
		if(contains((Track)track)){
			APManagerSystem.getSystemLogger().info("user attempted to insert a track that already exists");
			return;
		}
		Element rootElement = doc.getDocumentElement();
		Element externalNode = (Element)doc.adoptNode(track.toElement());
		rootElement.appendChild(externalNode);
		write(outputStream);
		MainFrame.CURRENT_SESSION_ADDED++;
		APManagerSystem.getSystemLogger().fine("a new track has been added to the data file");
	}
	
	/**
	 * Utility method used in {@link XMLTracks#add(XMLWritable)} to check whether the track to be 
	 * added is already contained in the data file, i.e. if their title and artist are equals.
	 * @param track a track object
	 * @return true if the track is already in the data file, false otherwise
	 */
	public boolean contains(Track track){
		ArrayList<Track> tracks = getList();
		for(Track current : tracks){
			if(current.getTitle().equals(track.getTitle()) && current.getArtist().equals(track.getArtist()))
				return true;
		}
		
		return false;
	}

	/**
	 * Deletes the track passed as parameter from the data file.
	 * @param track the track to delete
	 * @return true if the track has been found and deleted, false otherwise
	 */
	public boolean deleteTrack(Track track){
		String title = track.getTitle();
		String artist = track.getArtist();
		NodeList tracks = doc.getElementsByTagName("track");
		//Element trackToDelete = track.toElement();
		for(int i = 0; i < tracks.getLength(); i++){
			Element currentTrack = (Element) tracks.item(i);
			String currentTitle = currentTrack.getElementsByTagName("title").item(0).getTextContent();
			String currentArtist = currentTrack.getElementsByTagName("artist").item(0).getTextContent();
			if(currentTitle.equals(title) && currentArtist.equals(artist)){
				APManagerSystem.getSystemLogger().fine("entry found. Deleting");
				currentTrack.getParentNode().removeChild(currentTrack);
				APManagerSystem.getSystemLogger().fine("track removed");
				format();
				write(outputStream);
				MainFrame.CURRENT_SESSION_DELETED++;
				return true;
			}
		}
		APManagerSystem.getSystemLogger().warning("no entry found to delete");
		return false;
	}
	
	
	//STRUCTURAL. removes the extra spaces between nodes when deleting an element.
	/**
	 * Utility method which formats the xml file in order to remove redundant blank spaces.
	 * It is called after an element has been removed to delete the blank space left by its deletion.
	 */
	@Override
	public void format(){
		XPathFactory xpathFactory = XPathFactory.newInstance();
		// XPath to find empty text nodes.
		javax.xml.xpath.XPathExpression xpathExp = null;
		try {
			//this xpathExpression strips spaces from a specific node ( because (.) refers to the current node,
			//substituting everything with '' 
			xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
		} catch (XPathExpressionException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "invalid xpath");
			e.printStackTrace();
		}  
		NodeList emptyNodes = null;
		try {
			//get a list of text nodes that are "empty"
			emptyNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "invalid xPath expression");
			e.printStackTrace();
		}
		// Remove nodes which are empty
		for (int i = 0; i < emptyNodes.getLength(); i++) {
			Node emptyTextNode = emptyNodes.item(i);
			emptyTextNode.getParentNode().removeChild(emptyTextNode);
		}
	}
	
	
	/**
	 * Creates an {@link ArrayList} of {@link Track} out of the {@code data.xml} file. 
	 * @return an {@link ArrayList} of {@link Track}s
	 */
	public ArrayList<Track> getList(){
		ArrayList<Track> list = new ArrayList<Track>();
		if(doc == null)
			doc = parse(APManagerSystem.DATA_FILE);
		NodeList tracks = doc.getElementsByTagName("track");
		
		for(int i = 0; i < tracks.getLength(); i++){
			//creates the track object and adds it to the list
			Element current = (Element) tracks.item(i);
			list.add((Track)toObject(current));
		}
		return list;
	}
	
	/**
	 * Translates an xml Element representing a {@link Track} into an Object. 
	 * @param element the Element to translate into Object
	 * @return an Object representing the Element as a Track
	 * @throws NullPointerException if the element is cannot be parsed into a Track instance
	 */
	@Override
	public Object toObject(Element element){
		
		String title = element.getElementsByTagName("title").item(0).getTextContent();
		String artist = element.getElementsByTagName("artist").item(0).getTextContent();
		String album = element.getElementsByTagName("album").item(0).getTextContent();
		String gender = element.getElementsByTagName("gender").item(0).getTextContent();
		String label = element.getElementsByTagName("label").item(0).getTextContent();
		String writer = element.getElementsByTagName("writer").item(0).getTextContent();
		String yearString = element.getElementsByTagName("year").item(0).getTextContent();
		int year = Integer.parseInt(yearString);
		String addedBy = element.getElementsByTagName("addedBy").item(0).getTextContent();
		
		
		Track track = new Track(title, artist, album, gender, label, writer, year);
		track.setAddedBy(addedBy);
		
		return track;
		}
	
	/**
	 * Creates an xml with an empty {@code <data>} element.
	 */
	//STRUCTURAL. Creates empty xml <data> </data>
	private void createXML(){
		db = getDocumentBuilder();
		doc = db.newDocument();
		Element root = doc.createElement("data");
		doc.appendChild(root);
		doc.getDocumentElement().normalize();
		write(outputStream);

	}
	
	/**
	 * Helper method that returns a {@link DocumentBuilder} if the specific {@linkplain #db field} is null. 
	 * @return a DocumentBuilder instance
	 */
	private static DocumentBuilder getDocumentBuilder(){
		if(db == null){
			try {
				APManagerSystem.getSystemLogger().finer("No valid DocumentBuilder instance found. Creating a new one");
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				APManagerSystem.getSystemLogger().log(Level.SEVERE, "an exception occoured", e);
				e.printStackTrace();
			}
		}

		return db;
	}
	
	/**
	 * Writes the content of the source Document into the {@code data.xml} file.
	 * @param os a valid {@link OutputStream}, in this specific case the data file
	 */
	//write content in xml file
	@Override
	public void write(OutputStream os){
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer tr = null;
		try {
			tr = transformerFactory.newTransformer();
			os = new FileOutputStream(APManagerSystem.DATA_FILE, false);
		} catch (TransformerConfigurationException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "error while updating data.xml file");
			e.printStackTrace();
		}
		//options to correctly indent the xml tags and their content
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = null;

		try {
			result = new StreamResult(os);
			tr.transform(source, result);
			APManagerSystem.getSystemLogger().fine("XML file has been updated");
		} catch (TransformerException e) {
			APManagerSystem.getSystemLogger().log(Level.SEVERE, "error while updating the data file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Import the tracks from an external source.
	 * @see XMLImporter
	 * @param dataSource the file from which import the tracks
	 * @throws InvalidXMLImportException if the file does not satisfy the {@linkplain XMLImporter import policy}.
	 */
	//add a check that controls if a track already exists before importing it (maybe in the
	//add method). Assume a track is equal if has exactly same title and same artist
	public void importTracks(File dataSource) throws InvalidXMLImportException{
		XMLImporter importer = new XMLImporter(dataSource);
		ArrayList<Track> newTracks = importer.getExternalTracks();
		for(Track current : newTracks){
			//sets the added by field and adds the song to the file
			current.setAddedBy(MainFrame.getMainFrame().getCurrentUser().getUsername());
			add(current);
		}
		JOptionPane.showMessageDialog(null, "Import completed!", "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
		APManagerSystem.getSystemLogger().fine("import completed");
	}
	
	/**
	 * Getter for the Document field of the class
	 * @return the Document object
	 */
	public Document getDocument() {
		return doc;
	}

}