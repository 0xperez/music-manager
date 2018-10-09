package atunibz.dperez.approject1617.exceptions;
/**
 * Signals that a user has attempted to register with a username that is already in use.
 * @author Davide Perez
 * @version 1.0
 * @since 16/5/2017
 *
 */
public class UsernameAlreadyExistsException extends InvalidUsernameException {
	/**
	 * Constructs an instance of this class with {@code null} as its error message
	 */
	public UsernameAlreadyExistsException(){
		
	}
	
	/**
	 * Constructs an instance of this class with the specified error message
	 * @param message the message specifying the error
	 */
	public UsernameAlreadyExistsException(String message){
		super(message);
	}
	
	/**
	 * Constructs an instance of this class with a specified error message and the cause of the exception
	 * @param message the error message
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public UsernameAlreadyExistsException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 * Constructs an instance of this class specifying the cause
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public UsernameAlreadyExistsException(Throwable cause){
		super(cause);
	}

}
