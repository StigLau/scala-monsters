Quick pointers for installing on Google App Engine:
1. Grab a Google App Engine account http://appengine.google.com . Create a new application on it. Call it scala-monsters or something.
2. Download and unzip GAE Java SDK http://code.google.com/appengine/docs/java/gettingstarted/installing.html
3. Add the file scala-monsters/src/main/webapp/WEB-INF/appengine-web.xml
<?xml version="1.0" encoding="utf-8"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
 <application>scala-monsters</application>
 <version>1</version>
</appengine-web-app>
4. Run mvn clean install (Requires Maven 2 http://maven.apache.org)
5. Run appcfg.sh update target/gae-distribution-something-something
6. You're laughing!