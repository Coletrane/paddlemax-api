create table app_user (
  id bigint primary key,
  birthday bit(255),
  email varchar(255) unique,
  first_name varchar(255),
  last_name varchar(255),
  location varchar(255),
  weight_lbs integer
)
