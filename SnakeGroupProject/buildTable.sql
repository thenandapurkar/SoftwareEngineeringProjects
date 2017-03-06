DROP DATABASE if exists snakeDatabase;

CREATE DATABASE snakeDatabase;

USE snakeDatabase;

CREATE TABLE userinfo(
	-- username varchar(20) auto_increment primary key,
    username varchar(20) not null,
    -- password varchar(45) not null,
    password varchar(45) not null,
    -- color varchar(20) not null
    color varchar(20) not null,
    -- historyHigh int not null
    historyhigh int not null,
    -- totalscore int not null
    totalscore int not null
);
