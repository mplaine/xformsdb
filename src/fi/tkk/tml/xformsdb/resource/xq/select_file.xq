xquery version "1.0" encoding "UTF-8";
(: Retrieve a file from the files metadata data source :)


(: Declare namespaces :)
declare namespace xformsdb	= "http://www.tml.tkk.fi/2007/xformsdb";

(: Declare external variables :)
declare variable $id as xs:string external;

(: Retrieve a file :)
let $file					:=	for $file in //xformsdb:files/xformsdb:file
									where $file/@id = $id
									return
										$file


(: Return the result :)
return
	$file