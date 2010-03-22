package fi.tkk.tml.xformsdb.error;


/**
 * Error constants.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on February 23, 2010
 */
public class ErrorConstants {
	

	
	// PUBLIC STATIC FINAL VARIABLES
	// ResponseHandler errors
	public static final int ERROR_CODE_RESPONSEHANDLER_1			= 1001;
	public static final String ERROR_MESSAGE_RESPONSEHANDLER_1		= "Failed to write response.";

	public static final int ERROR_CODE_RESPONSEHANDLER_2			= 1002;
	public static final String ERROR_MESSAGE_RESPONSEHANDLER_2		= "Failed to write response for a file to be downloaded.";


	// RequestHandler errors
	public static final int ERROR_CODE_REQUESTHANDLER_1				= 2001;
	public static final String ERROR_MESSAGE_REQUESTHANDLER_1		= "Failed to handle the request.";

	
	// FuegoCodeTransformer errors
	public static final int ERROR_CODE_FUEGOCORETRANSFORMER_1		= 3001;
	public static final String ERROR_MESSAGE_FUEGOCORETRANSFORMER_1	= "Failed to execute the XSLT transformation (Fuego Core).";

	
	// SAXParser errors
	public static final int ERROR_CODE_SAXPARSER_1					= 4001; // Warning

	public static final int ERROR_CODE_SAXPARSER_2					= 4002; // Error

	public static final int ERROR_CODE_SAXPARSER_3					= 4003; // Fatal error

	
	// DOMParser errors
	public static final int ERROR_CODE_DOMPARSER_1					= 5001;

	public static final int ERROR_CODE_DOMPARSER_2					= 5002;
	public static final String ERROR_MESSAGE_DOMPARSER_2			= "Failed to handle the collection XML file.";

	public static final int ERROR_CODE_DOMPARSER_3					= 5003;
	public static final String ERROR_MESSAGE_DOMPARSER_3			= "Failed to handle the XFormsDB user XML file.";

	
	// Authentication errors
	public static final int ERROR_CODE_AUTHENTICATION_1				= 6001;
	public static final String ERROR_MESSAGE_AUTHENTICATION_1		= "Incorrect username/password combination.";

	public static final int ERROR_CODE_AUTHENTICATION_2				= 6002;
	public static final String ERROR_MESSAGE_AUTHENTICATION_2		= "Incorrect username/password combination.";

	public static final int ERROR_CODE_AUTHENTICATION_3				= 6003;
	public static final String ERROR_MESSAGE_AUTHENTICATION_3		= "Incorrect idkey/at combination while trying to retrieve the widget user information.";

	public static final int ERROR_CODE_AUTHENTICATION_4				= 6004;
	public static final String ERROR_MESSAGE_AUTHENTICATION_4		= "Incorrect idkey/at combination while trying to retrieve the widget user information.";

	public static final int ERROR_CODE_AUTHENTICATION_5				= 6005;
	public static final String ERROR_MESSAGE_AUTHENTICATION_5		= "Incorrect idkey/at combination while trying to retrieve the widget user's data sources.";

	public static final int ERROR_CODE_AUTHENTICATION_6				= 6006;
	public static final String ERROR_MESSAGE_AUTHENTICATION_6		= "Incorrect idkey/at combination while trying to retrieve the widget user's data sources.";

	
	// DAO errors
	public static final int ERROR_CODE_DAO_1						= 7001;
	public static final String ERROR_MESSAGE_DAO_1					= "The initialization query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_2						= 7002;
	public static final String ERROR_MESSAGE_DAO_2					= "The initialization query expression is null.";

	public static final int ERROR_CODE_DAO_3						= 7003;
	public static final String ERROR_MESSAGE_DAO_3					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_4						= 7004;
	public static final String ERROR_MESSAGE_DAO_4					= "The SELECT4UPDATE query expression does not contain an XPath expression.";

	public static final int ERROR_CODE_DAO_5						= 7005;
	public static final String ERROR_MESSAGE_DAO_5					= "The SELECT query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_6						= 7006;
	public static final String ERROR_MESSAGE_DAO_6					= "The SELECT4UPDATE query expression is null.";

	public static final int ERROR_CODE_DAO_7						= 7007;
	public static final String ERROR_MESSAGE_DAO_7					= "The SELECT query expression is null.";

//	public static final int ERROR_CODE_DAO_8						= 7008;

	public static final int ERROR_CODE_DAO_9						= 7009;
	public static final String ERROR_MESSAGE_DAO_9					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_10						= 7010;
	public static final String ERROR_MESSAGE_DAO_10_1				= "Failed to execute query '";
	public static final String ERROR_MESSAGE_DAO_10_2				= "' against data source.";

	public static final int ERROR_CODE_DAO_11						= 7011;
	public static final String ERROR_MESSAGE_DAO_11					= "The UPDATE query expression does not contain an XPath expression.";

	public static final int ERROR_CODE_DAO_12						= 7012;
	public static final String ERROR_MESSAGE_DAO_12					= "The UPDATE query expression is null.";

//	public static final int ERROR_CODE_DAO_13						= 7013;

	public static final int ERROR_CODE_DAO_14						= 7014;
	public static final String ERROR_MESSAGE_DAO_14					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_15						= 7015;
	public static final String ERROR_MESSAGE_DAO_15					= "Failed to update the collection.";

	public static final int ERROR_CODE_DAO_16						= 7016;
	public static final String ERROR_MESSAGE_DAO_16					= "Method not supported.";

	public static final int ERROR_CODE_DAO_17						= 7017;
	public static final String ERROR_MESSAGE_DAO_17					= "Method not supported.";

	public static final int ERROR_CODE_DAO_18						= 7018;
	public static final String ERROR_MESSAGE_DAO_18					= "Method not supported.";

	public static final int ERROR_CODE_DAO_19						= 7019;
	public static final String ERROR_MESSAGE_DAO_19					= "Method not supported.";

	public static final int ERROR_CODE_DAO_20						= 7020;
	public static final String ERROR_MESSAGE_DAO_20					= "Method not supported.";

	public static final int ERROR_CODE_DAO_21						= 7021;
	public static final String ERROR_MESSAGE_DAO_21					= "The SELECT4UPDATE query expression does not contain an XPath expression.";

	public static final int ERROR_CODE_DAO_22						= 7022;
	public static final String ERROR_MESSAGE_DAO_22					= "The SELECT query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_23						= 7023;
	public static final String ERROR_MESSAGE_DAO_23					= "The SELECT4UPDATE query expression is null.";

	public static final int ERROR_CODE_DAO_24						= 7024;
	public static final String ERROR_MESSAGE_DAO_24					= "The SELECT query expression is null.";

	public static final int ERROR_CODE_DAO_25						= 7025;
	public static final String ERROR_MESSAGE_DAO_25_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_25_2				= "' from file.";

	public static final int ERROR_CODE_DAO_26						= 7026;
	public static final String ERROR_MESSAGE_DAO_26_1				= "Failed to execute query '";
	public static final String ERROR_MESSAGE_DAO_26_2				= "' against data source.";

	public static final int ERROR_CODE_DAO_27						= 7027;
	public static final String ERROR_MESSAGE_DAO_27					= "Method not supported.";

	public static final int ERROR_CODE_DAO_28						= 7028;
	public static final String ERROR_MESSAGE_DAO_28_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_28_2				= "' from file.";

	public static final int ERROR_CODE_DAO_29						= 7029;
	public static final String ERROR_MESSAGE_DAO_29					= "Failed to execute the initialization query against data source.";

	public static final int ERROR_CODE_DAO_30						= 7030;

	public static final int ERROR_CODE_DAO_31						= 7031;
	public static final String ERROR_MESSAGE_DAO_31					= "Failed to connect to data source.";

	public static final int ERROR_CODE_DAO_32						= 7032;
	public static final String ERROR_MESSAGE_DAO_32					= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_33						= 7033;
	public static final String ERROR_MESSAGE_DAO_33					= "Failed to connect to data source.";

	public static final int ERROR_CODE_DAO_34						= 7034;
	public static final String ERROR_MESSAGE_DAO_34					= "Failed to connect to data source.";

	public static final int ERROR_CODE_DAO_35						= 7035;
	public static final String ERROR_MESSAGE_DAO_35					= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_36						= 7036;
	public static final String ERROR_MESSAGE_DAO_36					= "Failed to lookup realm.";

	public static final int ERROR_CODE_DAO_37						= 7037;
	public static final String ERROR_MESSAGE_DAO_37					= "The authentication query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_38						= 7038;
	public static final String ERROR_MESSAGE_DAO_38					= "The authentication query expression is null.";

	public static final int ERROR_CODE_DAO_39						= 7039;
	public static final String ERROR_MESSAGE_DAO_39					= "Failed to connect to realm.";

	public static final int ERROR_CODE_DAO_40						= 7040;
	public static final String ERROR_MESSAGE_DAO_40_1				= "Failed to execute query '";
	public static final String ERROR_MESSAGE_DAO_40_2				= "' against realm.";

	public static final int ERROR_CODE_DAO_41						= 7041;
	public static final String ERROR_MESSAGE_DAO_41					= "Failed to retrieve the initialization query file.";

	public static final int ERROR_CODE_DAO_42						= 7042;
	public static final String ERROR_MESSAGE_DAO_42					= "Failed to retrieve the authentication query file.";

	public static final int ERROR_CODE_DAO_43						= 7043;
	public static final String ERROR_MESSAGE_DAO_43					= "Failed to retrieve the authentication query file.";

	public static final int ERROR_CODE_DAO_44						= 7044;
	public static final String ERROR_MESSAGE_DAO_44					= "The authentication query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_45						= 7045;
	public static final String ERROR_MESSAGE_DAO_45					= "The authentication query expression is null.";

	public static final int ERROR_CODE_DAO_46						= 7046;
	public static final String ERROR_MESSAGE_DAO_46_1				= "Failed to execute query '";
	public static final String ERROR_MESSAGE_DAO_46_2				= "' against realm.";

	public static final int ERROR_CODE_DAO_47						= 7047;
	public static final String ERROR_MESSAGE_DAO_47_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_47_2				= "' from file.";

	public static final int ERROR_CODE_DAO_48						= 7048;
	public static final String ERROR_MESSAGE_DAO_48					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_49						= 7049;
	public static final String ERROR_MESSAGE_DAO_49_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_49_2				= "' from file.";

	public static final int ERROR_CODE_DAO_50						= 7050;
	public static final String ERROR_MESSAGE_DAO_50_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_50_2				= "' from file.";

	public static final int ERROR_CODE_DAO_51						= 7051;
	public static final String ERROR_MESSAGE_DAO_51					= "The query did not return any data.";

	public static final int ERROR_CODE_DAO_52						= 7052;
	public static final String ERROR_MESSAGE_DAO_52					= "The query did not return any data.";

	public static final int ERROR_CODE_DAO_53						= 7053;
	public static final String ERROR_MESSAGE_DAO_53					= "Method not supported.";

	public static final int ERROR_CODE_DAO_54						= 7054;
	public static final String ERROR_MESSAGE_DAO_54					= "The ALL query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_55						= 7055;
	public static final String ERROR_MESSAGE_DAO_55					= "The ALL query expression is null.";

	public static final int ERROR_CODE_DAO_56						= 7056;
	public static final String ERROR_MESSAGE_DAO_56_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_56_2				= "' from file.";

	public static final int ERROR_CODE_DAO_57						= 7057;
	public static final String ERROR_MESSAGE_DAO_57					= "The query did not return any data.";

	public static final int ERROR_CODE_DAO_58						= 7058;
	public static final String ERROR_MESSAGE_DAO_58					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_59						= 7059;
	public static final String ERROR_MESSAGE_DAO_59_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_DAO_59_2				= "' from file.";

	public static final int ERROR_CODE_DAO_60						= 7060;
	public static final String ERROR_MESSAGE_DAO_60_1				= "Failed to execute query '";
	public static final String ERROR_MESSAGE_DAO_60_2				= "' against data source.";

	public static final int ERROR_CODE_DAO_61						= 7061;
	public static final String ERROR_MESSAGE_DAO_61					= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_62						= 7062;
	public static final String ERROR_MESSAGE_DAO_62					= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_64						= 7064;
	public static final String ERROR_MESSAGE_DAO_64					= "Failed to retrieve the select files query file.";

	public static final int ERROR_CODE_DAO_65						= 7065;
	public static final String ERROR_MESSAGE_DAO_65					= "The select files query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_66						= 7066;
	public static final String ERROR_MESSAGE_DAO_66					= "The select files query expression is null.";

	public static final int ERROR_CODE_DAO_67						= 7067;
	public static final String ERROR_MESSAGE_DAO_67					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_68						= 7068;
	public static final String ERROR_MESSAGE_DAO_68_1				= "Failed to retrieve select files query '";
	public static final String ERROR_MESSAGE_DAO_68_2				= "' from file.";

	public static final int ERROR_CODE_DAO_69						= 7069;
	public static final String ERROR_MESSAGE_DAO_69_1				= "Failed to execute select files query '";
	public static final String ERROR_MESSAGE_DAO_69_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_70						= 7070;
	public static final String ERROR_MESSAGE_DAO_70					= "The select files query did not return any data.";

	public static final int ERROR_CODE_DAO_71						= 7071;
	public static final String ERROR_MESSAGE_DAO_71					= "Only one of the variables (ids, roles, and username) can be used in the same select files query.";

	public static final int ERROR_CODE_DAO_72						= 7072;
	public static final String ERROR_MESSAGE_DAO_72					= "Failed to retrieve the select file query file.";

	public static final int ERROR_CODE_DAO_73						= 7073;
	public static final String ERROR_MESSAGE_DAO_73					= "The select file query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_74						= 7074;
	public static final String ERROR_MESSAGE_DAO_74					= "The select file query expression is null.";

	public static final int ERROR_CODE_DAO_75						= 7075;
	public static final String ERROR_MESSAGE_DAO_75					= "Sorry, the file you are trying to download does not exist.";

	public static final int ERROR_CODE_DAO_76						= 7076;
	public static final String ERROR_MESSAGE_DAO_76					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_77						= 7077;
	public static final String ERROR_MESSAGE_DAO_77_1				= "Failed to retrieve select file query '";
	public static final String ERROR_MESSAGE_DAO_77_2				= "' from file.";

	public static final int ERROR_CODE_DAO_78						= 7078;
	public static final String ERROR_MESSAGE_DAO_78_1				= "Failed to execute select file query '";
	public static final String ERROR_MESSAGE_DAO_78_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_79						= 7079;
	public static final String ERROR_MESSAGE_DAO_79					= "Method not supported.";

	public static final int ERROR_CODE_DAO_80						= 7080;
	public static final String ERROR_MESSAGE_DAO_80					= "Failed to retrieve the select files query file.";

	public static final int ERROR_CODE_DAO_81						= 7081;
	public static final String ERROR_MESSAGE_DAO_81					= "The select files query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_82						= 7082;
	public static final String ERROR_MESSAGE_DAO_82					= "The select files query expression is null.";

	public static final int ERROR_CODE_DAO_83						= 7083;
	public static final String ERROR_MESSAGE_DAO_83					= "The select files query did not return any data.";

	public static final int ERROR_CODE_DAO_84						= 7084;
	public static final String ERROR_MESSAGE_DAO_84_1				= "Failed to retrieve select files query '";
	public static final String ERROR_MESSAGE_DAO_84_2				= "' from file.";

	public static final int ERROR_CODE_DAO_85						= 7085;
	public static final String ERROR_MESSAGE_DAO_85_1				= "Failed to execute select files query '";
	public static final String ERROR_MESSAGE_DAO_85_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_86						= 7086;
	public static final String ERROR_MESSAGE_DAO_86					= "Failed to retrieve the select file query file.";

	public static final int ERROR_CODE_DAO_87						= 7087;
	public static final String ERROR_MESSAGE_DAO_87					= "The select file query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_88						= 7088;
	public static final String ERROR_MESSAGE_DAO_88					= "The select file query expression is null.";

	public static final int ERROR_CODE_DAO_89						= 7089;
	public static final String ERROR_MESSAGE_DAO_89					= "Sorry, the file you are trying to download does not exist.";

	public static final int ERROR_CODE_DAO_90						= 7090;
	public static final String ERROR_MESSAGE_DAO_90_1				= "Failed to retrieve select file query '";
	public static final String ERROR_MESSAGE_DAO_90_2				= "' from file.";

	public static final int ERROR_CODE_DAO_91						= 7091;
	public static final String ERROR_MESSAGE_DAO_91_1				= "Failed to execute select file query '";
	public static final String ERROR_MESSAGE_DAO_91_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_92						= 7092;
	public static final String ERROR_MESSAGE_DAO_92					= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_93						= 7093;
	public static final String ERROR_MESSAGE_DAO_93					= "Method not supported.";

	public static final int ERROR_CODE_DAO_94						= 7094;
	public static final String ERROR_MESSAGE_DAO_94					= "Failed to retrieve the insert files query file.";

	public static final int ERROR_CODE_DAO_95						= 7095;
	public static final String ERROR_MESSAGE_DAO_95					= "The insert files query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_96						= 7096;
	public static final String ERROR_MESSAGE_DAO_96					= "The insert files query expression is null.";

	public static final int ERROR_CODE_DAO_97						= 7097;
	public static final String ERROR_MESSAGE_DAO_97					= "The insert files query did not return any data.";

	public static final int ERROR_CODE_DAO_98						= 7098;
	public static final String ERROR_MESSAGE_DAO_98					= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_99						= 7099;
	public static final String ERROR_MESSAGE_DAO_99_1				= "Failed to retrieve insert files query '";
	public static final String ERROR_MESSAGE_DAO_99_2				= "' from file.";

	public static final int ERROR_CODE_DAO_100						= 7100;
	public static final String ERROR_MESSAGE_DAO_100_1				= "Failed to execute insert files query '";
	public static final String ERROR_MESSAGE_DAO_100_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_101						= 7101;
	public static final String ERROR_MESSAGE_DAO_101				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_102						= 7102;
	public static final String ERROR_MESSAGE_DAO_102				= "Method not supported.";

	public static final int ERROR_CODE_DAO_103						= 7103;
	public static final String ERROR_MESSAGE_DAO_103				= "Failed to retrieve the delete files query file.";

	public static final int ERROR_CODE_DAO_104						= 7104;
	public static final String ERROR_MESSAGE_DAO_104				= "The delete files query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_105						= 7105;
	public static final String ERROR_MESSAGE_DAO_105				= "The delete files query expression is null.";

	public static final int ERROR_CODE_DAO_106						= 7106;
	public static final String ERROR_MESSAGE_DAO_106				= "The delete files query did not return any data.";

	public static final int ERROR_CODE_DAO_107						= 7107;
	public static final String ERROR_MESSAGE_DAO_107				= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_108						= 7108;
	public static final String ERROR_MESSAGE_DAO_108_1				= "Failed to retrieve delete files query '";
	public static final String ERROR_MESSAGE_DAO_108_2				= "' from file.";

	public static final int ERROR_CODE_DAO_109						= 7109;
	public static final String ERROR_MESSAGE_DAO_109_1				= "Failed to execute delete files query '";
	public static final String ERROR_MESSAGE_DAO_109_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_110						= 7110;
	public static final String ERROR_MESSAGE_DAO_110				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_111						= 7111;
	public static final String ERROR_MESSAGE_DAO_111				= "Method not supported.";

	public static final int ERROR_CODE_DAO_112						= 7112;
	public static final String ERROR_MESSAGE_DAO_112				= "Failed to retrieve the update files query file.";

	public static final int ERROR_CODE_DAO_113						= 7113;
	public static final String ERROR_MESSAGE_DAO_113				= "The update files query expression does not contain an XQuery expression.";

	public static final int ERROR_CODE_DAO_114						= 7114;
	public static final String ERROR_MESSAGE_DAO_114				= "The update files query expression is null.";

	public static final int ERROR_CODE_DAO_115						= 7115;
	public static final String ERROR_MESSAGE_DAO_115				= "The update files query did not return any data.";

	public static final int ERROR_CODE_DAO_116						= 7116;
	public static final String ERROR_MESSAGE_DAO_116				= "XML:DB Exception (error code: ";

	public static final int ERROR_CODE_DAO_117						= 7117;
	public static final String ERROR_MESSAGE_DAO_117_1				= "Failed to retrieve update files query '";
	public static final String ERROR_MESSAGE_DAO_117_2				= "' from file.";

	public static final int ERROR_CODE_DAO_118						= 7118;
	public static final String ERROR_MESSAGE_DAO_118_1				= "Failed to execute update files query '";
	public static final String ERROR_MESSAGE_DAO_118_2				= "' against files metadata data source.";

	public static final int ERROR_CODE_DAO_120						= 7120;
	public static final String ERROR_MESSAGE_DAO_120				= "Failed to execute query against widget realm.";

	public static final int ERROR_CODE_DAO_121						= 7121;
	public static final String ERROR_MESSAGE_DAO_121				= "Failed to execute query against widget realm.";

	public static final int ERROR_CODE_DAO_122						= 7122;
	public static final String ERROR_MESSAGE_DAO_122				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_123						= 7123;
	public static final String ERROR_MESSAGE_DAO_123				= "Failed to execute the REST method.";

	public static final int ERROR_CODE_DAO_124						= 7124;
	public static final String ERROR_MESSAGE_DAO_124				= "Failed to execute the REST method.";

	public static final int ERROR_CODE_DAO_125						= 7125;
	public static final String ERROR_MESSAGE_DAO_125				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_126						= 7126;
	public static final String ERROR_MESSAGE_DAO_126				= "Failed to execute the REST method.";

	public static final int ERROR_CODE_DAO_127						= 7127;
	public static final String ERROR_MESSAGE_DAO_127				= "Failed to execute query against widget data sources.";

	public static final int ERROR_CODE_DAO_128						= 7128;
	public static final String ERROR_MESSAGE_DAO_128				= "Failed to execute the REST method.";

	public static final int ERROR_CODE_DAO_129						= 7129;
	public static final String ERROR_MESSAGE_DAO_129				= "Failed to execute query against widget data sources.";

	public static final int ERROR_CODE_DAO_130						= 7130;
	public static final String ERROR_MESSAGE_DAO_130				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_131						= 7131;
	public static final String ERROR_MESSAGE_DAO_131				= "Failed to connect to data source.";

	public static final int ERROR_CODE_DAO_132						= 7132;
	public static final String ERROR_MESSAGE_DAO_132				= "Failed to connect to data source.";

	public static final int ERROR_CODE_DAO_133						= 7133;
	public static final String ERROR_MESSAGE_DAO_133				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_134						= 7134;
	public static final String ERROR_MESSAGE_DAO_134				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_135						= 7135;
	public static final String ERROR_MESSAGE_DAO_135				= "Failed to lookup data source.";

	public static final int ERROR_CODE_DAO_136						= 7136;
	public static final String ERROR_MESSAGE_DAO_136				= "Only one of the variables (ids, roles, and username) can be used in the same select files query.";

	
	// XFormsDBQuerySelectHandler errors
	public static final int ERROR_CODE_XFORMSDBQUERYSELECTHANDLER_1					= 8001;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYSELECTHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBQUERYSELECTHANDLER_2					= 8002;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYSELECTHANDLER_2			= "Failed to handle the SELECT action of the XFormsDB query request.";

	
	// XFormsDBQuerySelect4UpdateHandler errors
	public static final int ERROR_CODE_XFORMSDBQUERYSELECT4UPDATEHANDLER_1			= 9001;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYSELECT4UPDATEHANDLER_1	= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBQUERYSELECT4UPDATEHANDLER_2			= 9002;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYSELECT4UPDATEHANDLER_2	= "Failed to handle the SELECT4UPDATE action of the XFormsDB query request.";

	
	// XFormsDBQueryUpdateHandler errors
	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_1					= 10001;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_2					= 10002;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_2			= "Failed to retrieve the select4update (stored) query result.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_3					= 10003;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_3			= "Failed to retrieve the transformer.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_9					= 10009;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_9			= "Failed to update/replace the select4update (stored) query result.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_10				= 10010;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_10			= "Failed to handle the UPDATE action of the XFormsDB query request.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_11				= 10011;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_11_1		= "Failed to execute query '";
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_11_2		= "' due to update conflicts.";

	public static final int ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_13				= 10013;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_13			= "The contents of the <xformsdb:attachment> element is empty.";

	
	// XFormsDBStateGetHandler errors
	public static final int ERROR_CODE_XFORMSDBSTATEGETHANDLER_2					= 11002;
	public static final String ERROR_MESSAGE_XFORMSDBSTATEGETHANDLER_2				= "Failed to retrieve the web application's state from XFormsDB.";

	
	// XFormsDBStateSetHandler errors
	public static final int ERROR_CODE_XFORMSDBSTATESETHANDLER_1					= 12001;
	public static final String ERROR_MESSAGE_XFORMSDBSTATESETHANDLER_1				= "Failed to store the web application's state into XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBSTATESETHANDLER_2					= 12002;
	public static final String ERROR_MESSAGE_XFORMSDBSTATESETHANDLER_2				= "The web application's state is null.";

	
	// XFormsDBLoginHandler error
	public static final int ERROR_CODE_XFORMSDBLOGINHANDLER_2						= 13001;
	public static final String ERROR_MESSAGE_XFORMSDBLOGINHANDLER_2					= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBLOGINHANDLER_3						= 13003;
	public static final String ERROR_MESSAGE_XFORMSDBLOGINHANDLER_3					= "Failed to log in the user to XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBLOGINHANDLER_4						= 13004;
	public static final String ERROR_MESSAGE_XFORMSDBLOGINHANDLER_4					= "Failed to create the query.";

	
	// XFormsDBLogoutHandler errors
	public static final int ERROR_CODE_XFORMSDBLOGOUTHANDLER_1						= 14001;
	public static final String ERROR_MESSAGE_XFORMSDBLOGOUTHANDLER_1				= "Failed to log out the user from XFormsDB.";


	// XFormsDBUserHandler errors
	public static final int ERROR_CODE_XFORMSDBUSERHANDLER_1						= 15001;
	public static final String ERROR_MESSAGE_XFORMSDBUSERHANDLER_1					= "XFormsDB does not hold the logged in user.";

	public static final int ERROR_CODE_XFORMSDBUSERHANDLER_2						= 15002;
	public static final String ERROR_MESSAGE_XFORMSDBUSERHANDLER_2					= "Failed to retrieve the logged in user from XFormsDB.";

	
	// XFormsDBQueryAllHandler errors
	public static final int ERROR_CODE_XFORMSDBQUERYALLHANDLER_1					= 16001;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYALLHANDLER_1				= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBQUERYALLHANDLER_2					= 16002;
	public static final String ERROR_MESSAGE_XFORMSDBQUERYALLHANDLER_2				= "Failed to handle the ALL action of the XFormsDB query request.";

	
	// XFormsDBWidgetQuerySelectHandler errors
	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYSELECTHANDLER_1			= 37001;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYSELECTHANDLER_1		= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYSELECTHANDLER_2			= 37002;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYSELECTHANDLER_2		= "Failed to handle the SELECT action of the XFormsDB widget query request.";

	
	// XFormsDBWidgetQuerySelect4UpdateHandler errors
	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYSELECT4UPDATEHANDLER_1		= 38001;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYSELECT4UPDATEHANDLER_1	= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYSELECT4UPDATEHANDLER_2		= 38002;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYSELECT4UPDATEHANDLER_2	= "Failed to handle the SELECT4UPDATE action of the XFormsDB widget query request.";

	
	// XFormsDBWidgetQueryUpdateHandler errors
	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_1				= 39001;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_2				= 39002;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_2			= "Failed to retrieve the select4update (stored) widget query result.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_3				= 39003;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_3			= "Failed to retrieve the transformer.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_9				= 39009;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_9			= "Failed to update/replace the select4update (stored) widget query result.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_10				= 39010;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_10		= "Failed to handle the UPDATE action of the XFormsDB widget query request.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11				= 39011;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11_1		= "Failed to execute widget query '";
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11_2		= "' due to update conflicts.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_13				= 39013;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_13		= "The contents of the <xformsdb:attachment> element is empty.";

	
	// XFormsDBWidgetQueryAllHandler errors
	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYALLHANDLER_1				= 35001;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYALLHANDLER_1		= "Failed to retrieve the XFormsDB DAO of the appropriate DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBWIDGETQUERYALLHANDLER_2				= 35002;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETQUERYALLHANDLER_2		= "Failed to handle the ALL action of the XFormsDB widget query request.";


	// XFormsDBFileSelectHandler error
	public static final int ERROR_CODE_XFORMSDBFILESELECTHANDLER_1					= 17001;
	public static final String ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the files metadata DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBFILESELECTHANDLER_2					= 17002;
	public static final String ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_2			= "Failed to handle the SELECT action of the XFormsDB file request.";

	public static final int ERROR_CODE_XFORMSDBFILESELECTHANDLER_3					= 17003;
	public static final String ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_3			= "Failed to handle the SELECT action of the XFormsDB file request.";

	
	// XFormsDBFileInsertHandler error
	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_1					= 30001;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the files metadata DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_2					= 30002;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_2			= "Failed to handle the INSERT action of the XFormsDB file request.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_3					= 30003;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_3			= "Failed to write the files to XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_4					= 30004;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_4			= "Missing mandatory <xformsdb:insert> root element from the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_5					= 30005;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_5			= "The <xformsdb:insert> root element of the submitted attachment instance contains illegal child elements.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_6					= 30006;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_6			= "Missing mandatory attribute 'displayname' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_7					= 30007;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_7			= "Missing mandatory attribute 'filename' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_8					= 30008;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_8			= "Missing mandatory attribute 'mediatype' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_9					= 30009;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_9			= "Missing mandatory attribute 'filesize' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_10					= 30010;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_10			= "Missing mandatory attribute 'roles' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_11					= 30011;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_11			= "Failed to write the files to XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_12					= 30012;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_12			= "Missing mandatory attribute 'creator' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEINSERTHANDLER_13					= 30013;
	public static final String ERROR_MESSAGE_XFORMSDBFILEINSERTHANDLER_13			= "Missing mandatory attribute 'comment' from the <xformsdb:file> child element of the submitted attachment instance.";

	
	// XFormsDBFileDeleteHandler error
	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_1					= 31001;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the files metadata DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_2					= 31002;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_2			= "Failed to handle the DELETE action of the XFormsDB file request.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_3					= 31003;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_3			= "Failed to update the XOM document.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_4					= 31004;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_4			= "Failed to delete the files from XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_5					= 31005;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_5			= "Missing mandatory <xformsdb:delete> root element from the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_6					= 31006;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_6			= "The <xformsdb:delete> root element of the submitted attachment instance contains illegal child elements.";

	public static final int ERROR_CODE_XFORMSDBFILEDELETEHANDLER_7					= 31007;
	public static final String ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_7			= "Missing mandatory attribute 'id' from the <xformsdb:file> child element of the submitted attachment instance.";


	// XFormsDBFileUpdateHandler error
	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_1					= 32001;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the files metadata DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_2					= 32002;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_2			= "Failed to handle the UPDATE action of the XFormsDB file request.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_3					= 32003;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_3			= "Missing mandatory <xformsdb:update> root element from the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_4					= 32004;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_4			= "The <xformsdb:update> root element of the submitted attachment instance contains illegal child elements.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_5					= 32005;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_5			= "Missing mandatory attribute 'id' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_6					= 32006;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_6			= "Failed to update/overwrite the files to XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_7					= 32007;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_7			= "Missing mandatory attribute 'download' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_8					= 32008;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_8			= "Missing mandatory attribute 'lastmodified' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_9					= 32009;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_9			= "Missing mandatory attribute 'displayname' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_10					= 32010;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_10			= "Missing mandatory attribute 'filename' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_11					= 32011;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_11			= "Missing mandatory attribute 'mediatype' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_12					= 32012;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_12			= "Missing mandatory attribute 'filesize' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_13					= 32013;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_13			= "Missing mandatory attribute 'roles' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_14					= 32014;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_14			= "Failed to update files to XFormsDB because one of the files to be updated does not exist anymore.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_15					= 32015;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_15			= "Failed to update/overwrite the files to XFormsDB.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_16					= 32016;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_16			= "Missing mandatory attribute 'creator' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_17					= 32017;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_17			= "Missing mandatory attribute 'created' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_18					= 32018;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_18			= "Missing mandatory attribute 'comment' from the <xformsdb:file> child element of the submitted attachment instance.";

	public static final int ERROR_CODE_XFORMSDBFILEUPDATEHANDLER_19					= 32019;
	public static final String ERROR_MESSAGE_XFORMSDBFILEUPDATEHANDLER_19			= "Missing mandatory attribute 'lastmodifier' from the <xformsdb:file> child element of the submitted attachment instance.";

	
	// XFormsDBCookieHandler errors
	public static final int ERROR_CODE_XFORMSDBCOOKIEHANDLER_1						= 33001;
	public static final String ERROR_MESSAGE_XFORMSDBCOOKIEHANDLER_1				= "Failed to check browser support for cookies.";

	
	// XFormsDBWidgetLoginHandler error
	public static final int ERROR_CODE_XFORMSDBWIDGETLOGINHANDLER_1					= 34001;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETLOGINHANDLER_1			= "Failed to retrieve the XFormsDB DAO of the default DAO factory implementation.";

	public static final int ERROR_CODE_XFORMSDBWIDGETLOGINHANDLER_2					= 34002;
	public static final String ERROR_MESSAGE_XFORMSDBWIDGETLOGINHANDLER_2			= "Failed to log in the user to XFormsDB widget.";

	
	// Initialization errors
	public static final int ERROR_CODE_INITIALIZATION_1				= 18001;

	public static final int ERROR_CODE_INITIALIZATION_2				= 18002;
	public static final String ERROR_MESSAGE_INITIALIZATION_2		= "Failed to initialize the XFormsDB Framework.";

	public static final int ERROR_CODE_INITIALIZATION_3				= 18003;
	public static final String ERROR_MESSAGE_INITIALIZATION_3		= "Failed to initialize the data ID attribute of a data source.";

	public static final int ERROR_CODE_INITIALIZATION_4				= 18004;
	public static final String ERROR_MESSAGE_INITIALIZATION_4		= "Failed to set the realms.";

	public static final int ERROR_CODE_INITIALIZATION_5				= 18005;
	public static final String ERROR_MESSAGE_INITIALIZATION_5		= "Failed to set the DAO factory implementations.";

	public static final int ERROR_CODE_INITIALIZATION_6				= 18006;
	public static final String ERROR_MESSAGE_INITIALIZATION_6		= "Failed to add the XFormsDB MIME mappings to the Web application file MIME mappings.";

	public static final int ERROR_CODE_INITIALIZATION_7				= 18007;
	public static final String ERROR_MESSAGE_INITIALIZATION_7		= "Failed to remove existing XFormsDB MIME mappings from the Web application file MIME mappings.";

	public static final int ERROR_CODE_INITIALIZATION_8				= 18008;
	public static final String ERROR_MESSAGE_INITIALIZATION_8		= "Failed to parse the XFormsDB configuration file.";

	public static final int ERROR_CODE_INITIALIZATION_9				= 18009;
	public static final String ERROR_MESSAGE_INITIALIZATION_9		= "Failed to retrieve the content of the XFormsDB configuration file as an input stream.";

	public static final int ERROR_CODE_INITIALIZATION_10			= 18010;
	public static final String ERROR_MESSAGE_INITIALIZATION_10		= "Failed to retrieve the XFormsDB configuration file path.";

	public static final int ERROR_CODE_INITIALIZATION_11			= 18011;
	public static final String ERROR_MESSAGE_INITIALIZATION_11		= "Failed to parse the Web application file.";

	public static final int ERROR_CODE_INITIALIZATION_12			= 18012;
	public static final String ERROR_MESSAGE_INITIALIZATION_12		= "Failed to retrieve the content of the Web application file as an input stream.";

	public static final int ERROR_CODE_INITIALIZATION_13			= 18013;
	public static final String ERROR_MESSAGE_INITIALIZATION_13		= "Failed to retrieve the Web application file path.";

	public static final int ERROR_CODE_INITIALIZATION_14			= 18014;
	public static final String ERROR_MESSAGE_INITIALIZATION_14		= "Failed to update the XFormsDB configuration file (conf.xml).";

	public static final int ERROR_CODE_INITIALIZATION_15			= 18015;
	public static final String ERROR_MESSAGE_INITIALIZATION_15_1	= "The files folder path (";
	public static final String ERROR_MESSAGE_INITIALIZATION_15_2	= ") contains one or more of the following illegal characters: \":\".";

	public static final int ERROR_CODE_INITIALIZATION_16			= 18016;
	public static final String ERROR_MESSAGE_INITIALIZATION_16_1	= "Failed to access the files folder path (";
	public static final String ERROR_MESSAGE_INITIALIZATION_16_2	= ").";

	public static final int ERROR_CODE_INITIALIZATION_17			= 18017;
	public static final String ERROR_MESSAGE_INITIALIZATION_17		= "Failed to retrieve and initialize the value of the server URI encoding.";

	
	// Transformation errors
	public static final int ERROR_CODE_TRANSFORMATION_1				= 19001;
	public static final String ERROR_MESSAGE_TRANSFORMATION_1		= "Failed to execute the XSLT transformation.";

	public static final int ERROR_CODE_TRANSFORMATION_2				= 19002;

	public static final int ERROR_CODE_TRANSFORMATION_3				= 19003;
	public static final String ERROR_MESSAGE_TRANSFORMATION_3		= "Failed to transform the <xformsdb:submission> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_4				= 19004;

	public static final int ERROR_CODE_TRANSFORMATION_5				= 19005;

	public static final int ERROR_CODE_TRANSFORMATION_6				= 19006;
	public static final String ERROR_MESSAGE_TRANSFORMATION_6		= "Missing mandatory attribute 'id' from the <xformsdb:submission> element.";

	public static final int ERROR_CODE_TRANSFORMATION_7				= 19007;

	public static final int ERROR_CODE_TRANSFORMATION_8				= 19008;

	public static final int ERROR_CODE_TRANSFORMATION_9				= 19009;

	public static final int ERROR_CODE_TRANSFORMATION_10			= 19010;
	public static final String ERROR_MESSAGE_TRANSFORMATION_10		= "Failed to transform the <xformsdb:instance> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_11			= 19011;

	public static final int ERROR_CODE_TRANSFORMATION_12			= 19012;

	public static final int ERROR_CODE_TRANSFORMATION_13			= 19013;
	public static final String ERROR_MESSAGE_TRANSFORMATION_13		= "Missing expression (either 'resource' attribute or inline text should be provided) from the <xformsdb:expression> element.";

	public static final int ERROR_CODE_TRANSFORMATION_14			= 19014;
	public static final String ERROR_MESSAGE_TRANSFORMATION_14		= "Failed to transform the <xformsdb:query> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_15			= 19015;
	public static final String ERROR_MESSAGE_TRANSFORMATION_15		= "Missing mandatory attribute 'id' from the <xformsdb:instance> element.";

	public static final int ERROR_CODE_TRANSFORMATION_16			= 19016;
	public static final String ERROR_MESSAGE_TRANSFORMATION_16		= "Missing mandatory attribute 'id' from the <xformsdb:instance> element.";
	
	public static final int ERROR_CODE_TRANSFORMATION_17			= 19017;
	public static final String ERROR_MESSAGE_TRANSFORMATION_17		= "Failed to transform the <xforms:load> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_18			= 19018;

	public static final int ERROR_CODE_TRANSFORMATION_19			= 19019;

	public static final int ERROR_CODE_TRANSFORMATION_20			= 19020;
	public static final String ERROR_MESSAGE_TRANSFORMATION_20		= "Missing mandatory attribute 'id' from the <xformsdb:instance> element.";

	public static final int ERROR_CODE_TRANSFORMATION_21			= 19021;
	public static final String ERROR_MESSAGE_TRANSFORMATION_21		= "Failed to transform the <xformsdb:login> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_22			= 19022;
	public static final String ERROR_MESSAGE_TRANSFORMATION_22		= "Missing mandatory attribute 'id' from the <xformsdb:instance> element.";

	public static final int ERROR_CODE_TRANSFORMATION_23			= 19023;
	public static final String ERROR_MESSAGE_TRANSFORMATION_23		= "Missing mandatory attribute 'id' from the <xformsdb:instance> element.";

	public static final int ERROR_CODE_TRANSFORMATION_24			= 19024;
	public static final String ERROR_MESSAGE_TRANSFORMATION_24		= "Failed to transform the <xforms:submission> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_25			= 19025;

	public static final int ERROR_CODE_TRANSFORMATION_26			= 19026;

	public static final int ERROR_CODE_TRANSFORMATION_27			= 19027;
	public static final String ERROR_MESSAGE_TRANSFORMATION_27		= "Failed to transform the <xhtml:meta> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_28			= 19028;

	public static final int ERROR_CODE_TRANSFORMATION_29			= 19029;

	public static final int ERROR_CODE_TRANSFORMATION_30			= 19030;
	public static final String ERROR_MESSAGE_TRANSFORMATION_30		= "Failed to transform the <xhtml:a> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_31			= 19031;

	public static final int ERROR_CODE_TRANSFORMATION_32			= 19032;
	
	public static final int ERROR_CODE_TRANSFORMATION_33			= 19033;

	public static final int ERROR_CODE_TRANSFORMATION_34			= 19034;

	public static final int ERROR_CODE_TRANSFORMATION_35			= 19035;

	public static final int ERROR_CODE_TRANSFORMATION_36			= 19036;
	public static final String ERROR_MESSAGE_TRANSFORMATION_36		= "Invalid markup in the <xformsdb:submission> elements of the XFormsDB document.";

	public static final int ERROR_CODE_TRANSFORMATION_37			= 19037;
	public static final String ERROR_MESSAGE_TRANSFORMATION_37		= "Invalid markup in the <xformsdb:instance> elements of the XFormsDB document.";

	public static final int ERROR_CODE_TRANSFORMATION_38			= 19038;
	public static final String ERROR_MESSAGE_TRANSFORMATION_38		= "Invalid markup in the <xforms:load> elements of the XFormsDB document.";

	public static final int ERROR_CODE_TRANSFORMATION_39			= 19039;
	public static final String ERROR_MESSAGE_TRANSFORMATION_39		= "Failed to retrieve the real requested file path.";

	public static final int ERROR_CODE_TRANSFORMATION_40			= 19040;
	public static final String ERROR_MESSAGE_TRANSFORMATION_40		= "Failed to retrieve the XFormsDB handler.";

	public static final int ERROR_CODE_TRANSFORMATION_41			= 19041;
	public static final String ERROR_MESSAGE_TRANSFORMATION_41		= "Failed to retrieve the XFormsDB SAX parser.";

	public static final int ERROR_CODE_TRANSFORMATION_42			= 19042;
	public static final String ERROR_MESSAGE_TRANSFORMATION_42		= "Failed to parse the original XFormsDB document (include).";

	public static final int ERROR_CODE_TRANSFORMATION_43			= 19043;
	public static final String ERROR_MESSAGE_TRANSFORMATION_43		= "Failed to retrieve the transformer.";

	public static final int ERROR_CODE_TRANSFORMATION_44			= 19044;
	public static final String ERROR_MESSAGE_TRANSFORMATION_44		= "Failed to transform the original XFormsDB document (include).";

	public static final int ERROR_CODE_TRANSFORMATION_45			= 19045;
	public static final String ERROR_MESSAGE_TRANSFORMATION_45		= "Failed to transform the secviewed XFormsDB document (extract).";

	public static final int ERROR_CODE_TRANSFORMATION_46			= 19046;

	public static final int ERROR_CODE_TRANSFORMATION_47			= 19047;

	public static final int ERROR_CODE_TRANSFORMATION_48			= 19048;
	public static final String ERROR_MESSAGE_TRANSFORMATION_48		= "Failed to build the XOM document object.";

	public static final int ERROR_CODE_TRANSFORMATION_49			= 19049;
	public static final String ERROR_MESSAGE_TRANSFORMATION_49		= "Failed to update the XOM document object (<xformsdb:submissions> element).";

	public static final int ERROR_CODE_TRANSFORMATION_50			= 19050;
	public static final String ERROR_MESSAGE_TRANSFORMATION_50		= "Failed to update the XOM document object (<xformsdb:instances> element).";

	public static final int ERROR_CODE_TRANSFORMATION_51			= 19051;
	public static final String ERROR_MESSAGE_TRANSFORMATION_51		= "Failed to update the XOM document object (<xforms:loads> element).";

	public static final int ERROR_CODE_TRANSFORMATION_52			= 19052;
	public static final String ERROR_MESSAGE_TRANSFORMATION_52		= "Failed to update the XOM document object (<xforms:submissions> element).";

	public static final int ERROR_CODE_TRANSFORMATION_53			= 19053;
	public static final String ERROR_MESSAGE_TRANSFORMATION_53		= "Failed to update the XOM document object (<xhtml:metas> element).";

	public static final int ERROR_CODE_TRANSFORMATION_54			= 19054;
	public static final String ERROR_MESSAGE_TRANSFORMATION_54		= "Failed to update the XOM document object (<xhtml:as> element).";

	public static final int ERROR_CODE_TRANSFORMATION_55			= 19055;
	public static final String ERROR_MESSAGE_TRANSFORMATION_55		= "Failed to add the <xformsdb:query> elements to the XFormsDB query manager.";

	public static final int ERROR_CODE_TRANSFORMATION_56			= 19056;
	public static final String ERROR_MESSAGE_TRANSFORMATION_56		= "Failed to add the <xformsdb:login> elements to the XFormsDB login manager.";

	public static final int ERROR_CODE_TRANSFORMATION_57			= 19057;
	public static final String ERROR_MESSAGE_TRANSFORMATION_57		= "Failed to transform the included XFormsDB document (xforms).";

	public static final int ERROR_CODE_TRANSFORMATION_58			= 19058;
	public static final String ERROR_MESSAGE_TRANSFORMATION_58		= "Failed to transform the XFormsDB document.";

	public static final int ERROR_CODE_TRANSFORMATION_59			= 19059;
	public static final String ERROR_MESSAGE_TRANSFORMATION_59		= "Failed to retrieve request parameters.";

	public static final int ERROR_CODE_TRANSFORMATION_60			= 19060;
	public static final String ERROR_MESSAGE_TRANSFORMATION_60		= "Failed to retrieve the logged in <xformsdb:user> element from the XFormsDB user manager.";

	public static final int ERROR_CODE_TRANSFORMATION_61			= 19061;
	public static final String ERROR_MESSAGE_TRANSFORMATION_61		= "Failed to retrieve the web application's <xformsdb:state> element from the XFormsDB state manager.";

	public static final int ERROR_CODE_TRANSFORMATION_62			= 19062;
	public static final String ERROR_MESSAGE_TRANSFORMATION_62		= "Missing mandatory request parameter 'statetype' or illegal 'statetype' request parameter value. Valid values are: get and set.";

	public static final int ERROR_CODE_TRANSFORMATION_63			= 19063;
	public static final String ERROR_MESSAGE_TRANSFORMATION_63		= "Failed to transform the <xformsdb:state> element.";

	public static final int ERROR_CODE_TRANSFORMATION_64			= 19064;
	public static final String ERROR_MESSAGE_TRANSFORMATION_64		= "Failed to transform the <xformsdb:user> element.";

	public static final int ERROR_CODE_TRANSFORMATION_65			= 19065;
	public static final String ERROR_MESSAGE_TRANSFORMATION_65		= "Failed to transform the <xformsdb:login> element.";

	public static final int ERROR_CODE_TRANSFORMATION_66			= 19066;
	public static final String ERROR_MESSAGE_TRANSFORMATION_66		= "Failed to transform the <xformsdb:logout> element.";

	public static final int ERROR_CODE_TRANSFORMATION_67			= 19067;
	public static final String ERROR_MESSAGE_TRANSFORMATION_67		= "Failed to transform the <xformsdb:query> element.";

	public static final int ERROR_CODE_TRANSFORMATION_68			= 19068;
	public static final String ERROR_MESSAGE_TRANSFORMATION_68		= "XFormsDB does not hold the logged in user.";

	public static final int ERROR_CODE_TRANSFORMATION_69			= 19069;
	public static final String ERROR_MESSAGE_TRANSFORMATION_69		= "Failed to retrieve the logged in user from XFormsDB.";

	public static final int ERROR_CODE_TRANSFORMATION_70			= 19070;
	public static final String ERROR_MESSAGE_TRANSFORMATION_70		= "Failed to transform the <xformsdb:query> element.";

	public static final int ERROR_CODE_TRANSFORMATION_71			= 19071;
	public static final String ERROR_MESSAGE_TRANSFORMATION_71		= "Missing mandatory request parameter 'expressiontype' or illegal 'expressiontype' request parameter value. Valid values are: all, select, select4update, and update.";

	public static final int ERROR_CODE_TRANSFORMATION_72			= 19072;

	public static final int ERROR_CODE_TRANSFORMATION_73			= 19073;
	public static final String ERROR_MESSAGE_TRANSFORMATION_73		= "Failed to add the <xformsdb:file> elements to the XFormsDB file manager.";

	public static final int ERROR_CODE_TRANSFORMATION_74			= 19074;
	public static final String ERROR_MESSAGE_TRANSFORMATION_74		= "Failed to transform the <xformsdb:file> element.";

	public static final int ERROR_CODE_TRANSFORMATION_75			= 19075;
	public static final String ERROR_MESSAGE_TRANSFORMATION_75		= "Missing mandatory request parameter 'filetype' or illegal 'filetype' request parameter value. Valid values are: select, update, insert, and delete.";

	public static final int ERROR_CODE_TRANSFORMATION_76			= 19076;
	public static final String ERROR_MESSAGE_TRANSFORMATION_76		= "XFormsDB does not hold the logged in user.";

	public static final int ERROR_CODE_TRANSFORMATION_77			= 19077;
	public static final String ERROR_MESSAGE_TRANSFORMATION_77		= "Failed to retrieve the roles of the logged in user from XFormsDB.";

	public static final int ERROR_CODE_TRANSFORMATION_78			= 19078;
	public static final String ERROR_MESSAGE_TRANSFORMATION_78		= "Failed to retrieve request headers.";

	public static final int ERROR_CODE_TRANSFORMATION_79			= 19079;
	public static final String ERROR_MESSAGE_TRANSFORMATION_79		= "Failed to retrieve request base URI.";

	public static final int ERROR_CODE_TRANSFORMATION_80			= 19080;
	public static final String ERROR_MESSAGE_TRANSFORMATION_80		= "Failed to transform the <xformsdb:cookie> element.";

	public static final int ERROR_CODE_TRANSFORMATION_82			= 19082;
	public static final String ERROR_MESSAGE_TRANSFORMATION_82		= "Failed to auto log in the user to XFormsDB.";

	public static final int ERROR_CODE_TRANSFORMATION_84			= 19084;
	public static final String ERROR_MESSAGE_TRANSFORMATION_84		= "Failed to add the <xformsdb:widgetquery> elements to the XFormsDB widget query manager.";

	public static final int ERROR_CODE_TRANSFORMATION_85			= 19085;
	public static final String ERROR_MESSAGE_TRANSFORMATION_85		= "Failed to retrieve the logged in user from XFormsDB.";

	public static final int ERROR_CODE_TRANSFORMATION_86			= 19086;
	public static final String ERROR_MESSAGE_TRANSFORMATION_86		= "Missing mandatory request parameter 'expressiontype' or illegal 'expressiontype' request parameter value. Valid values are: all, select, select4update, and update.";

	public static final int ERROR_CODE_TRANSFORMATION_87			= 19087;
	public static final String ERROR_MESSAGE_TRANSFORMATION_87		= "Failed to transform the <xformsdb:widgetquery> element.";

	public static final int ERROR_CODE_TRANSFORMATION_88			= 19088;
	public static final String ERROR_MESSAGE_TRANSFORMATION_88		= "Failed to transform the <xformsdb:widgetquery> element.";

	public static final int ERROR_CODE_TRANSFORMATION_89			= 19089;
	public static final String ERROR_MESSAGE_TRANSFORMATION_89		= "Failed to update the XOM document object (<xforms:models> element).";

	public static final int ERROR_CODE_TRANSFORMATION_90			= 19090;
	public static final String ERROR_MESSAGE_TRANSFORMATION_90		= "Failed to transform the <xforms:model> elements.";

	public static final int ERROR_CODE_TRANSFORMATION_91			= 19091;
	public static final String ERROR_MESSAGE_TRANSFORMATION_91		= "Failed to transform the included XFormsDB document (secview).";

	public static final int ERROR_CODE_TRANSFORMATION_92			= 19092;
	public static final String ERROR_MESSAGE_TRANSFORMATION_92		= "Failed to retrieve the username of the logged in user from XFormsDB.";

	
	// ErrorWriter errors
	public static final int ERROR_CODE_ERRORWRITER_1				= 20001;
	public static final String ERROR_MESSAGE_ERRORWRITER_1			= "Failed to write the error in the XHTML format.";
	
	public static final int ERROR_CODE_ERRORWRITER_2				= 20002;
	public static final String ERROR_MESSAGE_ERRORWRITER_2			= "Failed to write the error in the XML format.";
	
	public static final int ERROR_CODE_ERRORWRITER_3				= 20003;
	public static final String ERROR_MESSAGE_ERRORWRITER_3			= "Failed to write the error in the XForms format.";

	public static final int ERROR_CODE_ERRORWRITER_4				= 20004;
	public static final String ERROR_MESSAGE_ERRORWRITER_4			= "Failed to the print the additional stack trace for the error to string.";

	public static final int ERROR_CODE_ERRORWRITER_5				= 20005;
	public static final String ERROR_MESSAGE_ERRORWRITER_5			= "Failed to write the error.";

	
	// Manager errors
	public static final int ERROR_CODE_MANAGER_1					= 21001;
	public static final String ERROR_MESSAGE_MANAGER_1				= "Failed to retrieve the active <xformsdb:login> element from the session.";

	public static final int ERROR_CODE_MANAGER_2					= 21002;
	public static final String ERROR_MESSAGE_MANAGER_2				= "Failed to add the active <xformsdb:login> elements into the session.";

	public static final int ERROR_CODE_MANAGER_3					= 21003;
	public static final String ERROR_MESSAGE_MANAGER_3				= "Failed to set the active <xformsdb:login> elements into the session.";

	public static final int ERROR_CODE_MANAGER_4					= 21004;
	public static final String ERROR_MESSAGE_MANAGER_4				= "Failed to retrieve the active <xformsdb:login> elements from the session.";

	public static final int ERROR_CODE_MANAGER_5					= 21005;
	public static final String ERROR_MESSAGE_MANAGER_5				= "Failed to retrieve the active <xformsdb:query> elements from the session.";

	public static final int ERROR_CODE_MANAGER_6					= 21006;
	public static final String ERROR_MESSAGE_MANAGER_6				= "Failed to set the active <xformsdb:query> elements into the session.";

	public static final int ERROR_CODE_MANAGER_7					= 21007;
	public static final String ERROR_MESSAGE_MANAGER_7				= "Failed to retrieve the SELECT4UPDATE <xformsdb:query> results from the session.";

	public static final int ERROR_CODE_MANAGER_8					= 21008;
	public static final String ERROR_MESSAGE_MANAGER_8				= "Failed to set the SELECT4UPDATE <xformsdb:query> results into the session.";

	public static final int ERROR_CODE_MANAGER_9					= 21009;
	public static final String ERROR_MESSAGE_MANAGER_9				= "Failed to add the active <xformsdb:query> elements into the session.";

	public static final int ERROR_CODE_MANAGER_10					= 21010;
	public static final String ERROR_MESSAGE_MANAGER_10				= "Failed to retrieve the active <xformsdb:query> element from the session.";

	public static final int ERROR_CODE_MANAGER_11					= 21011;
	public static final String ERROR_MESSAGE_MANAGER_11				= "Failed to add the SELECT4UPDATE <xformsdb:query> results into the session.";

	public static final int ERROR_CODE_MANAGER_12					= 21012;
	public static final String ERROR_MESSAGE_MANAGER_12				= "Failed to retrieve the SELECT4UPDATE <xformsdb:query> result from the session.";

	public static final int ERROR_CODE_MANAGER_13					= 21013;
	public static final String ERROR_MESSAGE_MANAGER_13				= "Failed to remove all cached transformers from memory.";

	public static final int ERROR_CODE_MANAGER_14					= 21014;
	public static final String ERROR_MESSAGE_MANAGER_14				= "Failed to remove the cached transformer from memory.";

	public static final int ERROR_CODE_MANAGER_15					= 21015;
	public static final String ERROR_MESSAGE_MANAGER_15				= "Failed to retrieve the cached transformer from memory.";

	public static final int ERROR_CODE_MANAGER_16					= 21016;
	public static final String ERROR_MESSAGE_MANAGER_16				= "Failed to set the logged in <xformsdb:user> element into the session.";

	public static final int ERROR_CODE_MANAGER_17					= 21017;
	public static final String ERROR_MESSAGE_MANAGER_17				= "Failed to remove the logged in <xformsdb:user> element from the session.";

	public static final int ERROR_CODE_MANAGER_18					= 21018;
	public static final String ERROR_MESSAGE_MANAGER_18				= "Failed to retrieve the logged in <xformsdb:user> element from the session.";

	public static final int ERROR_CODE_MANAGER_19					= 21019;
	public static final String ERROR_MESSAGE_MANAGER_19				= "Failed to set the web application's <xformsdb:state> element into the session.";

	public static final int ERROR_CODE_MANAGER_20					= 21020;
	public static final String ERROR_MESSAGE_MANAGER_20				= "Failed to retrieve the web applications's <xformsdb:state> element from the session.";

	public static final int ERROR_CODE_MANAGER_21					= 21021;
	public static final String ERROR_MESSAGE_MANAGER_21				= "Failed to retrieve the active <xformsdb:file> element from the session.";

	public static final int ERROR_CODE_MANAGER_22					= 21022;
	public static final String ERROR_MESSAGE_MANAGER_22				= "Failed to add the active <xformsdb:file> elements into the session.";

	public static final int ERROR_CODE_MANAGER_23					= 21023;
	public static final String ERROR_MESSAGE_MANAGER_23				= "Failed to set the active <xformsdb:file> elements into the session.";

	public static final int ERROR_CODE_MANAGER_24					= 21024;
	public static final String ERROR_MESSAGE_MANAGER_24				= "Failed to retrieve the active <xformsdb:file> elements from the session.";

	public static final int ERROR_CODE_MANAGER_25					= 21025;

	public static final int ERROR_CODE_MANAGER_26					= 21026;
	public static final String ERROR_MESSAGE_MANAGER_26				= "Failed to set the HTTP request headers (GET) into the session.";

	public static final int ERROR_CODE_MANAGER_27					= 21027;
	public static final String ERROR_MESSAGE_MANAGER_27				= "Failed to remove the HTTP request headers (GET) from the session.";

	public static final int ERROR_CODE_MANAGER_28					= 21028;
	public static final String ERROR_MESSAGE_MANAGER_28				= "Failed to retrieve the HTTP request headers (GET) from the session.";

	public static final int ERROR_CODE_MANAGER_29					= 21029;
	public static final String ERROR_MESSAGE_MANAGER_29				= "Failed to retrieve the active <xformsdb:widgetquery> elements from the session.";

	public static final int ERROR_CODE_MANAGER_30					= 21030;
	public static final String ERROR_MESSAGE_MANAGER_30				= "Failed to set the active <xformsdb:widgetquery> elements into the session.";

	public static final int ERROR_CODE_MANAGER_31					= 21031;
	public static final String ERROR_MESSAGE_MANAGER_31				= "Failed to add the active <xformsdb:widgetquery> elements into the session.";

	public static final int ERROR_CODE_MANAGER_32					= 21032;
	public static final String ERROR_MESSAGE_MANAGER_32				= "Failed to retrieve the active <xformsdb:widgetquery> element from the session.";

	public static final int ERROR_CODE_MANAGER_33					= 21033;
	public static final String ERROR_MESSAGE_MANAGER_33				= "Failed to set the <xformsdb:widget-data-sources> element into the session.";

	public static final int ERROR_CODE_MANAGER_34					= 21034;
	public static final String ERROR_MESSAGE_MANAGER_34				= "Failed to remove the <xformsdb:widget-data-sources> element from the session.";

	public static final int ERROR_CODE_MANAGER_35					= 21035;
	public static final String ERROR_MESSAGE_MANAGER_35				= "Failed to retrieve the <xformsdb:widget-data-sources> element from the session.";

	public static final int ERROR_CODE_MANAGER_36					= 21036;
	public static final String ERROR_MESSAGE_MANAGER_36				= "Failed to retrieve the SELECT4UPDATE <xformsdb:widgetquery> results from the session.";

	public static final int ERROR_CODE_MANAGER_37					= 21037;
	public static final String ERROR_MESSAGE_MANAGER_37				= "Failed to set the SELECT4UPDATE <xformsdb:widgetquery> results into the session.";

	public static final int ERROR_CODE_MANAGER_38					= 21038;
	public static final String ERROR_MESSAGE_MANAGER_38				= "Failed to add the SELECT4UPDATE <xformsdb:widgetquery> results into the session.";

	public static final int ERROR_CODE_MANAGER_39					= 21039;
	public static final String ERROR_MESSAGE_MANAGER_39				= "Failed to retrieve the SELECT4UPDATE <xformsdb:widgetquery> result from the session.";

	
	// GetHandler errors
	public static final int ERROR_CODE_GETHANDLER_1										= 22001;
	public static final String ERROR_MESSAGE_GETHANDLER_1								= "Failed to handle the GET request.";


	// GetUnknownHandler errors
	public static final int ERROR_CODE_GETUNKNOWNHANDLER_1								= 23001;
	public static final String ERROR_MESSAGE_GETUNKNOWNHANDLER_1_1						= "Failed to handle the GET request having the content type: ";
	public static final String ERROR_MESSAGE_GETUNKNOWNHANDLER_1_2						= ".";

	
	// GetApplicationXWWWFormUrlencodedHandler errors
	public static final int ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_1		= 24001;
	public static final String ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_1	= "Failed to retrieve the requested file.";

	public static final int ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_2		= 24002;
	public static final String ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_2	= "Failed to retrieve the requested file name.";

	public static final int ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_3		= 24003;
	public static final String ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_3	= "Failed to handle the requested file.";

	public static final int ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_4		= 24004;
	public static final String ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_4	= "Failed to retrieve the requested file extension.";

	public static final int ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_5		= 24005;
	public static final String ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_5	= "Failed to handle the GET request having the content type: application/x-www-form-urlencoded.";

	
	// GetNullHandler errors
	public static final int ERROR_CODE_GETNULLHANDLER_1									= 25001;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_1_1							= "Failed to retrieve requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_1_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_2									= 25002;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_2_1							= "Failed to extract name from requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_2_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_3									= 25003;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_3_1							= "Failed to handle requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_3_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_4									= 25004;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_4_1							= "Failed to extract extension from requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_4_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_5									= 25005;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_5							= "Failed to handle request.";

	public static final int ERROR_CODE_GETNULLHANDLER_6									= 25006;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_6_1							= "Failed to retrieve requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_6_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_7									= 25007;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_7_1							= "Failed to retrieve requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_7_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_8									= 25008;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_8_1							= "Failed to retrieve requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_8_2							= "' due to denied access.";

	public static final int ERROR_CODE_GETNULLHANDLER_9									= 25009;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_9_1							= "Failed to generate action URL from requested file '";
	public static final String ERROR_MESSAGE_GETNULLHANDLER_9_2							= "'.";

	public static final int ERROR_CODE_GETNULLHANDLER_10								= 25010;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_10							= "Failed to retrieve the decoded value of a request parameter.";

	public static final int ERROR_CODE_GETNULLHANDLER_11								= 25011;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_11							= "Failed to create the <xformsdb:file> element.";

	public static final int ERROR_CODE_GETNULLHANDLER_12								= 25012;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_12							= "Failed to handle the <xformsdb:file> request.";

	public static final int ERROR_CODE_GETNULLHANDLER_13								= 25013;

	public static final int ERROR_CODE_GETNULLHANDLER_14								= 25014;

	public static final int ERROR_CODE_GETNULLHANDLER_15								= 25015;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_15							= "Failed to build the XOM document object.";

	public static final int ERROR_CODE_GETNULLHANDLER_16								= 25016;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_16							= "Failed to handle the <xformsdb:file> request.";

	public static final int ERROR_CODE_GETNULLHANDLER_17								= 25017;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_17							= "Sorry, the file you are trying to download is not accessible.";

	public static final int ERROR_CODE_GETNULLHANDLER_18								= 25018;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_18							= "Failed to retrieve the roles of the logged in user from XFormsDB.";

	public static final int ERROR_CODE_GETNULLHANDLER_19								= 25019;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_19							= "Sorry, you have to be logged in to download the file.";

	public static final int ERROR_CODE_GETNULLHANDLER_20								= 25020;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_20							= "Sorry, you do not have access to the file you are trying to download.";

	public static final int ERROR_CODE_GETNULLHANDLER_21								= 25021;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_21							= "Failed to check the logged in XFormsDB user's access to the file.";

	public static final int ERROR_CODE_GETNULLHANDLER_23								= 25023;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_23							= "Failed to handle the XFormsDB widget login.";

	public static final int ERROR_CODE_GETNULLHANDLER_24								= 25024;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_24							= "Failed to retrieve the decoded query string.";

	public static final int ERROR_CODE_GETNULLHANDLER_25								= 25025;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_25							= "Failed to retrieve the decoded query string.";

	public static final int ERROR_CODE_GETNULLHANDLER_26								= 25026;
	public static final String ERROR_MESSAGE_GETNULLHANDLER_26							= "Failed to retrieve the decoded value of a request parameter.";

	
	// PostHandler errors
	public static final int ERROR_CODE_POSTHANDLER_1									= 26001;
	public static final String ERROR_MESSAGE_POSTHANDLER_1								= "Failed to handle the POST request.";

	
	// PostNullHandler errors
	public static final int ERROR_CODE_POSTNULLHANDLER_1								= 27001;
	public static final String ERROR_MESSAGE_POSTNULLHANDLER_1							= "Failed to handle the POST request having the content type: null.";

	
	// PostUnknownHandler errors
	public static final int ERROR_CODE_POSTUNKNOWNHANDLER_1								= 28001;
	public static final String ERROR_MESSAGE_POSTUNKNOWNHANDLER_1						= "Failed to handle the POST request having the content type: ";

	
	// PostApplicationXMLHandler errors
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_1						= 29001;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_1				= "Failed to retrieve the posted XML (xom).";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_2						= 29002;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_2				= "Failed to print the posted XML (xom).";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_3						= 29003;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_3				= "Failed to retrieve the XFormsDBRequest transfer object.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_4						= 29004;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_4				= "Failed to retrieve the active <xformsdb:query> element (stored).";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_5						= 29005;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_5				= "Failed to update query transfer object.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_7						= 29007;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_7				= "The action attribute of the <xformsdb:submission> element has an illegal expressiontype parameter value: ";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_8						= 29008;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_8				= "Failed to handle the <xformsdb:query> request.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_9						= 29009;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_9				= "Failed to write the response.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_10						= 29010;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_10				= "Failed to handle the POST request having the content type: application/xml.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_11						= 29011;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_11_1				= "Failed to retrieve query '";
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_11_2				= "' from XFormsDB.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_13						= 29013;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_13				= "Missing XFormsDB request (either <xformsdb:query>, <xformsdb:widgetquery>, <xformsdb:state>, <xformsdb:login>, <xformsdb:logout>, <xformsdb:user>, <xformsdb:file>, or <xformsdb:cookie> element should be provided) from the <xformsdb:instance> element.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_15						= 29015;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_15				= "Failed to update state transfer object.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_17						= 29017;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_17				= "Failed to handle the <xformsdb:state> request.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_18						= 29018;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_18				= "Failed to handle the <xformsdb:login> request.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_19						= 29019;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_19_1				= "Failed to retrieve login '";
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_19_2				= "' from XFormsDB.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_20						= 29020;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_20				= "Failed to retrieve the active <xformsdb:login> element (stored).";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_21						= 29021;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_21				= "Failed to update login transfer object.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_22						= 29022;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_22				= "Failed to update user transfer object.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_23						= 29023;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_23				= "Failed to handle the <xformsdb:user> request.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_24						= 29024;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_24				= "Failed to update logout transfer object.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_25						= 29025;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_25				= "Failed to handle the <xformsdb:logout> request.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_26						= 29026;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_26				= "XFormsDB does not hold the logged in user.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_27						= 29027;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_27				= "Failed to retrieve the logged in user from XFormsDB.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_28						= 29028;

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_29						= 29029;

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_30						= 29030;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_30				= "Failed to build the XOM document object.";

	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_32						= 29032;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_32_1				= "Failed to retrieve file '";
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_32_2				= "' from XFormsDB.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_33						= 29033;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_33				= "Failed to retrieve the active <xformsdb:file> element (stored).";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_34						= 29034;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_34				= "Failed to handle the <xformsdb:file> request.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_35						= 29035;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_35				= "The action attribute of the <xformsdb:submission> element has an illegal filetype parameter value: ";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_36						= 29036;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_36				= "Failed to handle the <xformsdb:cookie> request.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_37						= 29037;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_37_1				= "Failed to retrieve widget query '";
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_37_2				= "' from XFormsDB.";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_38						= 29038;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_38				= "Failed to retrieve the active <xformsdb:widgetquery> element (stored).";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_39						= 29039;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_39				= "The action attribute of the <xformsdb:submission> element has an illegal expressiontype parameter value: ";
	
	public static final int ERROR_CODE_POSTAPPLICATIONXMLHANDLER_40						= 29040;
	public static final String ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_40				= "Failed to handle the <xformsdb:widgetquery> request.";

	
	// Merger errors
	public static final int ERROR_CODE_MERGER_1											= 36001;
	public static final String ERROR_MESSAGE_MERGER_1									= "Failed to create the 3DM specific XML parser.";

	public static final int ERROR_CODE_MERGER_2											= 36002;
	public static final String ERROR_MESSAGE_MERGER_2									= "Failed to parse the XML document (T0, the original version).";

	public static final int ERROR_CODE_MERGER_3											= 36003;
	public static final String ERROR_MESSAGE_MERGER_3									= "Failed to parse the XML document (T1, the modified version).";

	public static final int ERROR_CODE_MERGER_4											= 36004;
	public static final String ERROR_MESSAGE_MERGER_4									= "Failed to parse the XML document (T2, the current version).";

	public static final int ERROR_CODE_MERGER_5											= 36005;
	public static final String ERROR_MESSAGE_MERGER_5									= "Failed to merge the XML documents (T0, T1, and T2).";

	public static final int ERROR_CODE_MERGER_6											= 36006;
	public static final String ERROR_MESSAGE_MERGER_6_1									= "The 3DM result contains ";
	public static final String ERROR_MESSAGE_MERGER_6_2									= " conflict(s):";

	public static final int ERROR_CODE_MERGER_7											= 36007;
	public static final String ERROR_MESSAGE_MERGER_7									= "Failed to handle the 3DM result.";
	
	public static final int ERROR_CODE_MERGER_8											= 36008;
	public static final String ERROR_MESSAGE_MERGER_8									= "Failed to create the matching classes.";
	
	public static final int ERROR_CODE_MERGER_9											= 36009;
	public static final String ERROR_MESSAGE_MERGER_9_1									= "The 3DM result contains ";
	public static final String ERROR_MESSAGE_MERGER_9_2									= " conflict(s) and ";
	public static final String ERROR_MESSAGE_MERGER_9_3									= " conflict warning(s):";
}