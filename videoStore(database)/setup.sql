/* schema */

Create table Plans(pid int primary key, name nvarchar(max), maxnum int, fee nvarchar(max))
Create table Customer(id int primary key, pid int references Plans(pid), login nvarchar(max), password nvarchar(max), fname nvarchar(max), lname nvarchar(max))
Create table Rental(cid int references Customer(id), mid int, status int, time datetime, primary key(cid,mid,time))

insert into Customer values(001, 001, 'a123', '321a', 'Jack1', 'Johnson1')
insert into Customer values(002, 002, 'b123', '321b', 'Jack2', 'Johnson2')
insert into Customer values(003, 003, 'c123', '321c', 'Jack3', 'Johnson3')
insert into Customer values(004, 004, 'd123', '321d', 'Jack4', 'Johnson4')
insert into Customer values(005, 005, 'e123', '321e', 'Jack5', 'Johnson5')
insert into Customer values(006, 006, 'f123', '321f', 'Jack6', 'Johnson6')
insert into Customer values(007, 007, 'g123', '321g', 'Jack7', 'Johnson7')
insert into Customer values(008, 008, 'h123', '321h', 'Jack8', 'Johnson8')


insert into Plans values(001,  'iron', 5,       '$19.99')
insert into Plans values(002,  'alumi', 10,     '$29.99')
insert into Plans values(003,  'cupper', 15,    '$39.99')
insert into Plans values(004,  'sliver', 20,    '$49.99')
insert into Plans values(005,  'gold', 25,      '$59.99')
insert into Plans values(006,  'rose_gold', 30, '$69.99')
insert into Plans values(007,  'pt', 35,        '$79.99')
insert into Plans values(008,  'super', 50,     '$99.99')


insert into Rental values(1,  98, 1,    '5/30/13 5:46:42 AM +00:00')
insert into Rental values(2,  9568, 1,  '5/30/13 7:46:42 AM +00:00')
insert into Rental values(3,  32, 1,    '5/30/13 6:46:42 AM +00:00')
