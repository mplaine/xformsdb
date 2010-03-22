package fi.tkk.tml.xformsdb.error;


/**
 * Abstract class that all XFormsDB exceptions must support.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public abstract class XFormsDBException extends Exception {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public XFormsDBException() {
		super();
	}
	public XFormsDBException( String message ) {
		super( message );
	}
	public XFormsDBException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public XFormsDBException( Throwable cause ) {
		super( cause );
	}
	public XFormsDBException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public XFormsDBException( String message, Throwable cause ) {
		super( message, cause );
	}
	public XFormsDBException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}

	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}