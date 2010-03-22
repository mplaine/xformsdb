package fi.tkk.tml.xformsdb.error;



/**
 * Authentication exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class AuthenticationException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;

	
	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public AuthenticationException() {
		super();
	}
	public AuthenticationException( String message ) {
		super( message );
	}
	public AuthenticationException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public AuthenticationException( Throwable cause ) {
		super( cause );
	}
	public AuthenticationException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public AuthenticationException( String message, Throwable cause ) {
		super( message, cause );
	}
	public AuthenticationException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}