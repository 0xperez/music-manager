package atunibz.dperez.approject1617.exceptions;

import java.io.IOException;
/**
 * Signals that a user has attempted to register with a username that does not follow the APManager specifications.
 * @author Davide Perez
 * @version 1.0
 * @since 15/5/2017
 *
 */
public class InvalidUsernameException extends IOException {
	/**
	 * Constructs an instance of this class with {@code null} as its error message
	 */
	public InvalidUsernameException(){
		
	}
	/**
	 * Constructs an instance of this class with the specified error message
	 * @param message the message specifying the error
	 */
	public InvalidUsernameException(String message){
		super(message);
	}
	/**
	 * Constructs an instance of this class specifying the cause
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidUsernameException(Throwable cause){
		super(cause);
	}
	/**
	 * Constructs an instance of this class with a specified error message and the cause of the exception
	 * @param message the error message
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidUsernameException(String message, Throwable cause){
		super(message, cause);
	}

}
