package fi.tkk.tml.xformsdb.error;



/**
 * Manager exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class ManagerException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	

	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public ManagerException() {
		super();
	}
	public ManagerException( String message ) {
		super( message );
	}
	public ManagerException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public ManagerException( Throwable cause ) {
		super( cause );
	}
	public ManagerException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public ManagerException( String message, Throwable cause ) {
		super( message, cause );
	}
	public ManagerException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}