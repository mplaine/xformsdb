xquery version "1.0" encoding "UTF-8";
(: Read notes :)


(: Read notes :)
let $noteList	:=	for $note in /root/notes/note
										return
											$note


(: Return notes :)
return
	<notes>{ $noteList }</notes>