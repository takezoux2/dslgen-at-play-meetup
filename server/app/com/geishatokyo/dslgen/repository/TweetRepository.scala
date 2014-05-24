
package com.geishatokyo.dslgen.repository

import com.geishatokyo.dslgen.entity._
import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * TweetRepository
 */
trait TweetRepository {
  def insert(tweet : Tweet) : Option[Tweet]
def update(tweet : Tweet) : Boolean
  def getById(id : Long): Option[Tweet]
  def getByUserIdWriteTimeDesc(userId : Long): List[Tweet]
  def getByWriteTimeDesc(): List[Tweet]

  //##hold
  //##end
}


class MySQLTweetRepository extends TweetRepository{
  
  override def insert(tweet : Tweet) = {
    DB.withTransaction {implicit conn =>{
      val id = SQL("INSERT INTO Tweet (userId,nickname,message,writeTime) VALUES ({userId},{nickname},{message},{writeTime});")
        .on('userId -> tweet.userId,'nickname -> tweet.nickname,'message -> tweet.message,'writeTime -> tweet.writeTime).executeInsert().get
      if(id > 0){
        Some(tweet.copy(id = id))
      }else{
        None
      }
    }}
  }
      

  override def update(tweet : Tweet) = {
    DB.withTransaction {implicit conn =>{
      SQL("UPDATE Tweet Set userId = {userId},nickname = {nickname},message = {message},writeTime = {writeTime} WHERE id = {id};")
        .on('id -> tweet.id,'userId -> tweet.userId,'nickname -> tweet.nickname,'message -> tweet.message,'writeTime -> tweet.writeTime).executeInsert().get > 0
    }}
  }
      

  override def getById(id : Long): Option[Tweet] = {
    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM Tweet Where id = {id}").on('id -> id).as(Tweet.anormParser.singleOpt)
    }
  }
    

  override def getByUserIdWriteTimeDesc(userId : Long): List[Tweet] = {
    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM Tweet WHERE userId = {userId} ORDER BY writeTime desc").on('userId -> userId).as(Tweet.anormParser.*)
    }
  }
    

  override def getByWriteTimeDesc(): List[Tweet] = {
    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM Tweet  ORDER BY writeTime desc").on().as(Tweet.anormParser.*)
    }
  }
    
  //##hold
  //##end
}
    