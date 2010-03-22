package fi.tkk.tml.xformsdb.error;



/**
 * XML exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 09, 2009
 */
public class XMLException extends XFormsDBException {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PUBLIC CONSTRUCTORS
	public XMLException() {
		super();
	}
	public XMLException( String message ) {
		super( message );
	}
	public XMLException( String message, Throwable cause ) {
		super( message, cause );
	}
	public XMLException( Throwable cause ) {
		super( cause );
	}
}