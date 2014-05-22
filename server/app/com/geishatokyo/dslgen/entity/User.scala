package com.geishatokyo.dslgen.entity
import anorm.SqlParser._

/**
 * Created by takeshita on 2014/05/16.
 */
case class User(id : Long, nickname : String, email : String, password : String) {

}

object User{

  val anormParser = for{
    id <- long("id")
    nickname <- str("nickname")
    email <- str("email")
    password <- str("password")
  } yield User(id,nickname,email,password)

}
