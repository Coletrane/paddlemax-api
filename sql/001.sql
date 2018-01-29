CREATE TABLE paddlemax_user(
  id BIGINT PRIMARY KEY NOT NULL,
  first_Name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  email TEXT NOT NULL,
  location TEXT,
  weight_lbs INT,
  birthday DATE,
  facebookId TEXT,
  googleId TEXT
)
