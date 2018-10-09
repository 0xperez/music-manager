package atunibz.dperez.approject1617.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Provides a method to check whether a string represents a valid username, i.e. if satisfies APManager specifications.
 *The class uses a regular expression in order to check whether an input string represents a well-formed username or not.
 *<p>
 * @author Davide Perez
 * @version 1.0
 * @since 16/5/2017
 */
public class UsernameValidator {
	
	/**
	 * Pattern object to compile the regex.
	 */
	private Pattern pattern;
	
	/**
	 * Matcher object to check regex matching.
	 */
	private Matcher matcher;
	
	/**Regular expression that matches a string of minimum length 5, made up of any combination
	*of alphanumerics characters and at most one special character of the char class [.-_].
	*/
	//private final String REGEX = "^(?=.{5})[a-z0-9]*([-_.][a-z0-9]*)?$";
	//matches a string of min. 5 chars, made up of any combination of alphanumerics characters and at most one
	//special char between . _ and -
	private final String REGEX = "^(?=.{5})[a-zA-Z0-9]*([-_.][a-zA-Z0-9]*)?$";
	
	
	/**
	 * Instantiates a UsernameValidator object with a Pattern
	 * representing the compiled regular expression.
	 */
	public UsernameValidator(){
		
		pattern = Pattern.compile(REGEX);
	}
	
	
	/**
	 * Validates an input string by checking if it matches the regex.
	 * @param username the string representing the username to validate.
	 * @return <b>true</b> if the username is valid, <b>false</b> otherwise.
	 */
	public boolean validate(String username){
		
		matcher = pattern.matcher(username);
		
		return matcher.matches();
	}
	

}
