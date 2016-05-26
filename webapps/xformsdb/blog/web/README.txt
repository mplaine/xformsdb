Blog
====


Initialization
--------------
The /WEB-INF/data/init (or /WEB-INF/data/test) folder contains XML documents that are needed for initializing (or testing, respectively) & running this Web application.

The XML documents should be stored in eXist-db under the following paths:
 * /db/xformsdb/blog/data/blog.xml
 * /db/xformsdb/blog/realm/xformsdb_users.xml

In addition, the following eXist-db user has to be created:
 * Username: xformsdb
 * Password: xformsdb1234
 * Group:    xformsdb

Finally, change the owners, groups, and permissions for all the stored aforementioned resources (collections and XML documents) to the following:
 * New owner:       xformsdb
 * New group:       xformsdb
 * New permissions: read, write, and update (user only)

In addition, copy the /xqm folder to the following directory: the directory, in which Java is running appended with ../webapps/blog. In case of an exception, see logs in debug mode in order to find out the correct path.


Users
-----
Username: admin
Password: adminpass


Finally
-------
This file can be removed from the web server after a successful deployment.