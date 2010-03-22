package fi.tkk.tml.xformsdb.util;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Convenience class for filtering filenames.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 19, 2009
 */
public class FileBackupFilenameFilter implements FilenameFilter {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( FileBackupFilenameFilter.class );

	
	// PRIVATE VARIABLES
	private File filesFolder;
	private String filenamePrefix;
	
	
	// PUBLIC CONSTRUCTORS
	public FileBackupFilenameFilter() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.filesFolder				= null;
		this.filenamePrefix				= null;
	}
	
	
	// PUBLIC METHODS
	// A method defined in the FilenameFilter interface
	public boolean accept( File dir, String name ) {
		logger.log( Level.DEBUG, "Method has been called." );
		boolean accept					= false;
		
		if ( dir != null && dir.compareTo( this.filesFolder ) == 0 ) {
			if ( name != null && name.startsWith( this.filenamePrefix ) == true ) {
				accept					= true;
			}
		}
		
		return accept;
	}
	
	
	public void setFilesFolder( File filesFolder ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.filesFolder				= filesFolder;
	}
	public File getFilesFolder() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.filesFolder;
	}
	
	
	public void setFilenamePrefix( String filenamePrefix ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.filenamePrefix				= filenamePrefix;
	}
	public String getFilenamePrefix() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.filenamePrefix;
	}
}