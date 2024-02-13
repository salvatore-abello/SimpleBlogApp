DROP DATABASE IF EXISTS blog;

CREATE DATABASE blog;

USE blog;

CREATE TABLE Users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    surname VARCHAR(32) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL
);

CREATE TABLE Tags (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tagname VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE Posts (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    CODOwner INT,
    FOREIGN KEY (CODOwner) REFERENCES Users(id)
);

CREATE TABLE Tags_Posts (
    CODTag INT NOT NULL,
    CODPost INT NOT NULL,
    PRIMARY KEY (CODTag, CODPost),
    FOREIGN KEY (CODTag) REFERENCES Tags(id),
    FOREIGN KEY (CODPost) REFERENCES Posts(id)
);

INSERT INTO Users (name, surname, email, password) VALUES
('John', 'Doe', 'john@example.com', 'password1'),
('Alice', 'Smith', 'alice@example.com', 'password2'),
('Bob', 'Johnson', 'bob@example.com', 'password3'),
('Emily', 'Davis', 'emily@example.com', 'password4'),
('Michael', 'Brown', 'michael@example.com', 'password5');

INSERT INTO Tags (tagname) VALUES
('Technology'),
('Travel'),
('Food'),
('Fashion'),
('Music');

INSERT INTO Posts (title, content, CODOwner) VALUES
('10 Tips for Better Coding', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 1),
('A Trip to Paris', 'Nulla vitae elit libero, a pharetra augue.', 2),
('Delicious Pasta Recipe', 'Donec ullamcorper nulla non metus auctor fringilla.', 3),
('Latest Fashion Trends', 'Vestibulum id ligula porta felis euismod semper.', 4),
('Top 10 Albums of the Year', 'Cras mattis consectetur purus sit amet fermentum.', 5);

INSERT INTO Tags_Posts (CODTag, CODPost) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(1, 3),
(3, 5),
(4, 1);
