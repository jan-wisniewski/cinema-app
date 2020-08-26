# Cinema App
> Maven multi-module console app with a simple registration and login system. There are two types of accounts: user and administrator which have different privileges. App allows to add movies, cities, cinemas, seances and buy or reserve ticket for seances.

## Table of contents
* [Technologies](#technologies)
* [Setup](#setup)
* [Author](#Author)

## Technologies
#### General:
* Java 14
* [Apache Maven](https://maven.apache.org/) 3.6.3 

#### Dependencies:
* [commons-code](https://commons.apache.org/proper/commons-codec/) 1.14
* evo-inflector 1.2.2
* [guava](https://github.com/google/guava/guava) 28.1-jre
* [javax.mail](http://javamail.java.net/javax.mail) 1.5.5
* [jdbi3-core](http://jdbi.org/) 3.12.2
* [junit-jupiter-engine](https://junit.org/junit5/) 5.6.1
* [junit-platform-launcher](https://junit.org/junit5/) 1.7.0-M1
* [junit-vintage-engine](https://junit.org/junit5/) 5.6.1
* [mockito-core](https://github.com/mockito/mockito) 3.3.3
* [mockito-junit-jupiter](https://github.com/mockito/mockito) 3.3.3
* [mysql-conector-java](http://dev.mysql.com/doc/connector-j/en/) 8.0.19
* [lombok](https://projectlombok.org/) 1.18.12
* [slf4j-api](http://www.slf4j.org/) 1.7.30
* [slf4j-log4j12](http://www.slf4j.org/) 1.7.30

#### Plugins:

* [flatten-maven-plugin](http://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin) 1.2.1
* [maven-antrun-plugin](http://maven.apache.org/plugins/maven-antrun-plugin/) 1.3
* [maven-assembly-plugin](https://maven.apache.org/plugins/maven-assembly-plugin/)	3.2.0
* [maven-clean-plugin](http://maven.apache.org/plugins/maven-clean-plugin/) 2.5
* [maven-compiler-plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)	3.8.1
* [maven-dependency-plugin](http://maven.apache.org/plugins/maven-dependency-plugin/) 2.8
* [maven-deploy-plugin](http://maven.apache.org/plugins/maven-deploy-plugin/) 2.7
* [maven-enforcer-plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/)	3.0.0-M3
* [maven-install-plugin](http://maven.apache.org/plugins/maven-install-plugin/) 2.4
* [maven-jar-plugin](http://maven.apache.org/plugins/maven-jar-plugin/) 2.4
* [maven-release-plugin](http://maven.apache.org/maven-release/maven-release-plugin/) 2.5.3
* [maven-resources-plugin](https://maven.apache.org/plugins/maven-resources-plugin/) 3.1.0
* [maven-site-plugin](https://maven.apache.org/plugins/maven-site-plugin/) 3.9.0
* [maven-surefire-plugin](http://maven.apache.org/surefire/maven-surefire-plugin) 2.12.4

## Setup
To build application from root directory of project run command

`mvn clean install`

Then copy resources folder and app.jar file from target folder in your ui module. Paste it anywhere. Then you can run app from console with the command

`java -jar --enable-preview app.jar`

## Author
Jan Wiśniewski