package fi.tkk.tml.xformsdb.error;



/**
 * Initialization exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class InitializationException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PRIVATE VARIABLES
	private int code;
	
	
	// PUBLIC CONSTRUCTORS
	public InitializationException() {
		super();
	}
	public InitializationException( String message ) {
		super( message );
	}
	public InitializationException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public InitializationException( Throwable cause ) {
		super( cause );
	}
	public InitializationException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public InitializationException( String message, Throwable cause ) {
		super( message, cause );
	}
	public InitializationException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC VARIABLES
	public int getCode() {
		return this.code;
	}
}