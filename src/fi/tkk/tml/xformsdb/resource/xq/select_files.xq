xquery version "1.0" encoding "UTF-8";
(: Retrieve files list from the files metadata data source :)


(: Declare namespaces :)
declare namespace functx	= "http://www.functx.com"; 
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
declare function xformsdb:is-matching-values-in-sequences( $seq1 as xs:anyAtomicType*, $seq2 as xs:anyAtomicType*, $type as xs:anyAtomicType? ) as xs:boolean {
	(: Normalize white space in seq1 :)
	let $seq1	:=	for $value in $seq1
						return
							normalize-space( $value )
	(: Normalize white space in seq2 :)
	let $seq2	:=	for $value in $seq2
						return
							normalize-space( $value )
	(: Remove empty values in seq1 :)
	let $seq1	:=	for $value in $seq1
						where string-length( $value ) > 0 
						return
							$value
	(: Remove empty values in seq2 :)
	let $seq2	:=	for $value in $seq2
						where string-length( $value ) > 0 
						return
							$value
	(: Distinct values in seq1 :)
	let $seq1	:=	distinct-values( $seq1 )
	(: Distinct values in seq2 :)
	let $seq2	:=	distinct-values( $seq2 )
	(: Insert an empty role i.e. the public role :)
	let $seq1	:=	if ( compare( $type, 'roles' ) = 0 ) then
						insert-before( $seq1, 1, "" )
					else
						$seq1
	(: Insert an empty role i.e. the public role if the file has no other roles :)
	let $seq2	:=	if ( compare( $type, 'roles' ) = 0 ) then
						if ( count( $seq2 ) = 0 ) then
							insert-before( $seq2, 1, "" )
						else
							$seq2
					else
						$seq2

	(: Is matching values in sequences :)                    
	return $seq1 = $seq2
};

(: Declare external variables :)
declare variable $action as xs:string external;
declare variable $type as xs:string external;
declare variable $ids as xs:string external;
declare variable $roles as xs:string external;
declare variable $username as xs:string external;

(: Retrieve files :)
let $files	:=	if ( compare( $type, 'all' ) = 0 ) then
					for $file in //xformsdb:files/xformsdb:file
						return
							functx:add-attributes( $file, xs:QName( 'download' ), concat( $action, '?id=', $file/@id ) )
				else if ( compare( $type, 'ids' ) = 0 ) then
					for $file in //xformsdb:files/xformsdb:file
						where
							xformsdb:is-matching-values-in-sequences( tokenize( string( $ids ), " " ), tokenize( string( $file/@id ), " " ), $type )
						return
							functx:add-attributes( $file, xs:QName( 'download' ), concat( $action, '?id=', $file/@id ) )
				else if ( compare( $type, 'username' ) = 0 ) then
					for $file in //xformsdb:files/xformsdb:file
						where
							xformsdb:is-matching-values-in-sequences( ( $username ), ( $file/@creator, $file/@lastmodifier ), $type )
						return
							functx:add-attributes( $file, xs:QName( 'download' ), concat( $action, '?id=', $file/@id ) )
				else if ( compare( $type, 'roles' ) = 0 ) then
					for $file in //xformsdb:files/xformsdb:file
						where
							xformsdb:is-matching-values-in-sequences( tokenize( string( $roles ), " " ), tokenize( string( $file/@roles ), " " ), $type )
						return
							functx:add-attributes( $file, xs:QName( 'download' ), concat( $action, '?id=', $file/@id ) )
				else
					()


(: Return the result :)
return
	element xformsdb:files {
		$files
	}