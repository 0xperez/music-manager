package atunibz.dperez.approject1617.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Simple utility class which provides a static method used to encrypt a user's password before saving it on the 
 * xml file. All passwords comparisons on this system, e.g. when a register user logs in and his credentials are 
 * compared with the one in the user.xml file, are done after the input has been encrypted. This happens because
 * the MD5 hash function is unidirectional, hence irreversible: once a string has been hashed, it is not possible
 * to convert it back to its original value.
 * <p>
 * @author Davide Perez
 * @version 1.0
 * @since 22/5/2017
 */
public class PasswordEncryptor {
	/**
	 * Static method that encrytpts the input string representing a password using an implementation of the MD5 algorithm, a
	 * cryptographic hash function which outputs as result a 128-bit hash value. Once the string has been 
	 * encrypted it is not possible to decode it to obtain the original password again.
	 * This method is called when a user is written on the user.xml file and on the user input during the authentication:
	 * if the hashed password in database matches with the hashed input string, then the user inserted the right password.
	 * @param passwordToHash string representing the password to encrypt.
	 * @return a string representing the hashed password
	 */
	public static String encryptPassword(String passwordToHash){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
	}

}
