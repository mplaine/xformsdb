package fi.tkk.tml.xformsdb.xml.ns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import fi.tkk.tml.xformsdb.util.PrefixIterator;


/**
 * XML Namespace contexts.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on August 04, 2008
 */
public class NamespaceContextImpl implements NamespaceContext {

	
	
	// PRIVATE VARIABLES
	private Map<String, String> contexts;

	
	// PUBLIC CONSTRUCTORS
	/**
	 * Initialize the XML Namespace context implementation.
	 */
	public NamespaceContextImpl() {
		super();
		this.contexts	= new HashMap<String, String>( 16 );
	}
	
	
	// PUBLIC METHODS
	/**
	 * Bound Namespace URI to a prefix in the current scope.
	 * 
	 * 
	 * @param prefix						Prefix to bound Namespace URI to.
	 * @throws IllegalArgumentException		When prefix is null.
	 */
	public void bound( String prefix, String namespaceURI ) throws IllegalArgumentException {
		if ( prefix == null ) {
			throw new IllegalArgumentException( "Prefix is null." );
		}
		else if (	( XMLConstants.XML_NS_PREFIX.equals( prefix ) == false ) &&
					( XMLConstants.XMLNS_ATTRIBUTE.equals( prefix ) == false ) ) {
			// Replaces old values if necessary
			this.contexts.put( prefix, namespaceURI );
		}
	}

	
	/**
	 * Retrieve XML Namespace contexts.
	 * 
	 * 
	 * @return contexts		XML Namespace contexts.
	 */
	public Map<String, String> getContexts() {
		return this.contexts;
	}

	
	/**
	 * Clear XML Namespace contexts.
	 */
	public void clearContexts() {
		this.contexts.clear();
	}

	
	/**
	 * Get Namespace URI bound to a prefix in the current scope.
	 * 
	 * 
	 * @param prefix						Prefix to look up.
	 * @return namespaceURI					Namespace URI bound to prefix in the current scope.
	 * @throws IllegalArgumentException		When prefix is null.
	 */
	public String getNamespaceURI( String prefix ) throws IllegalArgumentException {
		String namespaceURI		= null;
		
		
		if ( prefix == null ) {
			throw new IllegalArgumentException( "Prefix is null." );
		}
		else if ( XMLConstants.XMLNS_ATTRIBUTE.equals( prefix ) == true ) {
			namespaceURI		= XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		}
		else if ( XMLConstants.XML_NS_PREFIX.equals( prefix ) == true ) {
			namespaceURI		= XMLConstants.XML_NS_URI;
		}
		else {
			// Namespace URI bound to a prefix in the current scope
			namespaceURI		= this.contexts.get( prefix );

			// Unbound prefix and Default Namespace prefix is used
			if ( ( namespaceURI == null ) && ( XMLConstants.DEFAULT_NS_PREFIX.equals( prefix ) == true ) ) {
				// Default Namespace URI in the current scope
				namespaceURI	= "";
			}
			// Unbound prefix
			else if ( namespaceURI == null ) {
				namespaceURI	= "";
			}
			// Bound prefix
			else {
				// Already set above
			}
		}
		
		
		return namespaceURI;
	}
	
	
	/**
	 * Get prefix bound to Namespace URI in the current scope.
	 * 
	 * 
	 * @param namespaceURI					URI of Namespace to lookup.
	 * @return prefix						Prefix bound to Namespace URI in current context.
	 * @throws IllegalArgumentException		When namespaceURI is null.
	 */
	public String getPrefix( String namespaceURI ) throws IllegalArgumentException {
		String prefix												= null;
		List<String> boundPrefixes									= null;
		
		
		if ( namespaceURI == null ) {
			throw new IllegalArgumentException( "Namespace URI is null." );
		}
		else if ( XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals( namespaceURI ) == true ) {
			prefix													= XMLConstants.XMLNS_ATTRIBUTE;
		}
		else if ( XMLConstants.XML_NS_URI.equals( namespaceURI ) == true ) {
			prefix													= XMLConstants.XML_NS_PREFIX;
		}
		else {
			// XML Namespace contexts contains the given Namespace URI
			if ( this.contexts.containsValue( namespaceURI ) == true ) {
				// Retrieve contexts in this map
				Set<Map.Entry<String, String>> contexts	= this.contexts.entrySet();
				
				// Iterate over all contexts
				Iterator<Map.Entry<String, String>> contextIterator	= contexts.iterator();
				boundPrefixes										= new ArrayList<String>( 8 );
				Map.Entry<String, String> context					= null;
				while ( contextIterator.hasNext() == true ) {
					context											= contextIterator.next();
					
					// Bound
					if ( namespaceURI.equals( context.getValue() ) == true ) {
						boundPrefixes.add( context.getKey() );
					}
				}
				
				// Return the first bounded prefix
				prefix												= boundPrefixes.get( 0 );
			}
			// XML Namespace contexts does not contain the given Namespace URI and null Namespace URI is used
			else if ( ( this.contexts.containsValue( namespaceURI ) == false ) && ( "".equals( namespaceURI ) == true ) ) {
				prefix												= XMLConstants.DEFAULT_NS_PREFIX;
			}
			// Unbound Namespace URI
			else {
				// Already set (initial value)
			}
		}

		
		return prefix;
	}

	
	/**
	 * Get prefix bound to Namespace URI in the current scope.
	 * 
	 * 
	 * @param namespaceURI					URI of Namespace to lookup.
	 * @return prefixesIterator				For all prefixes bound to the Namespace URI in the current scope.
	 * @throws IllegalArgumentException		When namespaceURI is null.
	 */
	public Iterator getPrefixes( String namespaceURI ) throws IllegalArgumentException {
		List<String> boundPrefixes									= new ArrayList<String>( 8 );
		Iterator prefixIterator										= null;
		
		
		if ( namespaceURI == null ) {
			throw new IllegalArgumentException( "Namespace URI is null." );
		}
		else if ( XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals( namespaceURI ) == true ) {
			boundPrefixes.add( XMLConstants.XMLNS_ATTRIBUTE );
		}
		else if ( XMLConstants.XML_NS_URI.equals( namespaceURI ) == true ) {
			boundPrefixes.add( XMLConstants.XML_NS_PREFIX );
		}
		else {
			// XML Namespace contexts contains the given Namespace URI
			if ( this.contexts.containsValue( namespaceURI ) == true ) {
				// Retrieve contexts in this map
				Set<Map.Entry<String, String>> contexts				= this.contexts.entrySet();
				
				// Iterate over all contexts
				Iterator<Map.Entry<String, String>> contextIterator	= contexts.iterator();
				Map.Entry<String, String> context					= null;
				while ( contextIterator.hasNext() == true ) {
					context											= contextIterator.next();
					
					// Bound
					if ( namespaceURI.equals( context.getValue() ) == true ) {
						boundPrefixes.add( context.getKey() );
					}
				}
				
				// Null Namespace URI is used
				if ( "".equals( namespaceURI ) == true ) {
					boundPrefixes.add( XMLConstants.DEFAULT_NS_PREFIX );
				}				
			}
			// XML Namespace contexts does not contain the given Namespace URI and null Namespace URI is used
			else if ( ( this.contexts.containsValue( namespaceURI ) == false ) && ( "".equals( namespaceURI ) == true ) ) {
				boundPrefixes.add( XMLConstants.DEFAULT_NS_PREFIX );
			}
			// Unbound Namespace URI
			else {
				// Already set (initial value)
			}
		}
		prefixIterator	= new PrefixIterator<String>( boundPrefixes );
		
		
		return prefixIterator;
	}
}