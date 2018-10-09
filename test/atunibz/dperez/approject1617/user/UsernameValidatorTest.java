package atunibz.dperez.approject1617.user;

import static org.junit.Assert.*;

import org.junit.Test;

import atunibz.dperez.approject1617.user.UsernameValidator;


//test class for UsernameValidator. Tests 1-3 are valid nicknames, the others are not.
public class UsernameValidatorTest {

	@Test
	public void testValidate_01() {
		UsernameValidator validator = new UsernameValidator();
		String username = "admin";
		assertTrue(validator.validate(username));
	}
	
	@Test
	public void testValidate_02() {
		UsernameValidator validator = new UsernameValidator();
		String username = "123twinklestar";
		assertTrue(validator.validate(username));
	}
	
	@Test
	public void testValidate_03() {
		UsernameValidator validator = new UsernameValidator();
		String username = "user_1";
		assertTrue(validator.validate(username));
	}
	
	@Test
	public void testValidate_04() {
		UsernameValidator validator = new UsernameValidator();
		String username = "a.new_user";
		assertFalse(validator.validate(username));
	}
	
	@Test
	public void testValidate_05() {
		UsernameValidator validator = new UsernameValidator();
		String username = "hola";
		assertFalse(validator.validate(username));
	}
	
	@Test
	public void testValidate_06() {
		UsernameValidator validator = new UsernameValidator();
		String username = "hi1.";
		assertFalse(validator.validate(username));
	}
	
	@Test
	public void testValidate_07() {
		UsernameValidator validator = new UsernameValidator();
		String username = "ca$hmachine";
		assertFalse(validator.validate(username));
	}


}
