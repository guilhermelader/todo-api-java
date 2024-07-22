create table tasks(
    id bigint not null auto_increment,
    title varchar(100) not null,
    description varchar(255) not null,
    deadline date,
    primary key(id)
);