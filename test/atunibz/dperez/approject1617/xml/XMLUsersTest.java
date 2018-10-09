package atunibz.dperez.approject1617.xml;

import static org.junit.Assert.*;
import org.junit.Test;
import atunibz.dperez.approject1617.user.User;

public class XMLUsersTest {
	
	XMLUsers userXML = new XMLUsers();
	User user = new User("test", "test");
	
	//test add() by adding a user and checking if the number of users
	//has increased by 1 (the user is then remove to allow the other tests to run
	//properly).
	@Test
	public void testAdd() {
		int numberOfUsersBefore = userXML.getDocument().getElementsByTagName("user").getLength();
		userXML.add(user);
		int numberOfUsersAfter = userXML.getDocument().getElementsByTagName("user").getLength();
		assertEquals(numberOfUsersAfter, numberOfUsersBefore + 1);
		userXML.remove("test");//removed to do not affect other tests
	}
	
	//adds a user and checks if it is contained in the file
	@Test
	public void testAdd2(){
		userXML.add(user);
		assertTrue(userXML.contains(user.getUsername()));
		userXML.remove(user.getUsername());//removed to do not affect other tests
	}
	
	
	//adds a user, checks the number of users in the file and checks if it decreases after removing it
	//and if the user is not present in the file
	@Test
	public void testRemove() {
		userXML.add(user);
		int numberOfUsersBefore = userXML.getDocument().getElementsByTagName("user").getLength();
		userXML.remove("test");
		int numberOfUsersAfter = userXML.getDocument().getElementsByTagName("user").getLength();
		assertTrue(numberOfUsersAfter == numberOfUsersBefore - 1 && !userXML.contains("test"));
	}
	
	
	//attempts to remove a user that does not exist
	@Test(expected = NullPointerException.class)
	public void testRemove2(){
		userXML.remove("test");
	}
	
	
	//tests if a user is contained in the users.xml file after adding it
	@Test
	public void testContains() {
		userXML.add(user);
		assertTrue(userXML.contains("test"));
		userXML.remove("test");//removes the user to do not affect other tests
	}
	
	//test if a user is not contained in the users.xml file
	@Test
	public void testContains2(){
		assertFalse(userXML.contains("test"));
	}
	
	//adds a user and then mocks some login attempts
	@Test
	public void testAuthenticate(){
		userXML.add(user);
		assertTrue(userXML.authenticate("test", "test"));
		assertFalse(userXML.authenticate("test", "tset"));
		assertFalse(userXML.authenticate("tset", "test"));
		userXML.remove("test");//removes the user to do not affect other tests
	}

}
