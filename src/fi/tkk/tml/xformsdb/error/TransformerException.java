package fi.tkk.tml.xformsdb.error;



/**
 * Transformer exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class TransformerException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	

	// PRIVATE VARIABLES
	private int code;

	
	// PUBLIC CONSTRUCTORS
	public TransformerException() {
		super();
	}
	public TransformerException( String message ) {
		super( message );
	}
	public TransformerException( int code, String message ) {
		super( message );
		this.code	= code;
	}
	public TransformerException( Throwable cause ) {
		super( cause );
	}
	public TransformerException( int code, Throwable cause ) {
		super( cause );
		this.code	= code;
	}
	public TransformerException( String message, Throwable cause ) {
		super( message, cause );
	}
	public TransformerException( int code, String message, Throwable cause ) {
		super( message, cause );
		this.code	= code;
	}
	
	
	// PUBLIC METHODS
	public int getCode() {
		return this.code;
	}
}