package fi.tkk.tml.xformsdb.error;


/**
 * Error (servlet) exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class ErrorException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public ErrorException() {
		super();
	}
	public ErrorException( String message ) {
		super( message );
	}
	public ErrorException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public ErrorException( Throwable cause ) {
		super( cause );
	}
	public ErrorException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public ErrorException( String message, Throwable cause ) {
		super( message, cause );
	}
	public ErrorException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}

	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}