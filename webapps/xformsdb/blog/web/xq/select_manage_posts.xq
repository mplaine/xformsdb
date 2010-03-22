xquery version "1.0" encoding "UTF-8";
import module namespace functx = "http://www.functx.com" at "../webapps/blog/xqm/functx_blog.xqm";


(: Declare XML Namespaces :)
declare namespace xformsdb="http://www.tml.tkk.fi/2007/xformsdb";

(: Declare external variables :)
declare variable $date as xs:string external;


(: Validate the date :)
let $date		:=	functx:if-empty( $date, current-date() )

(: Force to date type :)
let $date		:=	xs:date( $date )

(: Create the minimum post date :)
let $minPost	:=	functx:date( year-from-date( $date ), month-from-date( $date ), 1 )
	
(: Create the maximum post date :)
let $maxPost 	:=	functx:previous-day( functx:add-months( xs:date( $minPost ), 1 ) )

(: Create posts :)
let $posts		:=	for $post in /root/blog/posts/post
						where number( translate( string( $post/creationtime/text() ), "-", "" ) ) >= number( translate( string( $minPost ), "-", "" ) ) and number( translate( string( $post/creationtime/text() ), "-", "" ) ) <= number( translate( string( $maxPost ), "-", "" ) )
						order by xs:date( $post/creationtime/text() ) descending
						return	<post id="{ $post/@id }">
									<headline>{ $post/headline/text() }</headline>
								</post>


(: Return posts :)
return	<manageposts>{ $posts }</manageposts>