xquery version "1.0";
(: $Id: acronyms.xq 6434 2007-08-28 18:59:23Z ellefj $ :)

declare namespace a="http://www.xml-acronym-demystifier.org";
declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";

declare function a:query-part($field as xs:string) as xs:string
{
	if($field = "acronym") then
		"a:Acronym/@id"
	else if($field = "expansion") then
		"a:Acronym/@expansion"
	else if($field = "definition") then
		"a:Definition"
	else
		"."
};

declare function a:build-query($field as xs:string, $term as xs:string)
as xs:string
{
	concat("//a:Entry[", a:query-part($field), " &amp;= """, $term, """]")
};

declare function a:do-query() as element()
{
	let $field := request:get-parameter("field", "any"),
		$term := request:get-parameter("term", "")
	return
		if (string-length($term) = 0) then
			<para>Please specify one or more keywords to search for!</para>
		else
			<entries>
				{ util:eval(a:build-query($field, $term)) }
			</entries>
};

<book>
  <bookinfo>
    <graphic fileref="logo.jpg"/>

    <productname>Open Source Native XML Database</productname>
    <title>XML Acronym Demystifier</title>

    <author>
      <firstname>Wolfgang M.</firstname>
      <surname>Meier</surname>
      <affiliation>
        <address format="linespecific">
          <email>wolfgang at exist-db.org</email>
        </address>
      </affiliation>
    </author>
    <style href="styles/acronyms.css"/>
  </bookinfo>
    
    <ci:include xmlns:ci="http://apache.org/cocoon/include/1.0" src="sidebar.xml"/>

    <chapter>
		<title>XML Acronym Demystifier Example</title>

		<para>A very simple example to search for XML acronyms. The required XML 
		source document is available from <ulink url="http://www.xml-acronym-demystifier.org/">The XML Acronym 
		Demystifier</ulink>. There's an XQuery script to install all examples automatically. Just go to the 
        <ulink url="../admin/admin.xql?user=admin&amp;password=&amp;panel=setup">Examples 
        Setup</ulink> page.</para>

		<form method="get" action="acronyms.xq">
			<table bgcolor="#F3F3F3" width="100%" cellpadding="5"
				cellspacing="0" border="0">
				<tr bgcolor="#D9D9D9">
					<th width="20%" align="left">Search for</th>
					<th width="80%" align="left" colspan="2">List of keywords</th>
				</tr>
				<tr>
					<td width="20%">
						<select name="field" size="1">
							<option value="acronym">Acronym</option>
							<option value="expansion">Expansion</option>
							<option value="definition">Definition</option>
							<option value="any" selected="true">Any Field</option>
						</select>
					</td>
					<td width="60%">
						<input type="text" name="term" size="30"/>
					</td>
					<td width="20%">
						<input type="submit"/>
					</td>
				</tr>
			</table>
		</form>
		{ a:do-query() }
		<para>
			<small>View <a href="source/acronyms.xq">source code</a>
			</small>
		</para>
	</chapter>
</book>
