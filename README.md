## StreamingWarsProject

A Java-based web application that simulates a streaming service marketplace where multiple streaming platforms can license and sell content from studios to users through different subscription models.

### Project Overview

**Language:** Java  
**Repository:** neetastyagi/StreamingWarsProject  
**Type:** Full-stack web application  

This project implements a comprehensive system for managing interactions between streaming services, studios, and users in a competitive streaming market. The application includes role-based access control with different functionalities for admins, streaming services, studios, and account users.

---

## Streaming Wars 



### Run the Application

Download java version 14 or higher from:

https://www.oracle.com/java/technologies/javase-jdk15-downloads.html

run jar which is in submission folder in the source code using command:
```
java -jar streamingwars-0.0.1-SNAPSHOT.jar
```
Once deployed, you can access the application at: 

http://localhost:8085

### Set up Database MYSQL

In order to view the database tables, properties to set up DB is given in :
[application.properties](src/main/resources/application.properties)

### Application Navigation

https://localhost:8085/login takes the user to Login page.\
New user should register using register link on screen.\
Studio/Streaming service register with their short name as First name and long name as Last name.\
After registration the user can log in using the username and password given at the time of registration.\
User names and passwords already available for different roles are as below. There is only one admin for the system.

For admin user:   
username: admin\
password: admin
  
For account user:\
username: account\
password: account
  
For streaming service user:  
username: service\
password: service
  
For studio user:\
username: studio\
password: studio 

Based on the role user will be navigated to their pages

### Role specific user functionalities

### Admin functionalities
create demographic groups\
edit demographic group(long name can be edited or updated)\
view system month and year\
set system next month\
view demographic groups, number of accounts in each demographic group, previous spending, current spending, total spending
of each demographic group.

### Studio functionalities
add/edit event(Movie/PPV)\
view events\
view previous, current, total revenue

### Streaming service functionalities
edit streaming service long name\
add/update subscription fee\
license event(Movie/PPV)\
add PPV viewing price\
view licensed events\
retract movie\
view total license fee paid\
view previous, current, total revenue

### Account functionalities
join demographic group (mandatory, without joining a demographic group rest of the functionalities will not be permitted)\
subscribe to streaming service\
watch movie\
buy PPV event\
view watch events\
view previous, current, total spending

### Authentication
Spring security authentication is achieved in class SecurityConfig.\
Class is annotated with @EnableWebSecurity to enable Spring Security's web security support\
The configure(HttpSecurity) method defines which URL paths should be secured and which should not. Specifically, the /login 
and /registration paths are configured to not require any authentication, since all users should be able to reach those pages.\
All other paths will be authenticated.\
In class UserServiceImpl, method loadUserByUsername(String userName) gets the password for the username from database and
verifies if the username, password entered by user is valid.

### Authorization
After logging in to the application users are redirected based on their roles\
In class UserController method redirectOnRole(Authentication auth), based on the role stored in auth, the users will be 
redirected to their pages.\
For example admin gets redirected to /admin page.
