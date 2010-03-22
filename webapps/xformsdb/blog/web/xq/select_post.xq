xquery version "1.0" encoding "UTF-8";
(: Declare XML Namespaces :)
declare namespace xformsdb="http://www.tml.tkk.fi/2007/xformsdb";

(: Declare external variables :)
declare variable $id as xs:string external;


(: Select post :)
for $post in /root/blog/posts/post
	where $post/@id = $id
	return	<post id="{ $post/@id }">
				<headline>{ $post/headline/text() }</headline>
				<creationtime>{ $post/creationtime/text() }</creationtime>
				<content>{ $post/content/text() }</content>
				<author>{ $post/author/text() }</author>
				<comments>{ count( $post/comments/comment ) }</comments>
			</post>