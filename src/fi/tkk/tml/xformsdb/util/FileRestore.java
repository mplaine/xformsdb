package fi.tkk.tml.xformsdb.util;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Convenience class for restoring files.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 19, 2009
 */
public class FileRestore {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( FileRestore.class );

	
	// PRIVATE VARIABLES
	private String backupFilePath;
	private ByteArrayOutputStream backupFileByteArrayOutputStream;
	private String newFilePath;
	

	// PUBLIC CONSTRUCTORS
	public FileRestore( String backupFilePath, ByteArrayOutputStream backupFileByteArrayOutputStream, String newFilePath ) {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.backupFilePath						= backupFilePath;
		this.backupFileByteArrayOutputStream	= backupFileByteArrayOutputStream;
		this.newFilePath						= newFilePath;
	}

	
	// PUBLIC METHODS
	public void setBackupFilePath( String backupFilePath ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.backupFilePath						= backupFilePath;
	}
	public String getBackupFilePath() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.backupFilePath;
	}


	public void setBackupFileByteArrayOutputStream( ByteArrayOutputStream backupFileByteArrayOutputStream ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.backupFileByteArrayOutputStream		= backupFileByteArrayOutputStream;
	}
	public ByteArrayOutputStream getBackupFileByteArrayOutputStream() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.backupFileByteArrayOutputStream;
	}
	
	
	public void setNewFilePath( String newFilePath ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.newFilePath						= newFilePath;
	}
	public String getNewFilePath() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.newFilePath;
	}
}