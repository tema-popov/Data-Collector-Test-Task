CREATE DATABASE  jobs CHARACTER SET 'UTF8';

CREATE TABLE  jobs.all_jobs (
title TEXT NOT NULL ,
description TEXT NOT NULL ,
link varchar(255) NOT NULL ,
salary TEXT NOT NULL ,
full_day BOOL NOT NULL ,
cluster_id INT DEFAULT '0' ,
 UNIQUE (link)
) CHARACTER SET 'UTF8';

CREATE TABLE  jobs.var (
var_name varchar(255) NOT NULL ,
var_value INT DEFAULT 0 ,
 UNIQUE (var_name)
) CHARACTER SET utf8;

INSERT INTO jobs.var VALUES('last_cluster', '0');
