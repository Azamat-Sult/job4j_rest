create table if not exists person (
   id serial primary key not null,
   login varchar(255),
   password varchar(255)
);

create table if not exists employee (
   id serial primary key not null,
   name varchar(255),
   surname varchar(255),
   tax_Id int,
   hire_date TIMESTAMP
);

create table if not exists employee_accounts (
   id serial primary key not null,
   employee_id INT references employee(id),
   accounts_id INT references person(id)
);

insert into person (login, password) values ('admin', '123456');
insert into person (login, password) values ('user1', '123456');
insert into person (login, password) values ('user2', '123456');
insert into person (login, password) values ('user3', '123456');

insert into employee (name, surname, tax_Id, hire_date) values ('emp1', 'emp1', 111111, '2021-11-16 19:44:58');
insert into employee (name, surname, tax_Id, hire_date) values ('emp2', 'emp2', 222222, '2021-11-17 19:44:58');
insert into employee (name, surname, tax_Id, hire_date) values ('emp3', 'emp3', 333333, '2021-11-18 19:44:58');

insert into employee_accounts (employee_id, accounts_id) values ((select id from employee where name = 'emp1'), (select id from person where login = 'admin'));
insert into employee_accounts (employee_id, accounts_id) values ((select id from employee where name = 'emp1'), (select id from person where login = 'user1'));

insert into employee_accounts (employee_id, accounts_id) values ((select id from employee where name = 'emp2'), (select id from person where login = 'user2'));

insert into employee_accounts (employee_id, accounts_id) values ((select id from employee where name = 'emp3'), (select id from person where login = 'user3'));