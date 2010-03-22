package fi.tkk.tml.xformsdb.error;



/**
 * Data access object (DAO) exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class DAOException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public DAOException() {
		super();
	}
	public DAOException( String message ) {
		super( message );
	}
	public DAOException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public DAOException( Throwable cause ) {
		super( cause );
	}
	public DAOException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public DAOException( String message, Throwable cause ) {
		super( message, cause );
	}
	public DAOException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}