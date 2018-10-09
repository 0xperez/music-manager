package atunibz.dperez.approject1617.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import atunibz.dperez.approject1617.data.DataInspector.QueryAttribute;

public class DataInspectorTest {
	
	static DataInspector query = new DataInspector();
	static ArrayList<Track> mockList = new ArrayList<Track>();
	static Track t1, t2, t3, t4;
	//creates a "mock" data ArrayList which simulates the internal tracklist of the
	//application. They are both ArrayList, and such a substitution is required since otherwise there
	//is no way to forecast with absolute precision the correctness of the test: there
	//may be other tracks whose attributes are similar to the test tracks
	@BeforeClass
	public static void createMockData(){
		mockList = new ArrayList<Track>();
		t1 = new Track("testTitle", "test", "test", "test", "test", "Writer", 1000);
		t1.setAddedBy("test");
		t2 = new Track("testTitle2", "testArt", "test2", "test2", "test2", "thiswriter", 2000);
		t2.setAddedBy("test");
		t3 = new Track("testTitle3", "testArt", "test3", "test3", "test3", "writerthis", 2000);
		t3.setAddedBy("test");
		t4 = new Track("junit", "junit", "junit", "junit", "junit", "writer", 0);
		t4.setAddedBy("junit");
		mockList.add(t1);
		mockList.add(t2);
		mockList.add(t3);
		mockList.add(t4);
		query.setData(mockList);
	}
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is contained in 
	//all of the found tracks
	@Test
	public void testFindByTitle() {
		ArrayList<Track> results = query.findBy(QueryAttribute.TITLE, "test");
		assertEquals(results.size(), 3);
		for(Track t : results){
			assertTrue(t.getTitle().contains("test"));
		}
	}
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is equals to the artist 
	//all of the found tracks
	@Test
	public void testFindByArtist() {
		ArrayList<Track> results = query.findBy(QueryAttribute.ARTIST, "testArt");
		assertEquals(results.size(), 2);
		for(Track t : results){
			assertTrue(t.getArtist().contains("testArt"));
		}
	}
	
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is equals to the 
	//all of the found tracks
	@Test
	public void testFindByAlbum() {
		ArrayList<Track> results = query.findBy(QueryAttribute.ALBUM, "junit");
		assertEquals(results.size(), 1);
		for(Track t : results){
			assertEquals(t.getArtist(), "junit");
		}
	}
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is common to all
	//the found track's field
	@Test
	public void testFindByGender() {
		ArrayList<Track> results = query.findBy(QueryAttribute.GENDER, "test2");
		assertEquals(results.size(), 1);
		for(Track t : results){
			assertFalse(t.getArtist().equals("test2"));
		}
	}
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is equals to the label field in 
	//all of the found tracks
	@Test
	public void testFindByLabel() {
		ArrayList<Track> results = query.findBy(QueryAttribute.LABEL, "TEST2");
		assertEquals(results.size(), 1);
		for(Track t : results){
			assertEquals(t.getLabel(), "TEST2".toLowerCase());//toLowerCase
			//because the query is not case sensitive
		}
	}
	
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is contained in the 
	//writer field of all of the found tracks
	@Test
	public void testFindByWriter() {
		ArrayList<Track> results = query.findBy(QueryAttribute.WRITER, "writer");
		assertEquals(results.size(), 4);
		for(Track t : results){
			assertTrue(t.getWriter().toLowerCase().contains("writer"));//to lower
			//case because the query is not case-sensitive
		}
	}
	
	//queries the simulated data list and checks if the number of tracks found
	//is equals to the expected one, then checks if the query is equals to the year 
	//of of the found tracks
	@Test
	public void testFindByYear() {
		ArrayList<Track> results = query.findBy(QueryAttribute.YEAR, "2000");
		assertEquals(results.size(), 2);
		for(Track t : results){
			assertEquals(t.getYear(), Integer.parseInt("2000"));
		}
	}
	
	//queries a simulated data searching for a non-existent track
	@Test
	public void failedQuery(){
		ArrayList<Track> results = query.findBy(QueryAttribute.TITLE, "notFound");
		assertTrue(results.size() == 0);
	}
	
	//tests the fact that an empty string matches any track
	@Test
	public void emptyQuery() {
		ArrayList<Track> results = query.findBy(QueryAttribute.ARTIST, "");
		assertEquals(results.size(), 4);
	}

	
	
	

}
