xquery version "1.0" encoding "UTF-8";
(: Create a new note :)


(: Declare XML Namespaces :)
declare namespace util	=		"http://exist-db.org/xquery/util";

(: Declare external variables :)
declare variable $resultsetsize as xs:string external;
declare variable $note as xs:string external;


(: Create a new note :)
let $insertResult				:=	update insert util:parse( $note ) into /root/notes

(: Read notes :)
let $noteList						:=	for $note in /root/notes/note
															where
																( count( /root/notes/note ) - $note/position() ) < number( $resultsetsize )										
															return
																$note


(: Return notes :)										
return
	<notes>{ $noteList }</notes>