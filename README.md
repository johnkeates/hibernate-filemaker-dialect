## Integrating FileMaker with Spring JPA/Hibernate HowTo

The FileMakerDialect.class was cretaed by John Keates, 
Forked from: https://github.com/johnkeates/hibernate-filemaker-dialect

This is just an implementation note.

Add FileMaker JDBC dirver to the classpath:
(provided by FileMaker Inc. http://www.filemaker.com

I do not know any Maven source.

fmjdbc.jar

Add the following dependencies to your Maven project:
```xml
<!-- hikari -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
</dependency>

<!-- needed for Hikari Configuration -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

setup the following properties in application.properties

```properties
# Hibernate did not recognize itsels which database is FileMaker
# so help it providing the driver to be used
spring.datasource.driver-class-name=com.filemaker.jdbc.Driver

# add "useSSL=false"
# no need to ask for UTF encoding, since FM always stores Unicode UTF8
spring.datasource.url=jdbc:filemaker://host/database?useSSL=false

# access credentials as usual
spring.datasource.username=**username**
spring.datasource.password=**password**

# add the custom Hibernate Dialect to the classpath
# and tell Spring to use it with FileMaker driver
spring.jpa.properties.hibernate.dialect = nl.keates.filemaker.hibernate.dialect.FileMakerDialect

# FM jdbc driver does not allow schema modifications, so disable updates
spring.jpa.hibernate.ddl-auto = none

# FM jdbc driver does not allow "SELECT 1" default connectivity test, so setup this
spring.datasource.hikari.connection-test-query=SELECT p.* FROM FileMaker_Tables p

# setup other properties as usual, like the following
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type=TRACE

```

NOTE: FileMaker SQL support is very poor, not supporting in any way reverse engeneering the database schema (maybe because FileMaker databases do not have 'schema') and constrains (can't see the defined relationships).

When generatinig entities from the database connection, do not forget to set all the @Id columns and create the required associations, because Hibernate can''t look inside.
