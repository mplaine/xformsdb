package fi.tkk.tml.xformsdb.manager.xslt;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;


/**
 * Manage transformers.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on July 07, 2009
 */
public class TransformerManager {


	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( TransformerManager.class );

	
	// PRIVATE STATIC VARIABLES
	// Map XSLT file names to MapEntry instances
	// (MapEntry is defined below)
	private static Map<String, MapEntry> cache	= new HashMap<String, MapEntry>();


	// PRIVATE CONSTRUCTORS
	// Prevent instantiation of this class
	private TransformerManager() {
	}
	

	// PUBLIC STATIC SYNCHRONIZED METHODS
	public static synchronized void removeAll() throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Remove all cached transformers from memory
			cache.clear();
			logger.log( Level.DEBUG, "All cached transformers have been successfully removed from memory." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_13, ErrorConstants.ERROR_MESSAGE_MANAGER_13, ex );
		}
	}
	

	public static synchronized void remove( String xsltFileName ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Remove the cached transformer from memory
			cache.remove( xsltFileName );
			logger.log( Level.DEBUG, "The cached transformer has been successfully removed from memory." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_14, ErrorConstants.ERROR_MESSAGE_MANAGER_14, ex );
		}
	}
	

	public static synchronized Transformer getTransformer( String xsltFileName ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		URLConnection xsltURLConnection			= null;
		//File xsltFile							= null;
		long xsltLastModified					= 0;
		MapEntry entry							= null;
		Source xsltSource						= null;
		TransformerFactory transformerFactory	= null;
		Templates templates						= null;
		Transformer transformer					= null;
		
		
		try {
			// Retrieve the XSLT stylesheet file
			xsltURLConnection					= Thread.currentThread().getContextClassLoader().getResource( xsltFileName ).openConnection();
			//xsltFile							= new File( xsltFileName );
			
			// Determine when the file was last modified on disk
			xsltLastModified					= xsltURLConnection.getLastModified();
			//xsltLastModified					= xsltFile.lastModified();
			entry								= ( MapEntry ) cache.get( xsltFileName );
			
			if ( entry != null ) {
				// If the file has been modified more recently than the
				// cached XSLT stylesheet, remove the entry reference
				if ( xsltLastModified > entry.lastModified ) {
					entry						= null;
				}
			}
	
			// Create a new entry in the cache if necessary
			if ( entry == null ) {
				xsltSource						= new StreamSource( xsltURLConnection.getInputStream() );
				xsltSource.setSystemId( xsltURLConnection.getURL().toExternalForm() ); // Important for resolving relative URIs!
				//xsltSource					= new StreamSource( xsltFile );
				
				transformerFactory				= TransformerFactory.newInstance();
				templates						= transformerFactory.newTemplates( xsltSource );
				
				entry							= new MapEntry( xsltLastModified, templates );
				cache.put( xsltFileName, entry );
			}
			
			transformer							= entry.templates.newTransformer();
			logger.log( Level.DEBUG, "The cached transformer has been successfully retrieved from memory." );
		} catch ( TransformerConfigurationException tcex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_25, tcex.getMessage(), tcex );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_15, ErrorConstants.ERROR_MESSAGE_MANAGER_15, ex );
		}

		
		return transformer;
	}
	
	
	/**
	 * This class represents a value in the cache Map.
	 * 
	 * 
	 * @author Markku Laine
	 */
	static class MapEntry {
		long lastModified; // When the file was modified
		Templates templates;
		
		
		// CONSTRUCTORS
		MapEntry( long lastModified, Templates templates ) {
			this.lastModified	= lastModified;
			this.templates		= templates;
		}
	}
}