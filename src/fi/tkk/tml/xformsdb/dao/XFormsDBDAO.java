package fi.tkk.tml.xformsdb.dao;

import fi.tkk.tml.xformsdb.error.DAOException;
import nu.xom.Document;


/**
 * Interface class that all XFormsDBDAOs must support.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 22, 2009
 */
public interface XFormsDBDAO {

	
	
	// PUBLIC METHODS (INTERFACES)
	/**
	 * Execute the select query expression and get the result in XML format.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:query> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The result of the query in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String select( Document document, String encoding ) throws DAOException;
	
	
	/**
	 * Execute the update query expression and get the result in XML format.
	 * 
	 * 
	 * @param document				The XML document (<xformsdb:query> element).
	 * @param tm					The merged XML document; 3DM result.
	 * @param encoding				The character encoding to be used.
	 * @return xmlString			The result of the query in XML format.
	 * @throws DAOException			DAO exception.
	 */
	public String update( Document document, String tm, String encoding ) throws DAOException;

	
	/**
	 * Execute all query expression and get the result in XML format.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:query> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The result of the query in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String all( Document document, String encoding ) throws DAOException;


	/**
	 * Authenticate the user with a username and password against the realm.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:login> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated user in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateUser( Document document, String encoding ) throws DAOException;
	
	
	/**
	 * Select files from the files metadata data source.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:file> element).
	 * @param actionURL			The action URL, from which the request has been sent.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String selectFiles( Document document, String actionURL, String encoding ) throws DAOException;
	
	
	/**
	 * Select file from the files metadata data source.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:file> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The files list containing only one file in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String selectFile( Document document, String encoding ) throws DAOException;
	
	
	/**
	 * Insert files to the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (insert)).
	 * @param actionURL			The action URL, from which the request has been sent.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The inserted files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String insertFiles( Document document, String actionURL, String encoding ) throws DAOException;
	
	
	/**
	 * Delete files from the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (delete)).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The deleted files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String deleteFiles( Document document, String encoding ) throws DAOException;
	
	
	/**
	 * Update files to the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (update)).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The updated files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String updateFiles( Document document, String encoding ) throws DAOException;
	
	
	/**
	 * Authenticate the widget user with an idkey and at against the widget realm.
	 * 
	 * 
	 * @param idkey				The idkey for authenticating the widget user.
	 * @param at				The at for authenticating the widget user.
	 * @param uriString			The URI string for connecting to the widget realm.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated widget user in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateWidgetUser( String idkey, String at, String uriString, String encoding ) throws DAOException;
	
	
	/**
	 * Authenticate the widget data sources with an idkey and at against the widget data sources.
	 * 
	 * 
	 * @param idkey				The idkey for authenticating the widget data sources.
	 * @param at				The at for authenticating the widget data sources.
	 * @param uriString			The URI string for connecting to the widget data sources.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated widget user's data sources in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateWidgetDataSources( String idkey, String at, String uriString, String encoding ) throws DAOException;
}