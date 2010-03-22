xquery version "1.0" encoding "UTF-8";
(: Delete files from the files metadata data source :)


(: Declare namespaces :)
declare namespace util		= "http://exist-db.org/xquery/util";
declare namespace xformsdb	= "http://www.tml.tkk.fi/2007/xformsdb";

(: Declare functions :)
(: Return a deep copy of the elements and all their sub elements :)
declare function local:copy( $elements as element()* ) as element()* {
	for $element in $elements
		return
			element { node-name( $element ) } {
				$element/@*,
				for $child in $element/node()
					return
						if ( $child instance of element() ) then
							local:copy( $child )
						else
							$child
	}
};

(: Declare external variables :)
declare variable $xformsdbdelete as xs:string external;
declare variable $deletedfileids as xs:string external;

(: Parse the XML document :)
let $xformsdbDelete			:=	util:parse( $xformsdbdelete )

(: Retrieve the deleted file IDs :)
let $deletedFileIDs			:=	tokenize( string( $deletedfileids ), " " )

(: Retrieve the information about the deleted files :)
let $files					:=	for $file in $xformsdbDelete/xformsdb:file
									return
										if ( $deletedFileIDs = $file/@id ) then
											$file
										else
											//xformsdb:files/xformsdb:file[ @id = $file/@id ]
											
(: Copy the information about the deleted files :)
let $files					:=	local:copy( $files )

(: Delete files :)
let $deleteResult			:=	for $fileToBeDeleted in $xformsdbDelete/xformsdb:file
									where not( $deletedFileIDs = $fileToBeDeleted/@id )
									return
										update delete //xformsdb:files/xformsdb:file[ @id = $fileToBeDeleted/@id ]

(: Update the deleted file IDs :)
let $deletedFileIDs			:=	string-join( $files/@id, ' ' )


(: Return the result :)
return
	element { node-name( $xformsdbDelete ) } {
		attribute deleted {
			$deletedFileIDs
		},
		$files
	}