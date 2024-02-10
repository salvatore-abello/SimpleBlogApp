CREATE DATABASE blog;
USE blog;

CREATE TABLE Users(
    id int not null auto_increment PRIMARY KEY,
    name varchar(32) not null,
    surname varchar(32) not null,
    email varchar(255) not null UNIQUE,
    password varchar(60) not null
);

CREATE TABLE Tags(
    id int not null auto_increment PRIMARY KEY,
    tagname varchar(32) not null
);


CREATE TABLE Posts(
    id int not null auto_increment PRIMARY KEY,
    title varchar(64) not null,
    content text not null,
    CODOwner int,
    CODTag int,
    FOREIGN KEY (CODOwner) REFERENCES Users(id),
    FOREIGN KEY (CODTag) REFERENCES Tags(id)
);

CREATE TABLE Tags_Posts(
    id int not null auto_increment PRIMARY KEY,
    CODTag int,
    CODPost int,
    FOREIGN KEY (CODTag) REFERENCES Tags(id),
    FOREIGN KEY (CODPost) REFERENCES Posts(id)
);