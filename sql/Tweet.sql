
CREATE TABLE IF NOT EXISTS Tweet(
  id BigInt PRIMARY KEY AUTO_INCREMENT,
  userId BigInt,
  nickname VARCHAR(128),
  message VARCHAR(128) character set utf8mb4,
  writeTime BigInt,

  INDEX (userId,writeTime),
  INDEX (writeTime)
);

    