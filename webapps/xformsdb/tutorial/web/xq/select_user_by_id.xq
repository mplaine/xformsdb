xquery version "1.0" encoding "UTF-8";
(: Declare external variables :)
declare variable $id as xs:string external;


(: Select user :)
/root/users/user[ @id = $id ]