

entity User{
  id : Long PK,
  nickname : String Emoji,
  email : String Unique,
  password : String
}


entity Tweet{
  id : Long PK,
  userId : Long,
  message : BigString Emoji
}
index {
  (userId,id).desc
}