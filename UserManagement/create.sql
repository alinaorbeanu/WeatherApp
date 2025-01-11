
    create table usersmanagement.role (
        id bigserial not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table usersmanagement.users (
        id bigserial not null,
        role_id bigint,
        email TEXT not null,
        name TEXT not null,
        password TEXT not null,
        primary key (id)
    );

    alter table if exists usersmanagement.users 
       add constraint FK4qu1gr772nnf6ve5af002rwya 
       foreign key (role_id) 
       references usersmanagement.role;

    create table usersmanagement.role (
        id bigserial not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table usersmanagement.users (
        id bigserial not null,
        role_id bigint,
        email TEXT not null,
        name TEXT not null,
        password TEXT not null,
        primary key (id)
    );

    alter table if exists usersmanagement.users 
       add constraint FK4qu1gr772nnf6ve5af002rwya 
       foreign key (role_id) 
       references usersmanagement.role;

    create table usersmanagement.role (
        id bigserial not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table usersmanagement.users (
        id bigserial not null,
        role_id bigint,
        email TEXT not null,
        name TEXT not null,
        password TEXT not null,
        primary key (id)
    );

    alter table if exists usersmanagement.users 
       add constraint FK4qu1gr772nnf6ve5af002rwya 
       foreign key (role_id) 
       references usersmanagement.role;
