package fi.tkk.tml.xformsdb.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Convenience class for handling exceptions.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 02, 2008
 */
public class ExceptionUtils {

	
		
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( ExceptionUtils.class );

	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private ExceptionUtils() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}

	
	// PUBLIC STATIC METHODS
	/**
	 * Retrive the message.
	 * 
	 * 
	 * @param throwable		The throwable.
	 * @return message		The message.
	 */
	public static String getMessage( Throwable throwable ) {
		return ( throwable == null ) ? null : throwable.getMessage();
	}

	
	/**
	 * Retrive the cause.
	 * 
	 * 
	 * @param throwable		The throwable.
	 * @return message		The cause.
	 */
	public static Throwable getCause( Throwable throwable ) {
		return ( throwable == null ) ? null : throwable.getCause();
	}

	
	/**
	 * Retrive the root message.
	 * 
	 * 
	 * @param throwable				The throwable.
	 * @return rootCauseMessage		The root cause message.
	 */
	public static String getRootMessage( Throwable throwable ) {
		return ( throwable.getCause() == null ) ? throwable.getMessage() : ExceptionUtils.getRootMessage( throwable.getCause() );		
	}
	
	
	/**
	 * Retrive the root cause.
	 * 
	 * 
	 * @param throwable		The throwable.
	 * @return rootCause	The root cause.
	 */
	public static Throwable getRootCause( Throwable throwable ) {
		return ( throwable.getCause() == null ) ? throwable : ExceptionUtils.getRootCause( throwable.getCause() );		
	}
}