# ![Janus Server](resources/janus-icon.png) (dev version)

## Presentation

Welcome on project Janus : a sockets based server running on Java Runtime Environment (above Java 8).

This server use two TCP ports like a FTP server, one for commands and the other for data. All it configuration
is made by a unique `.properties` file.
The command port is used to send commands to the server. All commands is handle by a *command* design pattern, 
that allow to add new commands easily. The data port is used to send files between the client and the server, he is only open
when a transfer of data is needed.

An high focus on *qualities over quantities* is apply in this project. Qualities is handle by the  running of a sonar analysies
before every major release, with the constraint to reach a code test coverage over 75% and the correction of all issues. 

The main goal of this project is to be easily extended to a more complex services server with a design oriented on user
defined commands and inheritance.

## Installation with Eclipse

* First, clone this repository with `File > Import... > Git > Projects from Git` wizard.
* Choose `Clone URI` and paste the Web URI : `https://github.com/Seikomi/Janus-Server.git`.
* Enter your Github login and password, and select all branchs of the project.
* Select the directory when you want to clone the repository on your file system.
* Select the wizard `Import existing Eclipse projects`.
* Click on `finish` button.

### Notes

You need to have Maven and a JRE or JDK of Java 8 installed on your machine.

## Contact

[nicolas.symphorien@gmail.com](mailto:nicolas.symphorien@gmail.com)
