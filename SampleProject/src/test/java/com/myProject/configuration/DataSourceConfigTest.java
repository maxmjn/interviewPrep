package com.myProject.configuration;

import com.myProject.configuration.common.StartServerIT;
import com.myProject.daoTest.Student;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataSourceConfigTest extends StartServerIT {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Autowired private DataSourceConfig dataSourceConfig;
  @Autowired private JdbcTemplate jdbcTemplate;

  @Ignore
  @Test
  public void testPrimaryDataSource() throws Exception{
//    DataSource dataSource = dataSourceConfig.primaryDataSource();
//    Connection connection = dataSource.getConnection();
    String sql = "select * from student";
    List<Student> studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Student.class));
    System.out.println("************* Test Results*************");
    System.out.println(studentList);
    System.out.println("************* Test Results*************");

    EntityManagerFactory emf= Persistence.createEntityManagerFactory("Student_details");
    EntityManager em=emf.createEntityManager();

    em.getTransaction().begin();

    /**
     * An object would have been considered as detached even though you are not intentionally done that.
     *
     * Check if you are trying to persist an entity which has the same id as another entity, and which
     * is already present in the PersistenceContext in your application.
     *
     * Do not set an ID before you save or persist it. Hibernate will look at the Entity you’ve passed
     * and it assumes that because it has its Primary Key populated that it is already in the database.
     *
     * If you are working with Spring MVC application, check that whether you have
     * added <tx:annotation-driven/> in your configuration file. If you are missing this line,
     * transactions would not have been started in your application.
     */
    Student s1=new Student();
    //Don't set primary key id since @GeneratedValue is being used to avoid org.hibernate.PersistentObjectException: detached entity passed to persist
    //s1.setId(101L);
    s1.setName("Gaurav");
    s1.setPassportNumber("P24");

    Student s2=new Student();
    //Don't set primary key id since @GeneratedValue is being used to avoid org.hibernate.PersistentObjectException: detached entity passed to persist
    //s2.setId(102L);
    s2.setName("Ronit");
    s2.setPassportNumber("P22");

    Student s3=new Student();
    //Don't set primary key id since @GeneratedValue is being used to avoid org.hibernate.PersistentObjectException: detached entity passed to persist
    //s3.setId(103L);
    s3.setName("Rahul");
    s3.setPassportNumber("P26");

    em.persist(s1);
    em.persist(s2);
    em.persist(s3);

    em.getTransaction().commit();

    Student search=em.find(Student.class,10001L); //10001 load thru resource/data.sql
    System.out.println("************* Search Test Results*************");
    System.out.println(search);
    System.out.println("************* Search Test Results*************");

    studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Student.class));
    System.out.println("************* New Test Results*************");
    System.out.println(studentList);
    System.out.println("************* New Test Results*************");

    System.out.println("************* Update Test Results*************");
    search.setPassportNumber("changed");
    em.getTransaction().begin();
    em.persist(search);
    em.getTransaction().commit();
    search=em.find(Student.class,10001L); //10001 load thru resource/data.sql
    System.out.println(search);
    System.out.println("************* Update Test Results*************");

    System.out.println("************* Delete Test Results*************");
    em.getTransaction().begin();
    em.remove(search);
    em.getTransaction().commit();
    System.out.println("************* Delete Test Results*************");

    //finally
    em.close();
    emf.close();

    studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Student.class));
    System.out.println("************* CRUD Test Results*************");
    System.out.println(studentList);
    System.out.println("************* CRUD Test Results*************");
  }
}