xquery version "1.0" encoding "UTF-8";
(: Read notes :)


(: Declare external variables :)
declare variable $resultsetsize as xs:string external;


(: Read notes :)
let $noteList	:=	for $note in /root/notes/note
										where
											( count( /root/notes/note ) - $note/position() ) < number( $resultsetsize )										
										return
											$note


(: Return notes :)										
return
	<notes>{ $noteList }</notes>