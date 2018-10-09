package atunibz.dperez.approject1617.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import atunibz.dperez.approject1617.gui.linux.MainFrame;
import atunibz.dperez.approject1617.xml.XMLWritable;

/**
 * Instances of this class represent a musical track and its attributes.
 * @author Davide Perez
 * @version 1.3
 * @since 15/5/2017
 *
 */
public class Track implements XMLWritable {
	/**
	 * The song's title
	 */
	private String title;
	/**
	 * The song's artist
	 */
	private String artist;
	/**
	 * The album the song belong to
	 */
	private String album;
	/**
	 * The song's gender
	 */
	private String gender;
	/**
	 * The song's label
	 */
	private String label;
	/**
	 * The song's writer
	 */
	private String writer;
	/**
	 * The year in which the song has been released
	 */
	private int year;
	/**
	 * The user that added this song to the {@code xml} file.
	 */
	private String addedBy;
	
	/**
	 * Constructs a Track object with no field values
	 */
	public Track(){
	}

	/**
	 * Constructs a Track object with the specified field values
	 * @param title the song title
	 * @param artist the song artist
	 * @param album the song's album
	 * @param gender the song's gender
	 * @param label the record label
	 * @param writer the writer of this song
	 * @param year the year of production of the song (in an integer representation)
	 */
	public Track(String title, String artist, String album, String gender, String label, String writer, int year) {

		this.title = title;
		this.artist = artist;
		this.album = album;
		this.gender = gender;
		this.label = label;
		this.writer = writer;
		this.year = year;
		this.addedBy = "admin";
	}
	
	
	public String toString(){
		
		return artist + " - " + title;
	}
	
	/**
	 * Returns a detailed string representation of the Track, listing all its field current values
	 * @return a string describing all the track's fields
	 */
	public String toStringDetailed(){
		return "Title: " + title + "\nArtist: " + artist + "\nAlbum: " + album + "\nGender: " + gender +
				"\nLabel: " + label + "\nWriter: " + writer + "\nYear: " + year + "\nAdded by: " + addedBy;
	}

	/**
	 * Getter for the title field
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for the title field
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for the title field
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Setter for the artist field
	 * @param artist the new artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * Getter for the album field
	 * @return the album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * Setter for the album field
	 * @param album the new album
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * Getter for the gender field
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Setter for the gender field
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Getter for the label field
	 * @return the record label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setter for the label field
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter for the writer field
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * Setter for the writer field
	 * @param writer the new writer
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * Getter for the year field
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Setter for the year field
	 * @param year the new year value
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Getter for the addedBy field
	 * @return the username of the user who added this song
	 */
	public String getAddedBy(){
		return addedBy;
	}
	/**
	 * Setter for the addedBy field
	 * @param addedBy the username of the user who added this song. Usually it is given by the 
	 * {@linkplain MainFrame#getCurrentUser() getCurrentUser()} method.
	 */
	public void setAddedBy(String addedBy){
		this.addedBy = addedBy;
	}

	/**
	 * Returns an {@link Element} representing the Track object. The generated Element has as children elements
	 * all the Track's fields in the order they are declared
	 */
	@Override
	public Element toElement() {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			Logger.getGlobal().log(Level.SEVERE, "error while converting track to xml element", e);
			
		}
		
		Document doc = db.newDocument();
		
		Element trackElement = doc.createElement("track");
		
		Element title = doc.createElement("title");
		title.setTextContent(this.title);
		trackElement.appendChild(title);
		
		Element artist = doc.createElement("artist");
		artist.setTextContent(this.artist);
		trackElement.appendChild(artist);
		
		Element gender = doc.createElement("gender");
		gender.setTextContent(this.gender);
		trackElement.appendChild(gender);
		
		Element album = doc.createElement("album");
		album.setTextContent(this.album);
		trackElement.appendChild(album);
		
		Element label = doc.createElement("label");
		label.setTextContent(this.label);
		trackElement.appendChild(label);
		
		Element writer = doc.createElement("writer");
		writer.setTextContent(this.writer);
		trackElement.appendChild(writer);
		
		Element year = doc.createElement("year");
		year.setTextContent(this.year + "");
		trackElement.appendChild(year);

		Element addedBy = doc.createElement("addedBy");
		addedBy.setTextContent(this.addedBy);
		trackElement.appendChild(addedBy);
		
		return trackElement;
	}
	
}
