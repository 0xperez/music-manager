package atunibz.dperez.approject1617.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.exceptions.InvalidXMLImportException;
import atunibz.dperez.approject1617.system.APManagerSystem;
/**
 * This class handles the validation and the parsing of an external {@code .xml} file to import tracks
 * into the local data file. For an {@code .xml} file to be succesfully validated and imported, it has to satisfy the APManager import policy listed below.
 * <p>
 * <b>APManager import policy</b><br>
 * The file must be a valid xml file with the {@code .xml} extension. The content must consist in a {@code <data>} tag which holds the set of track elements. Each track element must be enclosed 
 * in a {@code <track>} tag and have as children an element per field in the correct order. The {@code <year>} element, although is a string as any other xml element, will be parsed as an integer
 * when the track is imported. Hence, it should contain only string values that can be parsed in an integer, i.e digits from 0 to 9.<br>If any of this condition is not met, the import will fail.
 * <p>
 * <b>Example:</b>
 * <p>
 * {@code <data>}<br>
 * {@code <track>}<br>
 * {@code <title> yourtitle </title>}<br>
 * {@code <artist> yourartist </artist>}<br>
 * {@code <album> youralbum </album>}<br>
 * {@code <gender> yourgender </gender>}<br>
 * {@code <label> yourlabel </label>}<br>
 * {@code <writer> yourwriter </writer>}<br>
 * {@code <year> youryear </year>}<br>
 * {@code <addedBy> addedbyuser </addedBy>}<br>
 * {@code </track>}<br>
 * {@code ...other tracks...}<br>
 * {@code </data>}<br>
 *  
 * @author Davide Perez
 * @since 25/5/2017
 * @version 1.3
 *
 */
public class XMLImporter {
	/**
	 * Reference to the schema file dataschema.xsd 
	 */
	private static final File SCHEMA;
	/**
	 * Factory necessary to create Schema instances
	 */
	private static final SchemaFactory FACTORY;
	/**
	 * Schema object. Represents a set of rules to validate an xml file against specified constraints.
	 * Such constraints are represented by a {@code .xsd} file.
	 */
	private Schema schema;
	/**
	 * Validator object to execute the validation process against schema
	 */
	private Validator validator;
	/**
	 * The file to import
	 */
	private File externalXML;
	/**
	 * Initialization of static variables. The instructions in the static block are executed once 
	 * when the class is first loaded and initializing static final variables here optimizes the 
	 * resource management.
	 */
	static{
		SCHEMA = new File("xml/dataschema.xsd");
		FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}
	
	/**
	 * Constructor that accepts a file as parameter
	 * @param externalXML the file containing the tracks to import
	 */
	public XMLImporter(File externalXML){
		
		try {
			schema = FACTORY.newSchema(SCHEMA);
		} catch (SAXException e) {
			APManagerSystem.getSystemLogger().severe("error while initializing xsd schema");
			e.printStackTrace();
		}
		
		validator = schema.newValidator();
		this.externalXML = externalXML;
	}
	
	/**
	 * Validates the file {@link #externalXML} against the {@code .xsd} schema to check if it respects the import policy.
	 * @return true if {@link #externalXML} is a file respecting the import policy, false otherwise
	 */
	private boolean isXMLValid(){
		APManagerSystem.getSystemLogger().info("validating " + externalXML.getAbsolutePath());
		try {
			validator.validate(new StreamSource(externalXML));
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			APManagerSystem.getSystemLogger().warning("invalid xml import. " + e.getMessage());
			return false;
		}
		APManagerSystem.getSystemLogger().info("xml file valid");
		return true;
	}
	
	/**
	 * Checks the {@link #externalXML} file and parses it into a Document if valid.
	 * @return a Document representing the external file
	 * @throws InvalidXMLImportException if the file does not satisfy the import policy
	 */
	private Document parseExternalDocument() throws InvalidXMLImportException{
		
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			APManagerSystem.getSystemLogger().severe("error while configuring the parser");
			e.printStackTrace();
		}
		if(isXMLValid())
			try {
				doc = db.parse(externalXML);
			} catch (SAXException | IOException e) {
				APManagerSystem.getSystemLogger().severe("error while importing external .xml file");
				e.printStackTrace();
			}
		
		else{
			throw new InvalidXMLImportException("invalid .xml file");
		}
		
		return doc;
	}
	
	/**
	 * This method processes the Document obtained by parsing the imported file by reading every 
	 * track element into a {@link Track} and returning an {@link ArrayList} of such objects.
	 * @return an ArrayList of Track objects
	 * @throws InvalidXMLImportException if the imported file does not satisfy the import policy
	 */
	public ArrayList<Track> getExternalTracks() throws InvalidXMLImportException{
		ArrayList<Track> externalTracks = new ArrayList<Track>();
		Document doc = parseExternalDocument();
		NodeList tracks = doc.getElementsByTagName("track");
		APManagerSystem.getSystemLogger().info("found " + tracks.getLength() + " tracks to import");
		for(int i = 0; i < tracks.getLength(); i++){
			Element element = (Element) tracks.item(i);
			String title = element.getElementsByTagName("title").item(0).getTextContent();
			String artist = element.getElementsByTagName("artist").item(0).getTextContent();
			String album = element.getElementsByTagName("album").item(0).getTextContent();
			String gender = element.getElementsByTagName("gender").item(0).getTextContent();
			String label = element.getElementsByTagName("label").item(0).getTextContent();
			String writer = element.getElementsByTagName("writer").item(0).getTextContent();
			String yearString = element.getElementsByTagName("year").item(0).getTextContent();
			int year = Integer.parseInt(yearString);
			Track track = new Track(title, artist, album, gender, label, writer, year);
			//track.setAddedBy(MainFrame.getMainFrame().getCurrentUser().getUsername());
			externalTracks.add(track);
			APManagerSystem.getSystemLogger().fine("track imported");
		}
		APManagerSystem.getSystemLogger().info(externalTracks.size() + " tracks imported");
		return externalTracks;
	}
	
	
	
}
