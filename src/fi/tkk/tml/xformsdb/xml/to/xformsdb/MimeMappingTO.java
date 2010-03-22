package fi.tkk.tml.xformsdb.xml.to.xformsdb;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The MIME mapping (extension and MIME type) that is used to transform
 * files which are authored by the user in the XFormsDB format, i.e.
 * parsed content of the <mime-mapping> element from the XFormsDB
 * configuration file (conf.xml).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on March 26, 2007
 */
public class MimeMappingTO extends fi.tkk.tml.xformsdb.xml.to.MimeMappingTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	private static final Logger logger			= Logger.getLogger( MimeMappingTO.class );
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public MimeMappingTO() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		super.setExtension( Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_EXTENSION );
		super.setMimeType( Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE );
	}
}