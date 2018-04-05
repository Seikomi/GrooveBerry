# GrooveBerry

## Installation details for developper

* First clone these repository.
* This project is build with eclipse with the plugin STS (Spring Tool Suite) and Angular IDE, so you need to install it.
* When the eclipse installation is over, create a fresh workspace.
* Import the maven project from your local git repository
* Import the grooveberry-webappcli Angular IDE project

Note : Use a JDK rather than a JRE to ensure the maven build

## Launch instructions from eclipse

* First launch the grooveberry-server, by running the following java file /grooveberry-server/src/main/java/com/seikomi/grooveberry/Launcher.java
* Then launch the grooveberry-webapp spring boot application, by running the following java file /grooveberry-webapp/src/main/java/com/seikomi/grooveberry/GrooveberryWebappApplication.java
* And finally launch the grooveberry-webappcli angular application by running npm start  in the eclipse view terminal+ on the project grooveberry-webappcli
* You can access the web app at http://localhost:4200/
