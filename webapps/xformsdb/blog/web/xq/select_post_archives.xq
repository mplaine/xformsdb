xquery version "1.0" encoding "UTF-8";
import module namespace functx = "http://www.functx.com" at "../webapps/blog/xqm/functx_blog.xqm";


(: Create archive dates from creation times :)
let $archiveDates		:=	for $post in /root/blog/posts/post
								order by xs:date( $post/creationtime/text() ) descending
								return	functx:date( year-from-date( $post/creationtime/text() ), month-from-date( $post/creationtime/text() ), 1 )

(: Create the maximum archive date :)
let $maxArchiveDate		:=	functx:date( year-from-date( current-date() ), month-from-date( current-date() ), 1 )

(: Update archive dates :)	
let $archiveDates		:=	distinct-values( insert-before( $archiveDates, 1, $maxArchiveDate ) )

(: Create archives :)
let $archives			:=	for $archive in $archiveDates
								return	<archive>{ $archive }</archive>


(: Return archives :)
return	<archives>{ $archives }</archives>