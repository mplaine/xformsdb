package fi.tkk.tml.xformsdb.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.eaio.uuid.UUID;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:file> INSERT requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 23, 2009
 */
public class XFormsDBFileInsertHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsDBFileInsertHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBFileInsertHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS	
	private XFormsDBDAO getXFormsDBDAO( XFormsDBServlet xformsdbServlet, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, DAOFactory> daoFactories	= null;
		XFormsDBDAO xformsdbDAO					= null;
		
		
		try {
			// Retrieve the DAO Factories from the servlet context
			daoFactories						= xformsdbServlet.getDAOFactories();
			
			// Retrieve the files metadata DAO Factory implementation
			DAOFactory daoFactory				= daoFactories.get( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID );
			
			if ( daoFactory == null) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_92, ErrorConstants.ERROR_MESSAGE_DAO_92 );
			}
			
			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the files metadata DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	private List<String> writeFiles( XFormsDBConfigTO xformsdbConfigTO, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String insertedFileIDsStr						= null;
		List<String> newlyInsertedFilePaths				= null;
		int filesLength									= 0;
		boolean deleteWrittenFiles						= false;
		Calendar calendar								= null;
		String filePath									= null;
		String fileName									= null;
		String fileId									= null;
		String fileCreated								= null;
		String inputFilePath							= null;
		URL inputURL									= null;
		InputStream inputStream							= null;
		BufferedInputStream bufferedInputStream			= null;
		String outputFilePath							= null;
		File outputFile									= null;
		FileOutputStream fileOutputStream				= null;
		BufferedOutputStream bufferedOutputStream		= null;
		int byteOfData									= 0;
		File deleteFile									= null;
		String deleteFilePath							= null;
		
		
		try {
			// Retrieve the <xformsdb:attachment> element
			Element xformsdbAttachmentElement			= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			// Retrieve the <xformsdb:insert> element
			Element xformsdbInsertElement				= xformsdbAttachmentElement.getFirstChildElement( "insert", Constants.NAMESPACE_URI_XFORMSDB );
			
			// Validate the <xformsdb:insert> element
			if ( xformsdbInsertElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_4, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_4 );
			}
			else {
				// Validate child elements of the <xformsdb:insert> element
				Elements xformsdbFileElements			= xformsdbInsertElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbFileElements.size() != xformsdbInsertElement.getChildElements().size() ) {
					throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_5, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_5 );					
				}
				else if ( xformsdbFileElements.size() == xformsdbInsertElement.getChildElements().size() && xformsdbFileElements.size() > 0 ) {
					Element xformsdbFileElement			= null;
					Attribute displayNameAttribute		= null;
					Attribute fileNameAttribute			= null;
					Attribute mediaTypeAttribute		= null;
					Attribute fileSizeAttribute			= null;
					Attribute rolesAttribute			= null;
					Attribute creatorAttribute			= null;
					Attribute commentAttribute			= null;
					for ( int i = 0; i < xformsdbFileElements.size(); i++ ) {
						xformsdbFileElement				= xformsdbFileElements.get( i );
						displayNameAttribute			= xformsdbFileElement.getAttribute( "displayname" );
						fileNameAttribute				= xformsdbFileElement.getAttribute( "filename" );
						mediaTypeAttribute				= xformsdbFileElement.getAttribute( "mediatype" );
						fileSizeAttribute				= xformsdbFileElement.getAttribute( "filesize" );
						rolesAttribute					= xformsdbFileElement.getAttribute( "roles" );
						creatorAttribute				= xformsdbFileElement.getAttribute( "creator" );
						commentAttribute				= xformsdbFileElement.getAttribute( "comment" );
						if ( displayNameAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_6, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_6 );							
						}
						if ( fileNameAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_7, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_7 );							
						}
						if ( mediaTypeAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_8, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_8 );							
						}
						if ( fileSizeAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_9, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_9 );							
						}
						if ( rolesAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_10, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_10 );							
						}
						if ( creatorAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_12, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_12 );							
						}
						if ( commentAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_13, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_13 );							
						}
					}
				}
			}

			String insertedAttributeValue				= xformsdbInsertElement.getAttributeValue( "inserted" );
			if ( insertedAttributeValue == null ) {
				insertedFileIDsStr						= "";
			}
			else {
				insertedFileIDsStr						= insertedAttributeValue.trim();
			}
			StringTokenizer insertedFileIDs				= new StringTokenizer( insertedFileIDsStr, " " );

			// Iterate over files to be inserted
			Elements xformsdbFileElements				= xformsdbInsertElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
			filesLength									= xformsdbFileElements.size();
			newlyInsertedFilePaths						= new ArrayList<String>();
			Element xformsdbFileElement					= null;
			for ( int i = 0; i < filesLength; i++ ) {
				xformsdbFileElement						= xformsdbFileElements.get( i );
				
				// Retrieve file information
				filePath								= xformsdbFileElement.getValue();
				fileName								= xformsdbFileElement.getAttributeValue( "filename" );
				fileId									= xformsdbFileElement.getAttributeValue( "id" );

				// Add the ID attribute if needed
				if ( fileId == null ) {
					fileId								= new UUID().toString();
					xformsdbFileElement.addAttribute( new Attribute( "id", fileId ) );
				}
				
				// Check whether or not the file has been already inserted
				String insertedFileID					= null;
				boolean insertFile						= true;
				while ( insertedFileIDs.hasMoreTokens() ) {
					insertedFileID						= insertedFileIDs.nextToken();
					if ( fileId.equals( insertedFileID ) == true ) {
						insertFile						= false;
						break;
					}
				}
				
				// Insert the file because it has not been inserted to XFormsDB yet
				if ( insertFile == true ) {
					try {
						// Input file
						inputFilePath					= filePath;
						inputURL						= new URL( inputFilePath );
						inputStream						= inputURL.openStream();
						bufferedInputStream				= new BufferedInputStream( inputStream );
						
						// Output file
						outputFilePath					= xformsdbConfigTO.getFilesFolder() + File.separator + fileId + "_" + fileName;
						outputFile						= new File( outputFilePath );
						fileOutputStream				= new FileOutputStream( outputFile );
						bufferedOutputStream			= new BufferedOutputStream( fileOutputStream );
	
						// Store the file's output path
						newlyInsertedFilePaths.add( outputFilePath );
	
						// Add the created attribute
						calendar						= Calendar.getInstance();
						//calendar.setTimeInMillis( outputFile.lastModified() );
						fileCreated						= DateFormatUtils.format( calendar.getTime(), Constants.FILE_CREATED_FORMAT );
						xformsdbFileElement.addAttribute( new Attribute( "created", fileCreated ) );

						// Add the lastmodifier attribute
						xformsdbFileElement.addAttribute( new Attribute( "lastmodifier", "" ) );
						// Add the lastmodified attribute
						xformsdbFileElement.addAttribute( new Attribute( "lastmodified", "" ) );

				        // Write file to XFormsDB
				        while ( ( byteOfData = bufferedInputStream.read() ) != -1 ) {
				        	bufferedOutputStream.write( byteOfData );
				        }
						logger.log( Level.DEBUG, "The file has been successfully inserted to XFormsDB." );
					} catch ( Exception ex ) {
						deleteWrittenFiles				= true;
					} finally {
						if ( bufferedInputStream != null ) {
							try {
								bufferedInputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( inputStream != null ) {
							try {
								inputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( bufferedOutputStream != null ) {
							try {
								bufferedOutputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( fileOutputStream != null ) {
							try {
								fileOutputStream.close();
							} catch ( IOException ioex ) {
							}
						}
					}
				}

				// Something went wrong while writing the files to XFormsDB. Delete newly inserted files.
				if ( deleteWrittenFiles == true ) {
					for ( int j = 0; j < newlyInsertedFilePaths.size(); j++ ) {
						deleteFilePath					= newlyInsertedFilePaths.get( j );
						if ( deleteFilePath != null ) {
							deleteFile					= new File( deleteFilePath );
							deleteFile.delete();
						}
					}
					throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_11, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_11  );
				}
			}
			
			String namespacePrefixXFormsDBWithColon		= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" );
			// Create the <xformsdb:var> element (xformsdb:insert)
			Element xformsdbVar1						= new Element( namespacePrefixXFormsDBWithColon + "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVar1.addAttribute( new Attribute( "name", "xformsdbinsert" ) );
			xformsdbVar1.appendChild( new Element( xformsdbInsertElement ) );
			document.getRootElement().appendChild( xformsdbVar1 );

			// Create the <xformsdb:var> element (inserted file IDs)
			Element xformsdbVar2						= new Element( namespacePrefixXFormsDBWithColon + "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVar2.addAttribute( new Attribute( "name", "insertedfileids" ) );
			xformsdbVar2.appendChild( insertedFileIDsStr.toString() );
			document.getRootElement().appendChild( xformsdbVar2 );

			
			logger.log( Level.DEBUG, "The files have been successfully written to XFormsDB." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_3, ex  );
		}
		
		
		return newlyInsertedFilePaths;
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, String actionURL, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbInsert				= null;
		XFormsDBConfigTO xformsdbConfigTO	= null;
		XFormsDBDAO xformsdbDAO				= null;
		boolean deleteWrittenFiles			= true;
		List<String> newlyInsertedFilePaths	= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsdbConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );
			
			// Write the files to XFormsDB and update the XML document
			newlyInsertedFilePaths			= this.writeFiles( xformsdbConfigTO, document );
			
			// Insert files and retrieve the XML document
			xformsdbInsert					= xformsdbDAO.insertFiles( document, actionURL, xformsdbConfigTO.getEncoding() );
			deleteWrittenFiles				= false;
			logger.log( Level.DEBUG, "The INSERT action of the XFormsDB file request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEINSERTHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_2, ex  );
		} finally {
			if ( deleteWrittenFiles == true && newlyInsertedFilePaths != null ) {
				File deleteFile				= null;
				String deleteFilePath		= null;
				for ( int i = 0; i < newlyInsertedFilePaths.size(); i++ ) {
					deleteFilePath			= newlyInsertedFilePaths.get( i );
					if ( deleteFilePath != null ) {
						deleteFile			= new File( deleteFilePath );
						deleteFile.delete();
					}
				}
			}
		}
		
		
		return xformsdbInsert;
	}
}