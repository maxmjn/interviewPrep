drop table student;
create table student (id bigint not null, name varchar(255), passportNumber varchar(255), primary key (id));
insert into student
values(10001,'Ranga', 'E1234567');

insert into student
values(10002,'Ravi', 'A1234568');

insert into student
values(10003,'Rama', 'A1234568');