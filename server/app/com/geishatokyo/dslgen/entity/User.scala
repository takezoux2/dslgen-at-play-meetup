package com.geishatokyo.dslgen.entity
import anorm.SqlParser._

/**
 * Auto-generated code
 */
case class User(id : Long,nickname : String,email : String,password : String) {

}

object User{

  val anormParser = for{
    id <- long("id")
    nickname <- str("nickname")
    email <- str("email")
    password <- str("password")
  } yield User(id,nickname,email,password)

}