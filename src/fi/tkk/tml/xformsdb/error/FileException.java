package fi.tkk.tml.xformsdb.error;



/**
 * File exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class FileException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PRIVATE VARIABLES
	private int code;
	
	
	// PUBLIC CONSTRUCTORS
	public FileException() {
		super();
	}
	public FileException( String message ) {
		super( message );
	}
	public FileException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public FileException( Throwable cause ) {
		super( cause );
	}
	public FileException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public FileException( String message, Throwable cause ) {
		super( message, cause );
	}
	public FileException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}