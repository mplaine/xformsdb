package fi.tkk.tml.xformsdb.error;



/**
 * Handler (servlet) exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class HandlerException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;

	
	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public HandlerException() {
		super();
	}
	public HandlerException( String message ) {
		super( message );
	}
	public HandlerException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public HandlerException( Throwable cause ) {
		super( cause );
	}
	public HandlerException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public HandlerException( String message, Throwable cause ) {
		super( message, cause );
	}
	public HandlerException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}