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

(: Create the minimum comment date :)
let $minComment	:=	functx:date( year-from-date( $date ), month-from-date( $date ), 1 )
	
(: Create the maximum comment date :)
let $maxComment :=	functx:previous-day( functx:add-months( xs:date( $minComment ), 1 ) )

(: Create comments :)
let $comments	:=	for $comment in /root/blog/posts/post/comments/comment
						where number( translate( string( $comment/creationtime/text() ), "-", "" ) ) >= number( translate( string( $minComment ), "-", "" ) ) and number( translate( string( $comment/creationtime/text() ), "-", "" ) ) <= number( translate( string( $maxComment ), "-", "" ) )
						order by xs:date( $comment/creationtime/text() ) descending
						return	<comment id="{ $comment/@id }" postid="{ $comment/../../@id }">
									<content>{ $comment/content/text() }</content>
								</comment>


(: Return comments :)
return	<managecomments>{ $comments }</managecomments>