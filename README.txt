=================================================

==  ==  ==     XFormsDB -- Read Me     ==  ==  ==

=================================================


Last updated for XFormsDB 1.0 on March 16, 2010.

This README.txt file covers the following topics:

     1. About XFormsDB
     2. Licenses
     3. System Requirements
     4. Installation (Test Environment)
     5. Installation (Development Environment)
     6. Building XFormsDB-based Web applications
     7. Logging
     8. More Information
     9. Known Issues
    10. Third-Party Software
     
     
*************************************************
 1. About XFormsDB
*************************************************

XFormsDB is an XForms-based, open-source framework for simplifying Web
application development.

XFormsDB allows software developers and even non-programmers to build useful,
highly interactive multi-user Web applications quickly and easily using
purely declarative languages--does not require users to write any client-side
scripting or server-side programming code at all. 

For more information about XFormsDB, please see Markku Laine's Master's
Thesis presentation and Markku Laine's Master's Thesis (especially
Chapters 1 and 8), which are available at:

     /doc/theses/Markku_Laine_2010_-_Masters_Thesis_Presentation_with_Details.pdf
     /doc/theses/Markku_Laine_2010_-_Masters_Thesis.pdf
     

*************************************************
 2. Licenses
*************************************************

The source code is distributed under the terms of the MIT License. The full
text of the license is available at:

     /LICENSE.txt


See Section 10 (Third-Party Software) for more details about the licenses of
included third-party software.


*************************************************
 3. System Requirements
*************************************************

JDK 1.5.0 or later. The JDK can be downloaded from:

     http://java.sun.com/javase/downloads/index_jdk5.jsp
     
     
The installation instructions can be found from:

     http://java.sun.com/j2se/1.5.0/install.html


In addition, the JAVA_HOME environment variable needs to be set to point to
the pathname of the directory into which you installed the JDK (not JRE!!!),
e.g., /Library/Java/Home

Detailed instructions for setting the JAVA_HOME environment variable can be
found from:

     http://www.advancedwebstats.com/user-guide/html/en/ch05.html


*************************************************
 4. Installation (Test Environment)
*************************************************

By installing the test environment, you can quickly and easily try out
XFormsDB and several sample Web applications bundled with it.

The available commands for managing the test environment are:
 
     * Install the test environment:
       /bin/install.bat (Windows)
       /bin/install.sh (Unix)
     * Uninstall the test environment:
       /bin/uninstall.bat (Windows)
       /bin/uninstall.sh (Unix)
     * Start up the test environment:
       /bin/startup.bat (Windows)
       /bin/startup.sh (Unix)
     * Shut down the test environment:
       /bin/shutdown.bat (Windows)
       /bin/shutdown.sh (Unix)
     * Reset the test environment database:
       /bin/resetdb.bat (Windows)
       /bin/resetdb.sh (Unix)
     * Display information about the test environment:
       /bin/info.bat (Windows)
       /bin/info.sh (Unix)


Please, see Sections 5 (Installation (Development Environment)) and
6 (Building XFormsDB-Based Web Applications) for more information about
installing/setting up the development environment and how to build your own
XFormsDB-based Web applications.


*************************************************
 5. Installation (Development Environment)
*************************************************

By installing/setting up the development environment, users can compile, run,
etc. Web applications bundled with XFormsDB.

In theory, there is no difference between the test environment and the
development environment. The development environment, however, is a bit safer
to use in the long run as executing the /bin/uninstall.bat (Windows) or
/bin/uninstall.sh (Unix) by accident will not delete all you work.

Instructions for installing the development environment:

     * First, shut down the test environment and/or other servers running on
       port 8080.
     * JDK 1.5.0 or later:
       JDK 1.5.0 can be downloaded from:
       http://java.sun.com/javase/downloads/index_jdk5.jsp
     
       Follow the installation instructions available at:
       http://java.sun.com/j2se/1.5.0/install.html
       
       Set the JAVA_HOME environment variable to point to the pathname of the
       directory into which you installed the JDK (not JRE!!!),
       e.g., /Library/Java/Home
       
       Follow the detailed instructions available at:
       http://www.advancedwebstats.com/user-guide/html/en/ch05.html
     
       Set the JAVA_OPTS environment variable:
       JAVA_OPTS="-Xms448m -Xmx448m -XX:PermSize=32m -XX:MaxPermSize=256m -XX:+CMSClassUnloadingEnabled"

       Set/modify the PATH (add ;. and ;%JAVA_HOME%\bin) and CLASSPATH
       (add ;.) environment variables (Windows only).
     * Apache Tomcat 5.5.27:
       Apache Tomcat 5.5.27 can be copied from:
       /tools/tomcat
       
       Copy Apache Tomcat to a directory of your choice on your computer.
       It's important to use specifically this instance of Apache Tomcat as
       it contains some ready-made modifications (an Apache Tomcat user with
       correct roles) and it works together with all the installation
       scripts.

       Follow the installation instructions available at:
       /tools/tomcat/RUNNING.txt
       
       Set the CATALINA_HOME environment variable to point to the pathname
       of the directory into which you installed Apache Tomcat,
       e.g., /Applications/tomcat
       
       Set/modify the PATH (add ;%CATALINA_HOME%\bin) environment variable
       (Windows only).
       
       Finally, make sure that the directory into which you installed Apache
       Tomcat contains the following subfolders:
       $CATALINA_HOME/logs
       $CATALINA_HOME/common/endorsed
       $CATALINA_HOME/shared/lib
       
       And that all shell scripts within that directory are executable. To do
       this run the following command in the directory into which you
       installed Apache Tomcat (Unix only):
       chmod ug+x **/*.sh
     * Apache Ant 1.6.5:
       Apache Ant 1.6.5 can be copied from:
       /tools/ant
       
       Copy Apache Ant to a folder of your choice on your computer. It's
       important to use specifically this instance of Apache Ant as it
       contains some ready-made modifications (added catalina-ant.jar and
       ant-contrib-1.0b3.jar packages in the lib folder) and it works
       together with all the installation scripts.
       
       Follow the installation instructions available at:
       /tools/ant/INSTALL
       
       Set the ANT_HOME environment variable to point to the pathname
       of the directory into which you installed Apache Ant,
       e.g., /Applications/ant

       Set/modify the PATH (add ;%ANT_HOME%\bin) environment variable
       (Windows only).

       Set/modify the ANT_CLASSPATH environment variable to point to the
       ant-launcher.jar package of the directory into which you installed
       Apache Ant and to the current directory, e.g.,
       .:$ANT_HOME/lib/ant-launcher.jar
     * If you decide to modify Apache Tomcat, make sure to update the
       development environment properties to the following file:
       /dev-env.properties
     * In order to apply the changes made to environment variables, you need
       to close all your console windows/shells, and then reopen them and
       continue from where you left.


Apache Ant instructions:

     * The Apache Ant instructions can be seen by running the following
       command in your XFormsDB installation directory:
       ant


Instructions for deploying eXist-db (native XML database) and
Orbeon Forms (XForms processor) using Apache Ant:

     1) Shut down Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_exist.properties tomcat-shutdown
     2) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_exist.properties clean
     3) Deploy eXist-db (native XML database) to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_exist.properties deploy-lite
     4) Verify that eXist-db is running:
        http://localhost:8080/exist
     5) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_orbeon.properties clean
     6) Deploy Orbeon Forms (XForms processor) to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_orbeon.properties deploy-lite
     7) Verify that Orbeon Forms is running:
        http://localhost:8080/orbeon


Instructions for preparing eXist-db for bundled XFormsDB-based Web
applications:

     1) Start up Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_exist.properties tomcat-startup
     2) Open eXist-db:
        http://localhost:8080/exist
     3) Prepare eXist-db for bundled XFormsDB-based Web applications by
        following the instructions at:
        http://localhost:8080/exist/quickstart.xml#sect7
        /webapps/xformsdb/template/web/README.txt
        /webapps/xformsdb/tutorial/web/README.txt
        /webapps/xformsdb/pim/web/README.txt
        /webapps/xformsdb/blog/web/README.txt


Instructions for deploying bundled XFormsDB-based Web applications using
Apache Ant:

     1) Shut down Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_template.properties tomcat-shutdown
     2) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_template.properties clean
     3) Deploy XFormsDB Template (XFormsDB template, sample XFormsDB-based
        Web application) to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_template.properties deploy-lite
     4) Verify that XFormsDB Template is running:
        http://localhost:8080/template
     5) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_tutorial.properties clean
     6) Deploy XFormsDB Tutorial (XFormsDB and XForms tutorials, sample
        XFormsDB-based Web application) to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_tutorial.properties deploy-lite
     7) Verify that XFormsDB Tutorial is running:
        http://localhost:8080/tutorial
     8) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_pim.properties clean
     9) Deploy XFormsDB PIM (Personal Information Management, sample
        XFormsDB-based Web application) to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_pim.properties deploy-lite
    10) Verify that XFormsDB PIM is running:
        http://localhost:8080/pim
    11) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_blog.properties clean
    12) Deploy XFormsDB Blog (Blog, sample XFormsDB-based Web application)
        to Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_blog.properties deploy-lite
    13) Verify that XFormsDB Blog is running:
        http://localhost:8080/blog


*************************************************
 6. Building XFormsDB-Based Web Applications
*************************************************

Instructions for building XFormsDB-based Web applications:

     1) Use XFormsDB Template as a template for your new XFormsDB-based
        Web application. First, copy the /webapps/xformsdb/template folder
        into the /webapps/xformsdb directory and rename the copied folder
        after your project's name, e.g.,
        test
     2) Copy the /build_template.properties file into the / directory
        (the same directory, i.e., the root directory) and rename the copied
        file after your project's name, e.g.,
        build_test.properties
     3) Modify the contents of the /build_test.properties file. The
        webapp.name must match the name of the project, other properties etc.
        can be whatever you like, e.g.,
        # XFormsDB Test related properties
        webapp.name=test
        webapp.displayname=XFormsDB Test       


Now, you can start using Apache Ant commands for compiling, running, etc.
your new XFormsDB-based Web application. The same Apache Ant commands can
be used for compiling, running, etc. bundled XFormsSB-based sample Web
applications, too.

If you are developing an XFormsDB-based Web application and you need to see
changes made as fast as possible, then I would suggest to go through these
extra steps:

     1) Modify the $CATALINA_HOME/conf/server.xml file by adding the
        project's context information within the <Host> element, which can be
        found at the end of the file. In the example below, the value of the
        docBase attribute must point to the build directory of your XFormsDB
        installation and the value of the path attribute must be the same as
        the name of your project, e.g.,
        <Context docBase="/Applications/xformsdb/build" path="/test" reloadable="true" crossContext="true" />
     2) Shut down Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_test.properties tomcat-shutdown
     3) Clean all build products
        ant -propertyfile dev-env.properties -propertyfile build_test.properties clean
     4) Build XFormsDB Test
        ant -propertyfile dev-env.properties -propertyfile build_test.properties build-lite
     5) Start up Apache Tomcat
        ant -propertyfile dev-env.properties -propertyfile build_test.properties tomcat-startup
     6) Verify that XFormsDB Test is running:
        http://localhost:8080/test
     7) Make changes to XFormsDB Test, e.g., index.xformsdb
     8) Build XFormsDB Test
        ant -propertyfile dev-env.properties -propertyfile build_test.properties build-lite
     9) Verify changes, e.g., at:
        http://localhost:8080/test/index.xformsdb
    10) Repeat steps 7-9.
    11) Finally when your project is ready, create a WAR file for
        distributing your project:
        ant -propertyfile dev-env.properties -propertyfile build_test.properties war-lite


*************************************************
 7. Logging
*************************************************

eXist-db:

     * To modify logging properties:
       /webapps/thirdparty/exist/web/WEB-INF/log4j.xml
     * To modify logging properties:
       /webapps/thirdparty/exist/web/WEB-INF/logkit.xconf
     * To read logs:
       $CATALINA_HOME/webapps/exist/WEB-INF/logs/*.log


Orbeon Forms:
 
     * To modify logging properties:
       /webapps/thirdparty/orbeon/web/WEB-INF/resources/config/log4j.xml
     * To read logs:
       $CATALINA_HOME/logs/orbeon.log


XFormsDB-based Web applications:
     
     * To modify logging properties:
       /webapps/xformsdb/<webapp.name>/src/log4j.properties
     * To read logs:
       $CATALINA_HOME/logs/<webapp.name>.log


*************************************************
 8. More Information
*************************************************

Contact us at:

     * Project Manager, Professor Petri Vuorimaa, D.Sc (Tech.), petri.vuorimaa@tkk.fi
     * Project Manager, Mikko Honkala, D.Sc (Tech.), mikko.honkala@nokia.com
     * Project Manager, Oskari Koskimies, M.Sc., oskari.koskimies@nokia.com
     * Main Developer, Markku Laine, M.Sc. (Tech.), markku.laine@gmail.com


*************************************************
 9. Known Issues
*************************************************

The list of known issues:

     * Use the following JAVA_OPTS in order to avoid "OutOfMemoryError:
       PermGen space" error:
       -Xms448m -Xmx448m -XX:PermSize=32m -XX:MaxPermSize=256m -XX:+CMSClassUnloadingEnabled
     * If clicking the button on the select.xformsdb Web page of the XFormsDB
       Tutorial Web application gives you an error message that's because
       your operating system for some reason reads JAR files found in the
       /lib directory in wrong order. Rename the
       /lib/saxon_b-9-1-0-1j/endorse_over_exist_saxon9-xqj.jar file to
       /lib/saxon_b-9-1-0-1j/saxon9-xqj.jar and uninstall everything using
       the uninstall commands found from the /bin directory.
     * Do not use F5 for refreshing as it only refreshes the latest AJAX
       call, not the whole Web page. Either hit enter in the URL bar or
       use the CMD+R combo in Firefox to refresh the whole Web page.
     * Updating the root element of an XML document, which resides in
       eXist-db, when using the data synchronization fails in XFormsDB
       (XFormsDB & eXist-db "bug"). For the workaround, use a dummy root
       element in XML documents. See
       /webapps/xformsdb/pim/web/WEB-INF/data/init/db/xformsdb/pim/data/pim.xml
     * If you are planning to develop widgets, make sure to disable cookies
       in your Web application by adding <Context cookies="false"... /> to
       the $WEBAPP_HOME/META-INF/context.xml file --> Uses URL rewriting
       instead of cookies for session management
     * XQuery Update is not supported yet in Saxon-B (is supported in
       Saxon-A, though) 
     * Shell scripts need to be modified a bit if one wants to use Cygwin
       (uncomment cygwin lines in /bin/*.sh files)
     * The xforms-ready event used with the XFormsDB-related cookie request
       is not supported normally in Orbeon Forms (Orbeon Forms bug). See
       /webapps/xformsdb/tutorial/web/utilities.xformsdb for the workaround.
     * The xforms-submit event is not supported normally in Orbeon Forms
       (Orbeon Forms bug). See /webapps/xformsdb/pim/web/index.xformsdb for
       the workaround.
     * The <xforms:output mediatype="image/..." ... /> element does not work
       in the separate deployment mode if cookies are disabled in Orbeon
       Forms (Orbeon Forms as such does not work if cookies are disabled).
       See /webapps/xformsdb/weather/web/index.xformsdb for the workaround.
     * The <xforms:load show="new"... /> may not work in Orbeon Forms with
       IE7 if the "Automatic prompting for file downloads" in security
       options is disabled (default) (Orbeon Forms bug). See
       /webapps/xformsdb/tutorial/web/selectallfiles.xformsdb for the
       workaround.
     * The noscript mode in Orbeon Forms breaks down when a button is clicked
       (Orbeon Forms bug --> has been reported).


*************************************************
10. Third-Party Software
*************************************************

This product includes the following software:
 
     * 3dm 0.1.5b1 (http://developer.berlios.de/projects/tdm/)
       GNU Lesser General Public License, version 2.1
     * Apache Ant 1.6.5 (http://ant.apache.org/)
       Apache License, version 2.0
     * Apache Commons Lang 2.4 (http://commons.apache.org/lang/)
       Apache License, version 2.0
     * Apache HttpComponents Client 4.0 (http://hc.apache.org/httpcomponents-client/)
       Apache License, version 2.0
     * Apache Log4J 1.2.15 (http://logging.apache.org/log4j/)
       Apache License, version 2.0
     * Apache Tomcat 5.5.27 (http://tomcat.apache.org/)
       Apache License, version 2.0
     * Apache Xerces-J 2.9.1 (http://xerces.apache.org/xerces2-j/)
       Apache License, version 2.0
     * eXist-db 1.2.4-rev8072 (http://exist-db.org/)
       GNU Lesser General Public License, version 2.1
     * JCIP Annotations (http://www.jcip.net/)
       Creative Commons Attribution License, version 2.5
     * Orbeon Forms dev-post-3.7.1.200910160000-development (http://www.orbeon.com/)
       GNU Lesser General Public License, version undefined)
     * Saxon-B 9.1.0.1 for Java (http://saxon.sourceforge.net/)
       Mozilla Public License, version 1.0
     * UUID 3.1 (http://johannburkard.de/software/uuid/)
       MIT License
     * XOM 1.2.2 (http://www.xom.nu/)
       GNU Lesser General Public License, version 2.1

Please consult the third-party licenses directory for more information about
individual licenses.

     /lib/licenses/*.txt


Copyright (c) 2006-2010 Aalto University, School of Science and Technology
and Nokia Research Center. All rights reserved.

--
