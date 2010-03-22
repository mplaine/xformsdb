package fi.tkk.tml.xformsdb.handler;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpSession;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBStateManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:state> SET requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBStateSetHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger						= Logger.getLogger( XFormsDBStateSetHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBStateSetHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO					= null;
		Element xformsdbAttachmentElement					= null;
		Document xformsdbAttachmentElementContentsDocument	= null;
		ByteArrayOutputStream outputStream					= null;
		Serializer serializer								= null;
		String xformsdbState								= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO								= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the <xformsdb:attachment> element
			xformsdbAttachmentElement						= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );

			// Check the contents of the <xformsdb:attachment> element
			if ( xformsdbAttachmentElement.getChildElements().size() == 0 ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBSTATESETHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBSTATESETHANDLER_2 );
			}
			else {
				// Retrieve the contents of the <xformsdb:attachment> element
				xformsdbAttachmentElementContentsDocument	= new Document( new Element( xformsdbAttachmentElement.getChildElements().get( 0 ) ) );				
			}
			
			
			// Serialize the document
			outputStream									= new ByteArrayOutputStream();
			serializer										= new Serializer( outputStream, xformsDBConfigTO.getEncoding() );
			serializer.write( xformsdbAttachmentElementContentsDocument );			
			
			// Change the encoding
			xformsdbState									= outputStream.toString( xformsDBConfigTO.getEncoding() );
			
			// Store the web application's state into XFormsDB
			XFormsDBStateManager.setXFormsDBState( session, xformsdbState );
			logger.log( Level.DEBUG, "The web application's state has been successfully stored into XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBSTATESETHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBSTATESETHANDLER_1, ex  );
		}
		
		
		return xformsdbState;
	}	
}