xquery version "1.0" encoding "UTF-8";
(: Search weather :)


(: Declare external variables :)
declare variable $city as xs:string external;
declare variable $language as xs:string external;


(: Search weather :)
let $uri		:=	iri-to-uri( concat( 'http://www.google.com/ig/api?weather=', $city, '&amp;hl=', $language ) )
let $weather	:=	doc( $uri )


(: Return weather :)
return $weather