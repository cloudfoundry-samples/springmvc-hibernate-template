# Spring on Cloud Foundry

This is a simple application that demonstrates how to use Spring on Cloud Foundry. 

This application builds a transactional service that talks to an RDBMS and is fronted by a Spring MVC controller which handles RESTful API calls. 

Deployment is easy, and there are a lot of options: 

From an Eclipse environment like the SpringSource Tool Suite equipped with the m2e and Cloud Foundry WTP connector support: 

* Import the project into Eclipse using the m2e / m2eclipse plugin - File > Import > Maven > Existing Maven Projects. 
* Setup a Cloud Foundry WTP server pointing to the Cloud Foundry cloud you want to target
* Drag and drop the application onto the Cloud Foundry WTP instance, and specify that you need a Redis service and a PostgreSQL service and 512M of RAM.

You can use the vmc command line tool, too.
* you need to change the name of the application as specified in your manifest.yml file, if there's already an existing application deployed under the same name on the Cloud Foundry instance
* Run 'mvn clean install' on the command line from the root of the project to create a binary. 
* From the root of the project, run 'vmc push --path target/springmvc31-1.0.0'

You should also be able to deploy the project using the Maven Cloud Foundry plugin, which is already configured. You need to specify 
connectivity information in your ~/.m2/settings.xml file, as described in http://blog.springsource.org/2011/09/22/rapid-cloud-foundry-deployments-with-maven/ 
* you need to change the name of the application as specified in your manifest.yml file, if there's already an existing application deployed under the same name on the Cloud Foundry instance
* from the root of the project, run 'mvn clean install'
* then run 'mvn cf:push' 


Here are some SQL statements to setup the database:

## H2
 ```
 insert into customer (firstname ,lastname ,signupdate ) values( 'Juergen'  , 'Hoeller', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Mark'  , 'Fisher', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Chris'  , 'Richardson', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Josh'  , 'Long', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Dave'  , 'Syer', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Matt'  , 'Quinlan', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Gunnar'  , 'Hillert', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Dave'  , 'McCrory', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Raja'  , 'Rao', NOW()) ;
 insert into customer (firstname ,lastname ,signupdate ) values( 'Monica'  , 'Wilkinson', NOW()) ;
```

## PostgreSQL
```
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Mark', 'Fisher', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Juergen', 'Hoeller', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Chris', 'Richardson', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Dave', 'Syer', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Patrick', 'Chanezon', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Gunnar', 'Hiller', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Josh', 'Long', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Dave', 'McCrory', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Raja', 'Rao', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Andy', 'Piper', NOW()) ;
 INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Eric', 'Bottard', NOW()) ;
```
