create table activities (id uuid not null, name varchar(64) not null, user_id uuid not null, primary key (id));
create table timestamps (id uuid not null, end_at timestamp, start_at timestamp not null, activity_id uuid not null, primary key (id));
create table users (id uuid not null, hashed_password varchar(255) not null, name varchar(32) not null, primary key (id));
alter table if exists activities add constraint FKq6cjukylkgxdjkm9npk9va2f2 foreign key (user_id) references users;
alter table if exists timestamps add constraint FKr7l61wfcy0a9g712qjad40h5w foreign key (activity_id) references activities;