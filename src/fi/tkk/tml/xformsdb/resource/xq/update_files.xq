xquery version "1.0" encoding "UTF-8";
(: Update files to the files metadata data source :)


(: Declare namespaces :)
declare namespace functx	= "http://www.functx.com"; 
declare namespace util		= "http://exist-db.org/xquery/util";
declare namespace xformsdb	= "http://www.tml.tkk.fi/2007/xformsdb";

(: Declare functions :)
declare function functx:remove-attribute( $element as element(), $name as xs:string ) as element() {
	element { node-name( $element ) } {
		for $attribute in $element/@*
			where not( $attribute/name() = $name )
			return
				$attribute
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
declare variable $xformsdbupdate as xs:string external;

(: Parse the XML document :)
let $xformsdbUpdate		:=	util:parse( $xformsdbupdate )

(: Remove the value from the updated files :)
let $updatedFiles		:=	for $file in $xformsdbUpdate/xformsdb:file
								return
									functx:replace-element-values( $file, "" )

(: Remove the download attribute from the updated files :)
let $updatedFiles		:=	for $updatedFile in $updatedFiles
								return
									functx:remove-attribute( $updatedFile, 'download' )

(: Update files :)
let $updateResult		:=	for $updatedFile in $updatedFiles
								for $file in //xformsdb:files/xformsdb:file
									where $file[ @id = $updatedFile/@id ]
									return
										update replace $file with $updatedFile

(: Copy files :)
let $files				:=	for $file in $xformsdbUpdate/xformsdb:file
								return
									$file


(: Return the result :)
return
	element { node-name( $xformsdbUpdate ) } {
		$files
	}