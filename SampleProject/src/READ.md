### Interfaces
- API
    ```
    localhost:8080/api/health/pingpong?value=ping
    ```
    - returns json response
    - this is Jersey based RESTful API
- SWAGGER
    - Swagger doc page is returned
    ```
    http://localhost:8080/doc/index.html
    ```
- WEB
    - `static/index.html` is returned
    ```
    localhost:8080
    ```
- H2 DB console
    - Starts up h2 console. This is due to `application.yml` h2 console
    property enabled.
    ```
    localhost:8080/h2-console
    ```
    - To use in-memory _set JDBC URL_ `jdbc:h2:mem:testdb` where
    `testdb` can be any name of your choice.
    - Default _JDBC URL_ `jdbc:h2:~/myProject` indicates DB file will be
    created your local disk volume.
    - Click Connect and you should see the data in `resource/data.sql`
    - `.h2.server.properties` is created in your userAlbum folder/directory

### Build
- <b>Java-8</b> only
- Maven ` mvn clean install`
- Target folder will have executable jar file
- `du -h target/*.jar` gives size (<b>~42MB</b>) of jar

### Run
- Make sure JRE(runtime) is <b>Java 8</b>. If not error related to
  XML `javax.*` appears
- Run ` java -jar <jar name>.jar `

### Overview
- Web + App Server
    <br>`spring-boot-starter-web` provides web server (Tomcat default)
    <br>`spring-boot-starter-jersey` for RESTful resource. We could use
    Spring MVC that comes bundled with spring-boot-starter-web
- Logging
    <br>slf4j + logback
- Static files
    <br> index.html is auto-detected by Spring
- Persistence
    <br> - `spring-boot-starter-data-jpa` for JPA api
    <br> - h2 in-memory DB. Note `application.yml` set to show h2-console.
    <br> - When application starts the logs show `hikari` being used
    as DBCP(DB Connection Pool)

#### H2 overview
- On app start-up `resource/data.sql` is auto-detected by Spring and contents are run.
- Note only file by name `data.sql` is auto-detected by Spring
- Presence of DAO for example `Student.class` JPA annotated with `@Entity`
enables Spring to execute Insert statements within `data.sql` and load into h2.
- JPA notes
    - To use JPA use need `resource/META-INF/persistence.xml`
    - Every time to <b>drop and create</b> table within "persistence.xml"
    use `<property name="hibernate.hbm2ddl.auto" value="create" />`
    - Let's say you have already loaded H2 with table "student" using
    `resource/data.sql` and you want to <b>add to table</b> more data
    within "persistence.xml" use
    `<property name="hibernate.hbm2ddl.auto" value="update" />`

- Keeping both externally loaded data and JPA inserts
    - Let's say you have externally loaded data using `resource/data.sql`
    - Now with `<property name="hibernate.hbm2ddl.auto" value="update" />`
    table structure gets modified before executing JPA `EntityManager persist()`
    - In "persistence.xml" _comment out any_ `<property name="hibernate.hbm2ddl.auto"`
    - And have _drop-create statement_ in `resource/data.sql`
    ```
    drop table student;
    create table student (id bigint not null, name varchar(255), passportNumber varchar(255), primary key (id));
    insert into student
    values(10001,'Ranga', 'E1234567');
    ```

- Avoid error
`org.hibernate.PersistentObjectException: detached entity passed to persist`
    - If H2 table (like student) already has data loaded through
    `resource/data.sql` and you are adding more data programmatically
    to the same table through JPA call `EntityManager persist()`
        - And if DAO like Student.class has `@GeneratedValue` on primary
        field "Id"
        - Then `do not` set "Id" field to avoid error `detached entity passed to persist`
        ```
            Student s3=new Student();//@GeneratedValue is being used on field Student.id

            //Don't set primary key id
            //to avoid org.hibernate.PersistentObjectException: detached entity passed to persist
            //s3.setId(103L);

            s3.setName("John");
            s3.setPassportNumber("P26");

            EntityManagerFactory emf= Persistence.createEntityManagerFactory("Student_details");
            EntityManager em=emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(s3);
            em.getTransaction().commit();
            em.close();
            emf.close();
        ```