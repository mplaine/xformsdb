package fi.tkk.tml.xformsdb.transformer.xformsdb;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.util.StringUtils;


/**
 * Transform the <xformsdb:submission> elements.
 * 
 * The transformed <xformsdb:submission> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsdb:submission> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 19, 2009
 */
public class XFormsDBSubmissionsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger				= Logger.getLogger( XFormsDBSubmissionsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBSubmissionsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document, Map<String, String> expressionTypeUpdateRequestInstances, Map<String, String> stateTypeSetRequestInstances, Map<String, String> fileTypeInsertRequestInstances, Map<String, String> fileTypeDeleteRequestInstances, Map<String, String> fileTypeUpdateRequestInstances, String actionURL, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsModelsElement				= document.getRootElement().getFirstChildElement( "models", "http://www.w3.org/2002/xforms" );
			Elements xformsModelElements			= xformsModelsElement.getChildElements( "model", "http://www.w3.org/2002/xforms" );
			Element xformsModelElement				= null;
			Element xformsdbSubmissionsElement		= document.getRootElement().getFirstChildElement( "submissions", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbSubmissionElements		= xformsdbSubmissionsElement.getChildElements( "submission", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbSubmissionElement		= null;
			Element xformsdbSubmissionElement2		= null;
			for ( int i = 0; i < xformsdbSubmissionElements.size(); i++ ) {
				xformsdbSubmissionElement			= xformsdbSubmissionElements.get( i );
				
				// Update expressiontype and statetype
				if ( "query".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "widgetquery".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "state".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "login".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "logout".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "user".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				else if ( "file".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
				}
				else if ( "cookie".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == true ) {
					if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "expressiontype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "statetype" ) );
					}
					if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
						xformsdbSubmissionElement.removeAttribute( xformsdbSubmissionElement.getAttribute( "filetype" ) );
					}
				}
				
				
				// Validate
				if (	xformsdbSubmissionElement.getAttribute( "id" ) == null ||
						"".equals( xformsdbSubmissionElement.getAttributeValue( "id" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_6, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_6 );
				}
				if ( 	xformsdbSubmissionElement.getAttribute( "requestinstance" ) == null ||
						"".equals( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_7, "Missing mandatory attribute 'requestinstance' from the <xformsdb:submission" + ( ( xformsdbSubmissionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element." );
				}
				if ( 	"query".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"widgetquery".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"state".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"login".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"logout".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"user".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"file".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false &&
						"cookie".equals( xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) ) == false ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_35, "Missing mandatory child element or illegal child element " + ( ( xformsdbSubmissionElement.getAttribute( "xformsdbrequesttype" ) != null ) ? "'" + xformsdbSubmissionElement.getAttributeValue( "xformsdbrequesttype" ) + "' " : "" ) + "in the <xformsdb:instance" + ( ( xformsdbSubmissionElement.getAttribute( "requestinstance" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) + "\">" : ">" ) + " element. Valid values are: <xformsdb:query>, <xformsdb:widgetquery>, <xformsdb:state>, <xformsdb:login>, <xformsdb:logout>, <xformsdb:user>, <xformsdb:file>, and <xformsdb:cookie>." );
				}
				if ( 	"all".equals( xformsdbSubmissionElement.getAttributeValue( "replace" ) ) == false &&
						"instance".equals( xformsdbSubmissionElement.getAttributeValue( "replace" ) ) == false &&
						"text".equals( xformsdbSubmissionElement.getAttributeValue( "replace" ) ) == false &&
						"none".equals( xformsdbSubmissionElement.getAttributeValue( "replace" ) ) == false ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_33, "Illegal replace attribute value '" + xformsdbSubmissionElement.getAttributeValue( "replace" ) + "' in the <xformsdb:submission" + ( ( xformsdbSubmissionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element. Valid values are: all, instance, text, and none." );
				}
				if (	xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
					if (	Constants.EXPRESSION_TYPE_ALL.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == false &&
							Constants.EXPRESSION_TYPE_SELECT.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == false &&
							Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == false &&
							Constants.EXPRESSION_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == false ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_8, "Illegal expressiontype attribute value '" + xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) + "' in the <xformsdb:submission" + ( ( xformsdbSubmissionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element. Valid values are: all, select, and update." );
					}
				}
				if (	xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
					if (	Constants.STATE_TYPE_GET.equals( xformsdbSubmissionElement.getAttributeValue( "statetype" ) ) == false &&
							Constants.STATE_TYPE_SET.equals( xformsdbSubmissionElement.getAttributeValue( "statetype" ) ) == false ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_9, "Illegal statetype attribute value '" + xformsdbSubmissionElement.getAttributeValue( "statetype" ) + "' in the <xformsdb:submission" + ( ( xformsdbSubmissionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element. Valid values are: get and set." );
					}
				}
				if (	xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
					if (	Constants.FILE_TYPE_SELECT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == false &&
							Constants.FILE_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == false &&
							Constants.FILE_TYPE_INSERT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == false &&
							Constants.FILE_TYPE_DELETE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == false ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_72, "Illegal filetype attribute value '" + xformsdbSubmissionElement.getAttributeValue( "filetype" ) + "' in the <xformsdb:submission" + ( ( xformsdbSubmissionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsdbSubmissionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element. Valid values are: select, update, insert, and delete." );
					}
				}
				
				
				// Update properties if needed
				if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
					// The expressiontype value is "update"
					if ( Constants.EXPRESSION_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == true ) {
						// Add to a map of request instances that need an attachment element
						expressionTypeUpdateRequestInstances.put( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ), xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) );
						
						// Add xformsinsertorigin
						if ( ( Constants.EXPRESSION_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) == null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == true ) ) {
							xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
						}
						else {
							xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
						}
						
						// Iterate over xformsdb:submission elements in order to find the elements that have the same requestinstance value and the expressiontype value is "select"
						boolean matchFound				= false;
						for ( int j = 0; j < xformsdbSubmissionElements.size(); j++ ) {
							xformsdbSubmissionElement2	= xformsdbSubmissionElements.get( j );

							// Match found
							if (	( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ).equals( xformsdbSubmissionElement2.getAttributeValue( "requestinstance" ) ) == true ) &&
									( ( Constants.EXPRESSION_TYPE_SELECT.equals( xformsdbSubmissionElement2.getAttributeValue( "expressiontype" ) ) == true ) || ( Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( xformsdbSubmissionElement2.getAttributeValue( "expressiontype" ) ) == true ) ) ) {
								// Update expressiontype
								xformsdbSubmissionElement2.addAttribute( new Attribute( "expressiontype", Constants.EXPRESSION_TYPE_SELECT4UPDATE ) );									
								// Add action
								xformsdbSubmissionElement2.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( actionURL + "?" + Constants.REQUEST_PARAMETER_EXPRESSION_TYPE + "=" + Constants.EXPRESSION_TYPE_SELECT4UPDATE + "&" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement2.getAttributeValue( "replacetype" ), false, response ) ) );
								matchFound				= true;
							}
						}
						
						if ( matchFound == false ) {
							throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_88, "Missing mandatory <xformsdb:submission requestinstance=\"" + xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) + "\" expressiontype=\"" + Constants.EXPRESSION_TYPE_SELECT + "\"> element." );
						}
					}
				}
				
				
				// Update properties if needed
				if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
					// The statetype value is "set"
					if ( Constants.STATE_TYPE_SET.equals( xformsdbSubmissionElement.getAttributeValue( "statetype" ) ) == true ) {
						// Add to a map of request instances that need an attachment element
						stateTypeSetRequestInstances.put( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ), xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) );
					}
					
					// Add xformsinsertorigin
					if ( ( Constants.STATE_TYPE_SET.equals( xformsdbSubmissionElement.getAttributeValue( "statetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) == null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == true ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
					}
					else if ( ( Constants.STATE_TYPE_SET.equals( xformsdbSubmissionElement.getAttributeValue( "statetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) != null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == false ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
					}
				}

				
				// Update properties if needed
				if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
					// The filetype value is "insert"
					if ( Constants.FILE_TYPE_INSERT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) {
						// Add to a map of request instances that need an attachment element
						fileTypeInsertRequestInstances.put( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ), xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) );
					}
					// The filetype value is "delete"
					else if ( Constants.FILE_TYPE_DELETE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) {
						// Add to a map of request instances that need an attachment element
						fileTypeDeleteRequestInstances.put( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ), xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) );
					}
					// The filetype value is "update"
					else if ( Constants.FILE_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) {
						// Add to a map of request instances that need an attachment element
						fileTypeUpdateRequestInstances.put( xformsdbSubmissionElement.getAttributeValue( "requestinstance" ), xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) );
					}
					
					// Add xformsinsertorigin and uploadref
					if ( ( Constants.FILE_TYPE_INSERT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) == null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == true ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
						xformsdbSubmissionElement.addAttribute( new Attribute( "uploadref", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
					}
					else if ( ( Constants.FILE_TYPE_DELETE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) == null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == true ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
					}
					else if ( ( Constants.FILE_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) == null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == true ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
						xformsdbSubmissionElement.addAttribute( new Attribute( "uploadref", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
					}
					else if ( ( Constants.FILE_TYPE_INSERT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) != null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == false ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
						xformsdbSubmissionElement.addAttribute( new Attribute( "uploadref", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
					}
					else if ( ( Constants.FILE_TYPE_DELETE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) != null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == false ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
					}
					else if ( ( Constants.FILE_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) && ( xformsdbSubmissionElement.getAttribute( "attachmentinstance" ) != null || "".equals( xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) ) == false ) ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertorigin", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
						xformsdbSubmissionElement.addAttribute( new Attribute( "uploadref", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "attachmentinstance" ) + "' )" ) );
					}
				}

				
				// Add ref and action
				xformsdbSubmissionElement.addAttribute( new Attribute( "ref", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "requestinstance" ) + "' )" ) );
				if ( xformsdbSubmissionElement.getAttribute( "expressiontype" ) != null ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( actionURL + "?" + Constants.REQUEST_PARAMETER_EXPRESSION_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "expressiontype" ) + "&" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "replacetype" ), false, response ) ) );
				}
				else if ( xformsdbSubmissionElement.getAttribute( "statetype" ) != null ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( actionURL + "?" + Constants.REQUEST_PARAMETER_STATE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "statetype" ) + "&" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "replacetype" ), false, response ) ) );
				}
				else if ( xformsdbSubmissionElement.getAttribute( "filetype" ) != null ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( actionURL + "?" + Constants.REQUEST_PARAMETER_FILE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "filetype" ) + "&" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "replacetype" ), false, response ) ) );
					// BEGIN: Hack: Create a dummy submission in order to receive information about the file to be uploaded from Orbeon Forms
					if ( Constants.FILE_TYPE_INSERT.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true || Constants.FILE_TYPE_UPDATE.equals( xformsdbSubmissionElement.getAttributeValue( "filetype" ) ) == true ) {
						xformsdbSubmissionElement.addAttribute( new Attribute( "uploadaction", StringUtils.toSafeASCIIString( actionURL + "upload" + "?" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "replacetype" ), false, response ) ) );
					}
					// END: Hack: Create a dummy submission in order to receive information about the file to be uploaded from Orbeon Forms
				}
				else {
					xformsdbSubmissionElement.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( actionURL + "?" + Constants.REQUEST_PARAMETER_REPLACE_TYPE + "=" + xformsdbSubmissionElement.getAttributeValue( "replacetype" ), false, response ) ) );
				}
				
				// Add xformsinsertcontext, replace, instance, and encoding
				if ( "instance".equals( xformsdbSubmissionElement.getAttributeValue( "replacetype" ) ) == true ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "xformsinsertcontext", "instance( '" + xformsdbSubmissionElement.getAttributeValue( "instance" ) + "' )" ) );
				}
				if ( "none".equals( xformsdbSubmissionElement.getAttributeValue( "replacetype" ) ) == true ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "replace", "instance" ) );
				}
				if ( "instance".equals( xformsdbSubmissionElement.getAttributeValue( "replace" ) ) == true ) {
					xformsdbSubmissionElement.addAttribute( new Attribute( "instance", xformsdbSubmissionElement.getAttributeValue( "proxyinstance" ) ) );
				}
				xformsdbSubmissionElement.addAttribute( new Attribute( "encoding", encoding ) );
				xformsdbSubmissionElement.addAttribute( new Attribute( "mediatype", Constants.MEDIA_TYPE_APPLICATION_XML ) );
				xformsdbSubmissionElement.addAttribute( new Attribute( "method", Constants.METHOD_POST ) );
			}
			
			
			logger.log( Level.DEBUG, "The <xformsdb:submission> elements have been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_3, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_3, ex );
		}
	}	
}