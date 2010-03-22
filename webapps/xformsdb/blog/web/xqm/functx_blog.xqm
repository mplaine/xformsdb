xquery version "1.0" encoding "UTF-8";
(:~
 :
 : ----------------------------------
 : The FunctX XQuery Function Library
 : ----------------------------------
 :
 : Copyright (C) 2007 Datypic
 :
 : This library is free software; you can redistribute it and/or
 : modify it under the terms of the GNU Lesser General Public
 : License as published by the Free Software Foundation; either
 : version 2.1 of the License.
 :
 : This library is distributed in the hope that it will be useful,
 : but WITHOUT ANY WARRANTY; without even the implied warranty of
 : MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 : Lesser General Public License for more details.
 :
 : You should have received a copy of the GNU Lesser General Public
 : License along with this library; if not, write to the Free Software
 : Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 :
 : For more information on the FunctX XQuery library, contact contrib@functx.com.
 :
 : @version 1.0
 : @see     http://www.xqueryfunctions.com
 :) 
module namespace functx = "http://www.functx.com";


(:~
 : Construct a date from a year, month and day 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_date.html 
 : @param   $year the year 
 : @param   $month the month 
 : @param   $day the day 
 :) 
declare function functx:date( $year as xs:anyAtomicType, $month as xs:anyAtomicType, $day as xs:anyAtomicType ) as xs:date {
	xs:date( concat( functx:pad-integer-to-length( xs:integer( $year ), 4 ), '-', functx:pad-integer-to-length( xs:integer( $month ), 2 ), '-', functx:pad-integer-to-length( xs:integer( $day ), 2 ) ) )
};


(:~
 : Pads an integer to a desired length by adding leading zeros 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_pad-integer-to-length.html 
 : @param   $integerToPad the integer to pad 
 : @param   $length the desired length 
 :) 
declare function functx:pad-integer-to-length( $integerToPad as xs:anyAtomicType?, $length as xs:integer ) as xs:string {
	if ( $length < string-length( string( $integerToPad ) ) )
		then error( xs:QName( 'functx:Integer_Longer_Than_Length' ) )
	else
		concat( functx:repeat-string( '0', $length - string-length( string( $integerToPad ) ) ), string( $integerToPad ) )
};


(:~
 : Repeats a string a given number of times 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_repeat-string.html 
 : @param   $stringToRepeat the string to repeat 
 : @param   $count the desired number of copies 
 :) 
declare function functx:repeat-string( $stringToRepeat as xs:string?, $count as xs:integer ) as xs:string {
	string-join( ( for $i in 1 to $count return $stringToRepeat ), '' )
};


(:~
 : The previous day 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_previous-day.html 
 : @param   $date the date 
 :) 
declare function functx:previous-day( $date as xs:anyAtomicType? ) as xs:date? {
	xs:date( $date ) - xs:dayTimeDuration( 'P1D' )
};


(:~
 : Adds months to a date 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_add-months.html 
 : @param   $date the date 
 : @param   $months the number of months to add 
 :) 
declare function functx:add-months( $date as xs:anyAtomicType?, $months as xs:integer ) as xs:date? {
	xs:date( $date ) + functx:yearMonthDuration( 0, $months )
};


(:~
 : Construct a yearMonthDuration from a number of years and months 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_yearmonthduration.html 
 : @param   $years the number of years 
 : @param   $months the number of months 
 :) 
declare function functx:yearMonthDuration( $years as xs:decimal?, $months as xs:integer? ) as xs:yearMonthDuration {
	( xs:yearMonthDuration( 'P1M' ) * functx:if-empty( $months, 0 ) ) + ( xs:yearMonthDuration( 'P1Y' ) * functx:if-empty( $years, 0 ) )
};


(:~
 : The first argument if it is not blank, otherwise the second argument 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_if-empty.html 
 : @param   $arg the node that may be empty 
 : @param   $value the item(s) to use if the node is empty 
 :) 
declare function functx:if-empty( $arg as item()?, $value as item()* ) as item()* {
	if ( string( $arg ) != '' )
		then data( $arg )
	else
		$value
};