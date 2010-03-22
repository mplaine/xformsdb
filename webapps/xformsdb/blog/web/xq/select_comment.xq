xquery version "1.0" encoding "UTF-8";
(: Declare XML Namespaces :)
declare namespace xformsdb="http://www.tml.tkk.fi/2007/xformsdb";

(: Declare external variables :)
declare variable $id as xs:string external;


(: Select comment :)
for $comment in /root/blog/posts/post/comments/comment
	where $comment/@id = $id
	return	<comment id="{ $comment/@id }">
				<creationtime>{ $comment/creationtime/text() }</creationtime>
				<content>{ $comment/content/text() }</content>
				<author>{ $comment/author/text() }</author>
			</comment>