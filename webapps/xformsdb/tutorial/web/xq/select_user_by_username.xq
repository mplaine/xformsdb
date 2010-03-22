xquery version "1.0" encoding "UTF-8";
(: Declare external variables :)
declare variable $username as xs:string external;


(: Select user :)
for $user in /root/users/user
	where $user/username = $username
	return $user