package fi.tkk.tml.xformsdb.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.FileRestore;
import fi.tkk.tml.xformsdb.util.FileBackupFilenameFilter;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:file> UPDATE requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBFileUpdateHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsDBFileUpdateHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBFileUpdateHandler() {
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
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_110, ErrorConstants.ERROR_MESSAGE_DAO_110 );
			}
			
			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the files metadata DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	private List<FileRestore> writeFiles( XFormsDBConfigTO xformsdbConfigTO, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		List<FileRestore> fileRestores						= null;
		FileRestore fileRestore								= null;
		FileBackupFilenameFilter backupFilenameFilter		= null;
		String[] backupFilePaths							= null;
		String backupInputFilePath							= null;
		File backupInputFile								= null;
		InputStream backupInputStream						= null;
		InputStream backupBufferedInputStream				= null;
		ByteArrayOutputStream backupByteArrayOutputStream	= null;
		OutputStream backupBufferedOutputStream				= null;
		boolean restoreFiles								= false;
		int filesLength										= 0;
		String filePath										= null;
		String fileName										= null;
		String fileId										= null;
		String fileLastModified								= null;
		String filesFolderFilePath							= null;
		File filesFolderFile								= null;
		String inputFilePath								= null;
		URL inputURL										= null;
		InputStream inputStream								= null;
		InputStream inputBufferedInputStream				= null;
		String outputFilePath								= null;
		File outputFile										= null;
		OutputStream outputFileOutputStream					= null;
		OutputStream outputBufferedOutputStream				= null;
		Calendar calendar									= null;
		int byteOfData										= 0;		
		
		try {
			// Retrieve the <xformsdb:attachment> element
			Element xformsdbAttachmentElement				= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			// Retrieve the <xformsdb:update> element
			Element xformsdbUpdateElement					= xformsdbAttachmentElement.getFirstChildElement( "update", Constants.NAMESPACE_URI_XFORMSDB );
			
			// Validate the <xformsdb:update> element
			if ( xformsdbUpdateElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_3 );
			}
			else {
				// Validate child elements of the <xformsdb:update> element
				Elements xformsdbFileElements				= xformsdbUpdateElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbFileElements.size() != xformsdbUpdateElement.getChildElements().size() ) {
					throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_4, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_4 );					
				}
				else if ( xformsdbFileElements.size() == xformsdbUpdateElement.getChildElements().size() && xformsdbFileElements.size() > 0 ) {
					Element xformsdbFileElement				= null;
					Attribute idAttribute					= null;
					Attribute downloadAttribute				= null;
					Attribute lastModifiedAttribute			= null;
					Attribute displayNameAttribute			= null;
					Attribute fileNameAttribute				= null;
					Attribute mediaTypeAttribute			= null;
					Attribute fileSizeAttribute				= null;
					Attribute rolesAttribute				= null;
					Attribute creatorAttribute				= null;
					Attribute createdAttribute				= null;
					Attribute commentAttribute				= null;
					Attribute lastModifierAttribute			= null;
					for ( int i = 0; i < xformsdbFileElements.size(); i++ ) {
						xformsdbFileElement					= xformsdbFileElements.get( i );
						idAttribute							= xformsdbFileElement.getAttribute( "id" );
						downloadAttribute					= xformsdbFileElement.getAttribute( "download" );
						lastModifiedAttribute				= xformsdbFileElement.getAttribute( "lastmodified" );
						displayNameAttribute				= xformsdbFileElement.getAttribute( "displayname" );
						fileNameAttribute					= xformsdbFileElement.getAttribute( "filename" );
						mediaTypeAttribute					= xformsdbFileElement.getAttribute( "mediatype" );
						fileSizeAttribute					= xformsdbFileElement.getAttribute( "filesize" );
						rolesAttribute						= xformsdbFileElement.getAttribute( "roles" );
						creatorAttribute					= xformsdbFileElement.getAttribute( "creator" );
						createdAttribute					= xformsdbFileElement.getAttribute( "created" );
						commentAttribute					= xformsdbFileElement.getAttribute( "comment" );
						lastModifierAttribute				= xformsdbFileElement.getAttribute( "lastmodifier" );
						if ( idAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_5, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_5 );							
						}
						if ( downloadAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_7, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_7 );							
						}
						if ( lastModifiedAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_8, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_8 );							
						}
						if ( displayNameAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_9, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_9 );							
						}
						if ( fileNameAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_10, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_10 );							
						}
						if ( mediaTypeAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_11, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_11 );							
						}
						if ( fileSizeAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_12, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_12 );							
						}
						if ( rolesAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_13, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_13 );							
						}
						if ( creatorAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_16, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_16 );							
						}
						if ( createdAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_17, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_17 );							
						}
						if ( commentAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_18, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_18 );							
						}
						if ( lastModifierAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_19, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_19 );							
						}
					}
				}
			}
			
			// Iterate over files to be updated
			Elements xformsdbFileElements					= xformsdbUpdateElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
			filesLength										= xformsdbFileElements.size();
			fileRestores									= new ArrayList<FileRestore>();
			backupFilenameFilter							= new FileBackupFilenameFilter();
			Element xformsdbFileElement						= null;
			for ( int i = 0; i < filesLength; i++ ) {
				xformsdbFileElement							= xformsdbFileElements.get( i );
				
				// Retrieve file information
				filePath									= xformsdbFileElement.getValue();
				fileName									= xformsdbFileElement.getAttributeValue( "filename" );
				fileId										= xformsdbFileElement.getAttributeValue( "id" );

				// Update the lastmodified attribute
				calendar									= Calendar.getInstance();
				fileLastModified							= DateFormatUtils.format( calendar.getTime(), Constants.FILE_LAST_MODIFIED_FORMAT );
				xformsdbFileElement.addAttribute( new Attribute( "lastmodified", fileLastModified ) );

				// Backup the previous version, delete the previous version, and finally update/overwrite the updated version of the file
				if ( filePath != null && "".equals( filePath ) == false ) {
					// Everything still okay
					if ( restoreFiles == false ) {
						try {
							// Retrieve the files folder file
							filesFolderFilePath					= xformsdbConfigTO.getFilesFolder();
							filesFolderFile						= new File( filesFolderFilePath );
	
							// Retrieve the filename of the (current) file to be updated/overwritten
							backupFilenameFilter.setFilesFolder( filesFolderFile );
							backupFilenameFilter.setFilenamePrefix( fileId );
							backupFilePaths						= filesFolderFile.list( backupFilenameFilter );
							
							// Test whether or not the file to be updated still exists
							if ( backupFilePaths.length == 0 ) {
								throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_14, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_14 );
							}
							
							// Backup the previous version of the file
							// Backup input
							backupInputFilePath					= xformsdbConfigTO.getFilesFolder() + File.separator + backupFilePaths[ 0 ];
							backupInputFile						= new File( backupInputFilePath );
							backupInputStream					= new FileInputStream( backupInputFile );
							backupBufferedInputStream			= new BufferedInputStream( backupInputStream );
													
							// Backup output
							backupByteArrayOutputStream			= new ByteArrayOutputStream();
							backupBufferedOutputStream			= new BufferedOutputStream( backupByteArrayOutputStream );
			
					        // Write backup
							byteOfData							= 0;
					        while ( ( byteOfData = backupBufferedInputStream.read() ) != -1 ) {
					        	backupBufferedOutputStream.write( byteOfData );
					        }
					        
					        // Add the backup to the list of file backups
					        fileRestore							= new FileRestore( backupInputFilePath, backupByteArrayOutputStream, xformsdbConfigTO.getFilesFolder() + File.separator + fileId + "_" + fileName );
					        fileRestores.add( fileRestore );
							logger.log( Level.DEBUG, "The file has been successfully backed up." );
						} catch ( Exception ex ) {
							restoreFiles						= true;
						} finally {
							if ( backupBufferedInputStream != null ) {
								try {
									backupBufferedInputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( backupInputStream != null ) {
								try {
									backupInputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( backupBufferedOutputStream != null ) {
								try {
									backupBufferedOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( backupByteArrayOutputStream != null ) {
								try {
									backupByteArrayOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
						}
					}

					
					// Everything still okay
					if ( restoreFiles == false ) {
						try {
					        // Delete the previous version of the file
							backupInputFile.delete();
							logger.log( Level.DEBUG, "The backed up file has been successfully deleted." );
						} catch ( Exception ex ) {
							restoreFiles						= true;
						}
					}
					
				        
					// Everything still okay
					if ( restoreFiles == false ) {
						try {
					        // Update/Overwrite the updated version of the file
							// Update input
							inputFilePath						= filePath;
							inputURL							= new URL( inputFilePath );
							inputStream							= inputURL.openStream();
							inputBufferedInputStream			= new BufferedInputStream( inputStream );
							
							// Update output
							outputFilePath						= xformsdbConfigTO.getFilesFolder() + File.separator + fileId + "_" + fileName;
							outputFile							= new File( outputFilePath );
							outputFileOutputStream				= new FileOutputStream( outputFile );
							outputBufferedOutputStream			= new BufferedOutputStream( outputFileOutputStream );
	
					        // Write update
							byteOfData							= 0;
					        while ( ( byteOfData = inputBufferedInputStream.read() ) != -1 ) {
					        	outputBufferedOutputStream.write( byteOfData );
					        }
							logger.log( Level.DEBUG, "The file has been successfully updated to XFormsDB." );
						} catch ( Exception ex ) {
							restoreFiles						= true;
						} finally {
							if ( inputBufferedInputStream != null ) {
								try {
									inputBufferedInputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( inputStream != null ) {
								try {
									inputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( outputBufferedOutputStream != null ) {
								try {
									outputBufferedOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( outputFileOutputStream != null ) {
								try {
									outputFileOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
						}
					}
				}
				
				// Something went wrong while updating/overwriting the files to XFormsDB. Restore files.
				if ( restoreFiles == true ) {
					FileRestore restoreFileRestore				= null;
					String restoreNewFilePath					= null;
					File restoreNewFile							= null;
					InputStream restoreByteArrayInputStream		= null;
					InputStream restoreBufferedInputStream		= null;
					String restoreOutputFilePath				= null;
					File restoreOutputFile						= null;
					OutputStream restoreFileOutputStream		= null;
					OutputStream restoreBufferedOutputStream	= null;
					
					
					for ( int j = 0; j < fileRestores.size(); j++ ) {
						restoreFileRestore						= fileRestores.get( j );
						
						try {
							// Delete newly created file
							if ( restoreFileRestore.getNewFilePath() != null && "".equals( restoreFileRestore.getNewFilePath() ) == false ) {
								restoreNewFilePath				= restoreFileRestore.getNewFilePath();
								restoreNewFile					= new File( restoreNewFilePath );
								restoreNewFile.delete();
							}

							// Restore backup file
							// Restore input
							restoreByteArrayInputStream			= new ByteArrayInputStream( restoreFileRestore.getBackupFileByteArrayOutputStream().toByteArray() );
							restoreBufferedInputStream			= new BufferedInputStream( restoreByteArrayInputStream );
							
							// Restore output
							restoreOutputFilePath				= restoreFileRestore.getBackupFilePath();
							restoreOutputFile					= new File( restoreOutputFilePath );
							restoreFileOutputStream				= new FileOutputStream( restoreOutputFile );
							restoreBufferedOutputStream			= new BufferedOutputStream( restoreFileOutputStream );
		
					        // Write file to be restored to XFormsDB
							byteOfData							= 0;
					        while ( ( byteOfData = restoreBufferedInputStream.read() ) != -1 ) {
					        	restoreBufferedOutputStream.write( byteOfData );
					        }
							logger.log( Level.DEBUG, "The backed up file has been successfully restored." );
						} catch ( Exception ex ) {
							// Do nothing
							logger.log( Level.DEBUG, "Failed to restore files." );
						} finally {
							if ( restoreBufferedInputStream != null ) {
								try {
									restoreBufferedInputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( restoreByteArrayInputStream != null ) {
								try {
									restoreByteArrayInputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( restoreBufferedOutputStream != null ) {
								try {
									restoreBufferedOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
							if ( restoreFileOutputStream != null ) {
								try {
									restoreFileOutputStream.close();
								} catch ( IOException ioex ) {
								}
							}
						}
					}
					throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_15, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_15  );
				}
			}
			
			
			String namespacePrefixXFormsDBWithColon		= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" );
			// Create the <xformsdb:var> element (xformsdb:update)
			Element xformsdbVar1						= new Element( namespacePrefixXFormsDBWithColon + "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVar1.addAttribute( new Attribute( "name", "xformsdbupdate" ) );
			xformsdbVar1.appendChild( new Element( xformsdbUpdateElement ) );
			document.getRootElement().appendChild( xformsdbVar1 );

			
			logger.log( Level.DEBUG, "The files have been successfully updated/overwritten to XFormsDB" );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_6, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_6, ex  );
		}
		
		
		return fileRestores;
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, String actionURL, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUpdate				= null;
		XFormsDBConfigTO xformsdbConfigTO	= null;
		XFormsDBDAO xformsdbDAO				= null;
		List<FileRestore> fileRestores		= null;
		boolean restoreFiles				= true;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsdbConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );
			
			// Update/write the files to XFormsDB and update the XML document
			fileRestores					= this.writeFiles( xformsdbConfigTO, document );
			
			// Delete files and retrieve the XML document
			xformsdbUpdate					= xformsdbDAO.updateFiles( document, xformsdbConfigTO.getEncoding() );
			restoreFiles					= false;
			logger.log( Level.DEBUG, "The UPDATE action of the XFormsDB file request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_2, ex  );
		} finally {
			if ( restoreFiles == true && fileRestores != null ) {
				FileRestore restoreFileRestore				= null;
				String restoreNewFilePath					= null;
				File restoreNewFile							= null;
				InputStream restoreByteArrayInputStream		= null;
				InputStream restoreBufferedInputStream		= null;
				String restoreOutputFilePath				= null;
				File restoreOutputFile						= null;
				OutputStream restoreFileOutputStream		= null;
				OutputStream restoreBufferedOutputStream	= null;
				
				
				for ( int j = 0; j < fileRestores.size(); j++ ) {
					restoreFileRestore						= fileRestores.get( j );
					
					try {
						// Delete newly created file
						if ( restoreFileRestore.getNewFilePath() != null && "".equals( restoreFileRestore.getNewFilePath() ) == false ) {
							restoreNewFilePath				= restoreFileRestore.getNewFilePath();
							restoreNewFile					= new File( restoreNewFilePath );
							restoreNewFile.delete();
						}

						// Restore backup file
						// Restore input
						restoreByteArrayInputStream			= new ByteArrayInputStream( restoreFileRestore.getBackupFileByteArrayOutputStream().toByteArray() );
						restoreBufferedInputStream			= new BufferedInputStream( restoreByteArrayInputStream );
						
						// Restore output
						restoreOutputFilePath				= restoreFileRestore.getBackupFilePath();
						restoreOutputFile					= new File( restoreOutputFilePath );
						restoreFileOutputStream				= new FileOutputStream( restoreOutputFile );
						restoreBufferedOutputStream			= new BufferedOutputStream( restoreFileOutputStream );
	
				        // Write file to be restored to XFormsDB
						int byteOfData						= 0;
				        while ( ( byteOfData = restoreBufferedInputStream.read() ) != -1 ) {
				        	restoreBufferedOutputStream.write( byteOfData );
				        }
						logger.log( Level.DEBUG, "The backed up file has been successfully restored." );
					} catch ( Exception ex ) {
						// Do nothing
						logger.log( Level.DEBUG, "Failed to restore files." );
					} finally {
						if ( restoreBufferedInputStream != null ) {
							try {
								restoreBufferedInputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( restoreByteArrayInputStream != null ) {
							try {
								restoreByteArrayInputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( restoreBufferedOutputStream != null ) {
							try {
								restoreBufferedOutputStream.close();
							} catch ( IOException ioex ) {
							}
						}
						if ( restoreFileOutputStream != null ) {
							try {
								restoreFileOutputStream.close();
							} catch ( IOException ioex ) {
							}
						}
					}
				}
			}
		}
		
		
		return xformsdbUpdate;
	}
}