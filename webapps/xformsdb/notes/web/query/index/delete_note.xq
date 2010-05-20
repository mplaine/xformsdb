xquery version "1.0" encoding "UTF-8";
(: Delete a note :)


(: Declare XML Namespaces :)
declare namespace util	=		"http://exist-db.org/xquery/util";

(: Declare external variables :)
declare variable $resultsetsize as xs:string external;
declare variable $id as xs:string external;


(: Delete a note :)
let $deleteResult				:=	for $note in /root/notes/note
															where
																$note/@id = $id
															return
																update delete $note

(: Read notes :)
let $noteList						:=	for $note in /root/notes/note
															where
																( count( /root/notes/note ) - $note/position() ) < number( $resultsetsize )										
															return
																$note


(: Return notes :)										
return
	<notes>{ $noteList }</notes>