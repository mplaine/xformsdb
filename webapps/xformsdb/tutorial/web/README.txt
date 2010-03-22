Tutorial
========


Initialization
--------------
The /WEB-INF/data/init (or /WEB-INF/data/test) folder contains XML documents that are needed for initializing (or testing, respectively) & running this Web application.

The XML documents should be stored in eXist-db under the following paths:
 * /db/xformsdb/tutorial/data/users.xml
 * /db/xformsdb/tutorial/files/xformsdb_files.xml
 
In addition, the following eXist-db user has to be created:
 * Username: xformsdb
 * Password: xformsdb1234
 * Group:    xformsdb
 
Finally, change the owners, groups, and permissions for all the stored aforementioned resources (collections and XML documents) to the following:
 * New owner:       xformsdb
 * New group:       xformsdb
 * New permissions: read, write, and update (user only)

If one wants to run the Web application without using the test data & files, then the conf.xml file needs to be edited a bit:
 * Line  92: /WEB-INF/xmldocumentdata/test --> /WEB-INF/xmldocumentdata/init
 * Line  99: /WEB-INF/xmldocumentrealm/test --> /WEB-INF/xmldocumentrealm/init
 * Line 164: WEB-INF/files/test --> WEB-INF/files/init

This file can be removed from the Web server after a successful deployment.


Users
-----
Username: xformsdb
Password: 

Username: worker
Password: secret

Username: boss
Password: topsecret