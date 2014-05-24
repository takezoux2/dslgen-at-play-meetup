package com.geishatokyo.dslgen.entity
import anorm.SqlParser._

/**
 * Auto-generated code
 */
case class Tweet(id : Long,userId : Long,nickname : String,message : String,writeTime : Long) {

}

object Tweet{

  val anormParser = for{
    id <- long("id")
    userId <- long("userId")
    nickname <- str("nickname")
    message <- str("message")
    writeTime <- long("writeTime")
  } yield Tweet(id,userId,nickname,message,writeTime)

}