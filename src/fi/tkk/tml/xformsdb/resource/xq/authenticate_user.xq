xquery version "1.0" encoding "UTF-8";
(: Authenticate the user with a username and password against realm :)


(: Declare namespaces :)
declare namespace functx	= "http://www.functx.com";
declare namespace xformsdb	= "http://www.tml.tkk.fi/2007/xformsdb";

(: Declare functions :)
(:~
 : Removes attributes from an XML element, based on name 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_remove-attributes.html 
 : @param   $elements the element(s) from which to remove the attributes 
 : @param   $names the names of the attributes to remove, or * for all attributes 
 :)
declare function functx:remove-attributes( $elements as element()*, $names as xs:string* ) as element() {
	for $element in $elements
		return
			element { node-name( $element ) } {
				$element/@*[ not( functx:name-test( name(), $names ) ) ],
				$element/node()
			}
};
(:~
 : Whether a name matches a list of names or name wildcards
 :
 : NOTE: Fixed a bug in the function by converting the value of the name to string. 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @author  Markku Laine, TKK 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_name-test.html 
 : @param   $testname the name to test 
 : @param   $names the list of names or name wildcards 
 :)
declare function functx:name-test( $testname as xs:string?, $names as xs:string* ) as xs:boolean {
	$testname = $names or
	$names = '*' or
	functx:substring-after-if-contains( $testname,':' ) = ( for $name in $names return substring-after( $name, '*:' ) ) or
	substring-before( $testname, ':' ) = ( for $name in $names[ contains( string( . ), ':*' ) ] return substring-before( $name, ':*' ) )
};
(:~
 : Performs substring-after, returning the entire string if it does not contain the delimiter 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_substring-after-if-contains.html 
 : @param   $arg the string to substring 
 : @param   $delim the delimiter 
 :)
declare function functx:substring-after-if-contains( $arg as xs:string?, $delim as xs:string ) as xs:string? {
	if ( contains( $arg, $delim ) ) then
		substring-after( $arg, $delim )
	else
		$arg
};

(: Declare external variables :)
declare variable $username as xs:string external;
declare variable $password as xs:string external;

(: Retrieve user :)
let $user	:=	for $user in //xformsdb:users/xformsdb:user
					where $user/@username = $username and $user/@password = $password
					return
						$user


(: Return the result :)
return
	if ( exists( $user/@username ) ) then
		functx:remove-attributes( $user, ( 'password' ) )
	else
		element xformsdb:user {
		}