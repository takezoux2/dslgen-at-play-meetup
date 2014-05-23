
CREATE TABLE IF NOT EXISTS User(
  id BigInt PRIMARY KEY AUTO_INCREMENT,
  nickname VARCHAR(128) character set utf8mb4,
  email VARCHAR(128) UNIQUE,
  password VARCHAR(128)

);

    