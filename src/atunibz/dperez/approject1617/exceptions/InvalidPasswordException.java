package atunibz.dperez.approject1617.exceptions;

import java.io.IOException;
/**
 * Signals that a user has attempted to register with a password that is shorter than 5 characters.
 * @author Davide Perez
 * @version 1.0
 * @since 15/5/2017
 *
 */
public class InvalidPasswordException extends IOException {
	/**
	 * Constructs an instance of this class with {@code null} as its error message
	 */
	//custom exception to be thrown if a password is not proper, i.e if it is too short.
	public InvalidPasswordException(){
		
	}
	/**
	 * Constructs an instance of this class with the specified error message
	 * @param message the message specifying the error
	 */
	public InvalidPasswordException(String message){
		super(message);
	}
	/**
	 * Constructs an instance of this class specifying the cause
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidPasswordException(Throwable cause){
		
	}
	/**
	 * Constructs an instance of this class with a specified error message and the cause of the exception
	 * @param message the error message
	 * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown
	 */
	public InvalidPasswordException(String message, Throwable cause){
		
	}

}
