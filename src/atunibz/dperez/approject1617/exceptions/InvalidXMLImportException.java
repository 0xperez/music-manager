package atunibz.dperez.approject1617.exceptions;
/**
 * Signals that a user has attempted to import a file which is not an {@code .xml} file or does
 * not respect the {@linkplain atunibz.dperez.approject1617.xml.XMLImporter import policy}. 
 * @author Davide Perez
 * @version 1.0
 * @since 18/5/2017
 *
 */
public class InvalidXMLImportException extends Exception {
	/**
	 * Constructs an instance of this class with {@code null} as its error message
	 */
	public InvalidXMLImportException(){
		
	}
	/**
	 * Constructs an instance of this class with the specified error message
	 * @param message the message specifying the error
	 */
	public InvalidXMLImportException(String message) {
		super(message);
	}
	/**
	 * Constructs an instance of this class specifying the cause
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidXMLImportException(Throwable cause){
		super(cause);
	}
	/**
	 * Constructs an instance of this class with a specified error message and the cause of the exception
	 * @param message the error message
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidXMLImportException(String message, Throwable cause){
		super(message, cause);
	}

}
