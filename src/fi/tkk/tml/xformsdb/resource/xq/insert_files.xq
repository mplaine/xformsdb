xquery version "1.0" encoding "UTF-8";
(: Insert files to the files metadata data source :)


(: Declare namespaces :)
declare namespace functx	= "http://www.functx.com"; 
declare namespace util		= "http://exist-db.org/xquery/util";
declare namespace xformsdb	= "http://www.tml.tkk.fi/2007/xformsdb";

(: Declare functions :)
(:~
 : Adds attributes to XML elements 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_add-attributes.html 
 : @param   $elements the element(s) to which you wish to add the attribute 
 : @param   $attrNames the name(s) of the attribute(s) to add 
 : @param   $attrValues the value(s) of the attribute(s) to add 
 :)
declare function functx:add-attributes( $elements as element()*, $attrNames as xs:QName*, $attrValues as xs:anyAtomicType* ) as element()? {       
	for $element in $elements
		return
			element { node-name( $element ) } {
				for $attrName at $seq in $attrNames
					return
						if ( $element/@*[ node-name( . ) = $attrName ] ) then
							()
						else
							attribute { $attrName } {
								$attrValues[ $seq ]
							},
				$element/@*,
				$element/node()
			}
};
(:~
 : Updates the content of one or more elements 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_replace-element-values.html 
 : @param   $elements the elements whose content you wish to replace 
 : @param   $values the replacement values 
 :)
declare function functx:replace-element-values( $elements as element()*, $values as xs:anyAtomicType* ) as element()* {
	for $element at $seq in $elements
		return
			element { node-name( $element ) } {
				$element/@*,
				$values[ $seq ]
			}
};
 
(: Declare external variables :)
declare variable $action as xs:string external;
declare variable $xformsdbinsert as xs:string external;
declare variable $insertedfileids as xs:string external;

(: Parse the XML document :)
let $xformsdbInsert		:=	util:parse( $xformsdbinsert )

(: Retrieve the inserted file IDs :)
let $insertedFileIDs	:=	tokenize( string( $insertedfileids ), " " )

(: Remove the value from files to be inserted :)
let $filesToBeInserted	:=	for $file in $xformsdbInsert/xformsdb:file
								return
									functx:replace-element-values( $file, "" )

(: Insert files :)
let $insertResult		:=	for $fileToBeInserted in $filesToBeInserted
								where not( $insertedFileIDs = $fileToBeInserted/@id )
								return
									update insert $fileToBeInserted into //xformsdb:files

(: Add the download attribute :)
let $files				:=	for $file in $xformsdbInsert/xformsdb:file
								return
									if ( exists( $file/@download ) ) then
										$file
									else
										functx:add-attributes( $file, xs:QName( 'download' ), concat( $action, '?id=', $file/@id ) )

(: Update the inserted file IDs :)
let $insertedFileIDs	:=	string-join( $files/@id, ' ' )


(: Return the result :)
return
	element { node-name( $xformsdbInsert ) } {
		attribute inserted {
			$insertedFileIDs
		},
		$files
	}