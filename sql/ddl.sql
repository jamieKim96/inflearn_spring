create table member
(
    id bigint generated by default as identity,  //1
    name varchar(255),
    primary key (id)
    );