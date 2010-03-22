package fi.tkk.tml.xformsdb.transformer.xformsdb;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;


/**
 * Transform the <xformsdb:login> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 31, 2008
 */
public class XFormsDBLoginTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBLoginTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBLoginTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document, Element activeXFormsDBLoginElement ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Create a temporary copy of the <xformsdb:login> element (secured/sent) and its children
			Element sentXFormsDBLoginElement						= new Element( document.getRootElement() );

			// Create a copy of the <xformsdb:login> element (unsecured/stored)
			Element newXFormsDBLoginElement							= new Element( activeXFormsDBLoginElement );

			// Copy the XML Namespace declarations for the <xformsdb:login> element
			String sentXFormsDBLoginElementNamespacePrefix			= null;
			String sentXFormsDBLoginElementNamespaceURI				= null;
			for ( int i = 0; i < sentXFormsDBLoginElement.getNamespaceDeclarationCount(); i++ ) {
				sentXFormsDBLoginElementNamespacePrefix				= sentXFormsDBLoginElement.getNamespacePrefix( i );
				sentXFormsDBLoginElementNamespaceURI				= sentXFormsDBLoginElement.getNamespaceURI( sentXFormsDBLoginElementNamespacePrefix );
				newXFormsDBLoginElement.addNamespaceDeclaration( sentXFormsDBLoginElementNamespacePrefix, sentXFormsDBLoginElementNamespaceURI );
			}

			// Copy the values for the <xformsdb:var> elements
			Elements newXFormsDBLoginElementXFormsDBVarElements		= newXFormsDBLoginElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Elements sentXFormsDBLoginElementXFormsDBVarElements	= sentXFormsDBLoginElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBLoginElementXFormsDBVarElement		= null;
			Element sentXFormsDBLoginElementXFormsDBVarElement		= null;
			for ( int i = 0; i < newXFormsDBLoginElementXFormsDBVarElements.size(); i++ ) {
				newXFormsDBLoginElementXFormsDBVarElement			= newXFormsDBLoginElementXFormsDBVarElements.get( i );
				for ( int j = 0; j < sentXFormsDBLoginElementXFormsDBVarElements.size(); j++ ) {
					sentXFormsDBLoginElementXFormsDBVarElement		= sentXFormsDBLoginElementXFormsDBVarElements.get( j );
					if ( newXFormsDBLoginElementXFormsDBVarElement.getAttributeValue( "name" ).equals( sentXFormsDBLoginElementXFormsDBVarElement.getAttributeValue( "name" ) ) == true ) {
						newXFormsDBLoginElementXFormsDBVarElement.getParent().replaceChild( newXFormsDBLoginElementXFormsDBVarElement, new Element( sentXFormsDBLoginElementXFormsDBVarElement ) );
						break;
					}
				}
			}
			
			// Update the <xformsdb:login> element (secured/sent)
			document.setRootElement( new Element( newXFormsDBLoginElement ) );						
			logger.log( Level.DEBUG, "The <xformsdb:login> element has been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_65, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_65, ex );
		}
	}	
}