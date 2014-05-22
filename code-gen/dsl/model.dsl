

entity User{
  id : Long PK,
  nickname : String Emoji,
  email : String Unique,
  password : String
}


entity Tweet{
  id : Long PK,
  userId : Long,
  nickname : String,
  message : BigString Emoji,
  writeTime : Long
}
index {
  (userId,writeTime).desc
}
