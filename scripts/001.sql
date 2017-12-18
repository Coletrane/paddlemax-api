create table app_user (
  id bigint primary key,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  email varchar(255) unique not null,
  password varchar(255)not null,
  birthday bit(255),
  location varchar(255),
  weight_lbs integer
)
