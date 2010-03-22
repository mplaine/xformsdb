xquery version "1.0" encoding "UTF-8";
(: Update settings :)


(: Declare external variables :)
declare variable $city as xs:string external;
declare variable $temperature as xs:string external;
declare variable $show-forecast as xs:string external;
declare variable $language as xs:string external;


(: Update settings :)
let $updatedCity					:=	update replace /settings/city/text() with $city
let $updatedTemperature		:=	update replace /settings/temperature/text() with $temperature
let $updatedShowForecast	:=	update replace /settings/show-forecast/text() with $show-forecast
let $updatedLanguage			:=	update replace /settings/language/text() with $language


(: Return settings :)
return /