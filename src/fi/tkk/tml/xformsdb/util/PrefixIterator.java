package fi.tkk.tml.xformsdb.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Prefix iterator over prefixes.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on March 8, 2007
 */
public class PrefixIterator<E> implements Iterator<E> {

	

	// PRIVATE FINAL VARIABLES
	// Underlying prefixes
	private List<E> prefixes;
	
	
	// PRIVATE VARIABLES
	// Current index into prefixes
	private int index;
	

	// PUBLIC CONSTRUCTORS
	public PrefixIterator( List<E> prefixes ) {
		this.prefixes	= prefixes;
		this.index		= 0;
	}

	
	// PUBLIC METHODS
	/**
	 * Return <code>true</code> if the iteration has more prefixes.
	 * 
	 * 
	 * @return hasNext	<code>true</code> if the iterator has more prefixes,
	 * 					otherwise <code>false</code>.
	 */
	public boolean hasNext() {
		return this.index < this.prefixes.size();
	}
	
	
	/**
	 * Return the next prefix in the iteration.
	 * 
	 * 
	 * @return prefix					The next prefix in the iteration.
	 * @throws NoSuchElementException	Iteration has no more prefixes.

	 */
	public E next() throws NoSuchElementException {
		if ( this.index >= this.prefixes.size() ) {
			throw new NoSuchElementException( "Iteration has no more prefixes." );
		}
		
		return ( E ) this.prefixes.get( this.index++ );
	}
	
	
	/**
	 * Remove from the underlying collection the last prefix
	 * returned by the iterator.
	 * 
	 * NOTE: Unsupported operation.
	 * 
	 * 
	 * @throws UnsupportedOperationException	If the remove operation
	 * 											is not supported by this
	 * 											Iterator.
	 * @throws IllegalStateException			If the next method has not
	 * 											yet been called, or the remove
	 * 											method has already been called
	 * 											after the last call to the next
	 * 											method.
	 */
	public void remove() throws UnsupportedOperationException, IllegalStateException {
		throw new UnsupportedOperationException( "PrefixIterator does not support this method." );
	}
}