package fi.tkk.tml.xformsdb.xml.to.xformsdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The XFormsDB configuration file (conf.xml),
 * i.e. parsed content of the <xformsdb-config> element.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class XFormsDBConfigTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	private static final Logger logger			= Logger.getLogger( XFormsDBConfigTO.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME		= "xformsdb-config";

	
	// PRIVATE VARIABLES
	private List<MimeMappingTO> mimeMappingTOs;
	private String encoding;
	private List<DataSourceTO> dataSourceTOs;
	private FilesMetadataTO filesMetadataTO;
	private String filesFolder;
	private String tdmConflictLevel;
	private String widgetAuthenticationURI;
	private String widgetDataSourcesURI;
	private Map<String, SecurityFileTO> securityFileTOs;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public XFormsDBConfigTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.mimeMappingTOs						= new ArrayList<MimeMappingTO>( 8 );
		this.encoding							= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
		this.dataSourceTOs						= new ArrayList<DataSourceTO>( 16 );
		this.filesMetadataTO					= new FilesMetadataTO();
		this.filesFolder						= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_FOLDER;
		this.tdmConflictLevel					= Constants.XFORMSDB_CONFIG_DEFAULT_TDM_CONFLICT_LEVEL;
		this.widgetAuthenticationURI			= Constants.XFORMSDB_CONFIG_DEFAULT_WIDGET_AUTHENTICATION_URI;
		this.widgetDataSourcesURI				= Constants.XFORMSDB_CONFIG_DEFAULT_WIDGET_DATA_SOURCES_URI;
		this.securityFileTOs					= new HashMap<String, SecurityFileTO>( 8 );
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the MIME mappings (extension and MIME type) that are used to transform
	 * files which are authored by the user in the XFormsDB format.
	 * 
	 * 
	 * @return mimeMappingTOs	The MIME mappings (extension and MIME type)
	 * 							that are used to transform files which are
	 * 							authored by the user in the XFormsDB format.
	 */
	public List<MimeMappingTO> getMimeMappingTOs() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.mimeMappingTOs;
	}

	
	/**
	 * Set the MIME mappings (extension and MIME type) that are used to transform
	 * files which are authored by the user in the XFormsDB format.
	 * 
	 * 
	 * @param mimeMappingTOs	The MIME mappings (extension and MIME type)
	 * 							that are used to transform files which are
	 * 							authored by the user in the XFormsDB format.
	 */
	public void setMimeMappingTOs( List<MimeMappingTO> mimeMappingTOs ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.mimeMappingTOs	= mimeMappingTOs;
	}

	
	/**
	 * Get the character encoding to be used in XML piping and XML documents.
	 * 
	 * 
	 * @return encoding		The character encoding to be used in XML piping and
	 * 						XML documents.
	 */
	public String getEncoding() {
		return this.encoding;
	}


	/**
	 * Set the character encoding to be used in XML piping and XML documents.
	 * 
	 * 
	 * @param encoding		The character encoding to be used in XML piping and
	 * 						XML documents.
	 */
	public void setEncoding( String encoding ) {
		this.encoding	= encoding;
	}

	
	/**
	 * Get the data source configurations used by the application.
	 * 
	 * 
	 * @return dataSourceTOs	The data source configurations used by
	 * 							the application.
	 */
	public List<DataSourceTO> getDataSourceTOs() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.dataSourceTOs;
	}
	
	
	/**
	 * Set the data source configurations used by the application.
	 * 
	 * 
	 * @param dataSourceTOs		The data source configurations used by
	 * 							the application.
	 */
	public void setDataSourceTOs( List<DataSourceTO> dataSourceTOs ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.dataSourceTOs	= dataSourceTOs;
	}
	
	
	/**
	 * Get the data source configurations for files metadata used by the
	 * application.
	 * 
	 * 
	 * @return filesMetadataTO	The data source configurations for files
	 * 							metadata used by the application.
	 */
	public FilesMetadataTO getFilesMetadataTO() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.filesMetadataTO;
	}
	
	
	/**
	 * Set the data source configurations for files metadata used by the
	 * application.
	 * 
	 * 
	 * @param filesMetadataTO	The data source configurations for files
	 * 							metadata used by the application.
	 */
	public void setFilesMetadataTO( FilesMetadataTO filesMetadataTO ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.filesMetadataTO	= filesMetadataTO;
	}

	
	/**
	 * Get the files folder in which files will be stored.
	 * 
	 * 
	 * @return filesFolder	The files folder in which files will be stored.
	 */
	public String getFilesFolder() {
		return this.filesFolder;
	}


	/**
	 * Set the files folder in which files will be stored.
	 * 
	 * 
	 * @param filesFolder	The files folder in which files will be stored.
	 */
	public void setFilesFolder( String filesFolder ) {
		this.filesFolder	= filesFolder;
	}

	
	/**
	 * Get the three-way XML merging conflict level to be used.
	 * 
	 * 
	 * @return tdmConflictLevel		The three-way XML merging conflict level to be used.
	 */
	public String getTDMConflictLevel() {
		return this.tdmConflictLevel;
	}


	/**
	 * Set the three-way XML merging conflict level to be used.
	 * 
	 * 
	 * @param tdmConflictLevel		The three-way XML merging conflict level to be used.
	 */
	public void setTDMConflictLevel( String tdmConflictLevel ) {
		this.tdmConflictLevel	= tdmConflictLevel;
	}

	
	/**
	 * Get the widget authentication URI.
	 * 
	 * 
	 * @return widgetAuthenticationURI	The widget authentication URI.
	 */
	public String getWidgetAuthenticationURI() {
		return this.widgetAuthenticationURI;
	}


	/**
	 * Set the widget authentication URI.
	 * 
	 * 
	 * @param widgetAuthenticationURI	The widget authentication URI.
	 */
	public void setWidgetAuthenticationURI( String widgetAuthenticationURI ) {
		this.widgetAuthenticationURI	= widgetAuthenticationURI;
	}


	/**
	 * Get the widget data sources URI.
	 * 
	 * 
	 * @return widgetDataSourcesURI		The widget data sources URI.
	 */
	public String getWidgetDataSourcesURI() {
		return this.widgetDataSourcesURI;
	}


	/**
	 * Set the widget data sources URI.
	 * 
	 * 
	 * @param widgetDataSourcesURI		The widget data sources URI.
	 */
	public void setWidgetDataSourcesURI( String widgetDataSourcesURI ) {
		this.widgetDataSourcesURI	= widgetDataSourcesURI;
	}

	
	/**
	 * Get the security files defined by extension to be protected from clients.
	 * 
	 * 
	 * @return securityFileTOs	The security files defined by extension to be
	 * 							protected from clients.
	 */
	public Map<String, SecurityFileTO> getSecurityFileTOs() {
		return this.securityFileTOs;
	}


	/**
	 * Set the security files defined by extension to be protected from clients.
	 * 
	 * 
	 * @param securityFileTOs	The security files defined by extension to be
	 * 							protected from clients.
	 */
	public void setSecurityFileTOs( Map<String, SecurityFileTO> securityFileTOs ) {
		this.securityFileTOs	= securityFileTOs;
	}

	
	/**
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result			=  "XFormsDBConfigTO:" + Constants.LINE_SEPARATOR;
		result					+= "-----------------" + Constants.LINE_SEPARATOR;
		for ( int i = 0; i < this.mimeMappingTOs.size(); i++ ) {
			if ( this.mimeMappingTOs.get( i ) != null ) {
				result			+= this.mimeMappingTOs.get( i ).toString() + Constants.LINE_SEPARATOR;
			}
		}
		result					+= "Encoding:" + Constants.LINE_SEPARATOR;
		result					+= "---------" + Constants.LINE_SEPARATOR;
		result					+= this.encoding + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
		for ( int i = 0; i < this.dataSourceTOs.size(); i++ ) {
			if ( this.dataSourceTOs.get( i ) != null ) {
				result			+= this.dataSourceTOs.get( i ).toString() + Constants.LINE_SEPARATOR;
			}
		}
		if ( this.filesMetadataTO != null ) {
			result				+= this.filesMetadataTO.toString() + Constants.LINE_SEPARATOR;
		}
		result					+= "Files folder:" + Constants.LINE_SEPARATOR;
		result					+= "-------------" + Constants.LINE_SEPARATOR;
		result					+= this.filesFolder + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
		result					+= "TDM conflict level:" + Constants.LINE_SEPARATOR;
		result					+= "-------------------" + Constants.LINE_SEPARATOR;
		result					+= this.tdmConflictLevel + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
		result					+= "Widget authentication URI:" + Constants.LINE_SEPARATOR;
		result					+= "--------------------------" + Constants.LINE_SEPARATOR;
		result					+= this.widgetAuthenticationURI + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
		result					+= "Widget data sources URI:" + Constants.LINE_SEPARATOR;
		result					+= "------------------------" + Constants.LINE_SEPARATOR;
		result					+= this.widgetDataSourcesURI + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
		
		Set<String> keys		= this.securityFileTOs.keySet();
		Iterator<String> it		= keys.iterator();
		String key				= null;
		SecurityFileTO value	= null;
		while ( it.hasNext() ) {
			key					= it.next();
			value				= this.securityFileTOs.get( key );
			result				+= value.toString() + Constants.LINE_SEPARATOR;
		}

		
		return result;		
	}
}