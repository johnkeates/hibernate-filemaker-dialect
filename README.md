## Integrating FileMaker with Spring JPA/Hibernate

The FileMakerDialect.class was cretaed by John Keates, 
Forked from: https://github.com/johnkeates/hibernate-filemaker-dialect

This is just an implementation note.

Add FileMaker JDBC dirver to the classpath:
(provided by FileMaker Inc. http://www.filemaker.com)

> fmjdbc.jar

I've tested this implementation with the driver versions provided with FileMaker 12~17 and aparently all worked identically (fine)

I do not know any available Maven repo to date (added a todo to fix this).

#### Configure your pom.xml

Add the following dependencies to your Maven project in order to provide namespaces for required configurations:
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

#### Configure Spring properties

Setup the following properties in the *resources/application.properties* file

```properties
# Hibernate did not recognize itself which database is FileMaker
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

# FM jdbc driver does not allow "SELECT 1" default connectivity test, so use the following
spring.datasource.hikari.connection-test-query=SELECT p.* FROM FileMaker_Tables p

# setup other properties as usual, like the following
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type=TRACE

```

#### Drawbacks, issues & fixes to FileMaker JDBC Driver limitations

FileMaker SQL support is very poor, not supporting in any way reverse engeneering the database schema (maybe because FileMaker databases do not have 'schema') and constrains (can't see the defined relationships).

When generatinig entities from the database connection, do not forget to set all the @Id columns and create the required associations, because Hibernate can't detect key columns nor constrains.

Any relationship must be created manually and JPA/Hibernate annotations fixed.

#### ToDo

- [ ] Create a Maven repository for John's **nl.keates.filemaker.hibernate.dialect.FileMakerDialect** class
- [ ] Add yml configuration template for dev/prod application property files in a common **jhipster** deployment

