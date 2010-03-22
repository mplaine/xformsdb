package fi.tkk.tml.xformsdb.merger;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.MergerException;
import fi.tkk.tml.xformsdb.xml.sax.Writer;
import fi.tkk.tml.xformsdb.tdm.lib.ConflictLog;
import fi.tkk.tml.xformsdb.tdm.lib.Merge;
import fi.tkk.tml.xformsdb.tdm.lib.XMLParser;
import fi.tkk.tml.xformsdb.tdm.lib.XMLPrinter;
import fi.tkk.tml.xformsdb.util.IOUtils;
import tdm.lib.BaseNode;
import tdm.lib.BranchNode;
import tdm.lib.HeuristicMatching;
import tdm.lib.Matching;
import tdm.lib.TriMatching;


/**
 * Three-way merges XML documents.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 19, 2009
 */
public class XMLMerger {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final int COPY_THRESHOLD		= 2000000000;
	private static final Logger logger			= Logger.getLogger( XMLMerger.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XMLMerger() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
		
	// PUBLIC STATIC SYNCHRONIZED METHODS	
	public static synchronized String merge( String t0, String t1, String t2, String tdmConflictLevel, String encoding ) throws MergerException {
		logger.log( Level.DEBUG, "Method has been called." );
		HeuristicMatching.COPY_THRESHOLD	= XMLMerger.COPY_THRESHOLD; // Basically, this means that copies are ignored
		XMLParser xmlParser					= null;
		Matching matchingA					= null;
		Matching matchingB					= null;
		BaseNode baseNode					= null;
		BranchNode branchNodeA				= null;
		BranchNode branchNodeB				= null;		
		Merge merge							= null;
		ConflictLog conflictLog				= null;
		ByteArrayOutputStream out			= null;
		String tm							= null;

		
		// Create the 3DM specific XML parser
		try {
			xmlParser			= new XMLParser();
			logger.log( Level.DEBUG, "The 3DM specific XML parser has been successfully created." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_1, ErrorConstants.ERROR_MESSAGE_MERGER_1, ex );			
		}
		
		// Create the matching classes
		try {
			matchingA			= ( Matching ) HeuristicMatching.class.newInstance();
			matchingB			= ( Matching ) HeuristicMatching.class.newInstance();
			logger.log( Level.DEBUG, "The matching classes have been successfully created." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_8, ErrorConstants.ERROR_MESSAGE_MERGER_8, ex );			
		}
		
		
		// Parse the XML document (T0, the original version)
		try {
			baseNode			= ( BaseNode ) xmlParser.parse( IOUtils.convert( t0, encoding ), encoding, matchingA.getBaseNodeFactory() );
			logger.log( Level.DEBUG, "The XML document (T0, the original version) has been successfully parsed." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_2, ErrorConstants.ERROR_MESSAGE_MERGER_2, ex );			
		}

		// Parse the XML document (T1, the modified version)
		try {
			branchNodeA			= ( BranchNode ) xmlParser.parse( IOUtils.convert( t1, encoding ), encoding, matchingA.getBranchNodeFactory() );
			logger.log( Level.DEBUG, "The XML document (T1, the modified version) has been successfully parsed." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_3, ErrorConstants.ERROR_MESSAGE_MERGER_3, ex );			
		}

		// Parse the XML document (T2, the current version)
		try {
			branchNodeB			= ( BranchNode ) xmlParser.parse( IOUtils.convert( t2, encoding ), encoding, matchingB.getBranchNodeFactory() );
			logger.log( Level.DEBUG, "The XML document (T2, the current version) has been successfully parsed." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_4, ErrorConstants.ERROR_MESSAGE_MERGER_4, ex );			
		}
		
		
		// Merge the XML documents (T0, T1, and T2)
		try {
			out					= new ByteArrayOutputStream();
			merge				= new Merge( new TriMatching( branchNodeA, baseNode, branchNodeB, matchingA.getClass(), matchingB.getClass() ) );

			// BEGIN: XML writer option #1
			merge.merge( new Writer( out, encoding ) );
			// END: XML writer option #1
			
			// BEGIN: XML writer option #2
			//FIXME: Encodings within the XMLPrinter class
			//merge.merge( new XMLPrinter( out ) );
			// END: XML writer option #2
			logger.log( Level.DEBUG, "The XML documents (T0, T1, and T2) have been successfully merged." );
		} catch ( Exception ex ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_5, ErrorConstants.ERROR_MESSAGE_MERGER_5, ex );			
		}
		
		
		// Handle the 3DM result
		try {
			// Retrieve the conflict log
			conflictLog			= merge.getConflictLog();
			logger.log( Level.DEBUG, "The 3DM result contains " + conflictLog.getConflictsSize() + " conflict(s) and " + conflictLog.getWarningsSize() + " conflict warning(s)." );
			
			if ( Constants.TDM_CONFLICT_LEVEL_ERROR.equalsIgnoreCase( tdmConflictLevel ) ) {
				// No conflicts --> return the XML document (Tm, the merged version)
				if ( conflictLog.hasConflicts() == false ) {
					tm			= out.toString( encoding );
				}
				// Conflicts --> throw a new exception
				else {
					out			= new ByteArrayOutputStream();
					merge.getConflictLog().writeConflicts( new Writer( out, encoding ) );
					throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_6, ErrorConstants.ERROR_MESSAGE_MERGER_6_1 + conflictLog.getConflictsSize() + ErrorConstants.ERROR_MESSAGE_MERGER_6_2 + Constants.LINE_SEPARATOR + out.toString( encoding ) );
				}
			}
			else if ( Constants.TDM_CONFLICT_LEVEL_WARN.equalsIgnoreCase( tdmConflictLevel ) ) {
				// No conflicts nor conflict warnings --> return the XML document (Tm, the merged version)
				if ( conflictLog.hasConflicts() == false && conflictLog.hasWarnings() == false ) {
					tm			= out.toString( encoding );
				}
				// Conflicts or conflict warnings --> throw a new exception
				else {
					out			= new ByteArrayOutputStream();
					merge.getConflictLog().writeConflictsAndConflictWarnings( new Writer( out, encoding ) );
					throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_9, ErrorConstants.ERROR_MESSAGE_MERGER_9_1 + conflictLog.getConflictsSize() + ErrorConstants.ERROR_MESSAGE_MERGER_9_2 + conflictLog.getWarningsSize() + ErrorConstants.ERROR_MESSAGE_MERGER_9_3 + Constants.LINE_SEPARATOR + out.toString( encoding ) );
				}
			}
			
			logger.log( Level.DEBUG, "The 3DM result has been successfully handled." );
		} catch ( MergerException mex ) {
			throw mex;
		} catch ( Exception e ) {
			throw new MergerException( ErrorConstants.ERROR_CODE_MERGER_7, ErrorConstants.ERROR_MESSAGE_MERGER_7 );			
		}
		
		
		return tm;
	}
}