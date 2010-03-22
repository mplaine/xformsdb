package fi.tkk.tml.xformsdb.error;



/**
 * Merger exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class MergerException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	

	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public MergerException() {
		super();
	}
	public MergerException( String message ) {
		super( message );
	}
	public MergerException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public MergerException( Throwable cause ) {
		super( cause );
	}
	public MergerException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public MergerException( String message, Throwable cause ) {
		super( message, cause );
	}
	public MergerException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}