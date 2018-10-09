package atunibz.dperez.approject1617.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import org.junit.Test;
import org.w3c.dom.NodeList;
import atunibz.dperez.approject1617.exceptions.*;
import atunibz.dperez.approject1617.data.Track;

public class XMLTracksTest {
	
	XMLTracks trackXML = new XMLTracks();
	Track track = new Track("test", "test", "test", "test", "test", "test", 0);
	
	//checks if the number of tracks is increased by 1 after having added one
	@Test
	public void testAdd() {
		int numberOfUsersBefore = trackXML.getDocument().getElementsByTagName("track").getLength();
		trackXML.add(track);
		int numberOfUsersAfter = trackXML.getDocument().getElementsByTagName("track").getLength();
		assertEquals(numberOfUsersAfter, numberOfUsersBefore + 1);
		trackXML.deleteTrack(track);//removed to do not affect other tests
	}
	
	//checks if the track is not present in the file, then adds it and checks again
	@Test
	public void testAdd2(){
		assertFalse(trackXML.contains(track));
		trackXML.add(track);
		assertTrue(trackXML.contains(track));
		trackXML.deleteTrack(track);
	}
	
	//adds a track, checks the number of tracks in the file and checks if it decreases after removing it
	//and if the track is then not present in the file
	@Test
	public void testDeleteTrack() {
		trackXML.add(track);
		int numberOfUsersBefore = trackXML.getDocument().getElementsByTagName("track").getLength();
		trackXML.deleteTrack(track);
		int numberOfUsersAfter = trackXML.getDocument().getElementsByTagName("track").getLength();
		assertTrue(numberOfUsersAfter == numberOfUsersBefore-1 && !trackXML.contains(track));
	}
	
	//gets the track list and the list of xml nodes and checks if they have same size
	//then checks if every track is present in the xml file
	@Test
	public void testGetList() {
		ArrayList<Track> tracks = trackXML.getList();
		NodeList nodes = trackXML.getDocument().getElementsByTagName("track");
		assertEquals(tracks.size(), nodes.getLength());
		for(Track t : tracks){
			assertTrue(trackXML.contains(t));
		}
	}

	//test the import tracks feature by importing some tracks from a file and checking if the 
	//size has increased correctly (then deletes the added tracks to do not affect other tests)
	@Test
	public void testImportTracks() throws InvalidXMLImportException {
		int numberOfTracksBefore = trackXML.getDocument().getElementsByTagName("track").getLength();
		XMLImporter importer = new XMLImporter(new File("test_files/test_data.xml"));
		ArrayList<Track> imported = importer.getExternalTracks();//gets the external tracks

		for(Track t : imported){
			trackXML.add(t);
		}
		int numberOfTracksAfter = trackXML.getDocument().getElementsByTagName("track").getLength();
		assertEquals(numberOfTracksAfter, numberOfTracksBefore + imported.size());//checks if the 
		//number of tracks present after the import is equals to the number of tracks present
		//before plus the size of the arraylist holding the imported tracks
		for(Track t : imported){
			trackXML.deleteTrack(t);
		}
	}
	
	//attempts to import an xml file that does not satisfy the import policy
	@Test(expected = InvalidXMLImportException.class)
	public void testImportTracks3() throws InvalidXMLImportException{
		trackXML.importTracks(new File("test_files/corrupted_data.xml"));
		
	}

}
