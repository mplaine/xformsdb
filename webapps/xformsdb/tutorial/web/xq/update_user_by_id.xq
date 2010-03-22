xquery version "1.0" encoding "UTF-8";
(: Declare XML Namespaces :)
declare namespace util="http://exist-db.org/xquery/util";  

(: Declare external variables :)
declare variable $id as xs:string external;
declare variable $user as xs:string external;


(: Update user :)
let $updateResult := update replace /root/users/user[ @id = $id ] with util:parse( $user )

(: Select user :)
return /root/users/user[ @id = $id ]