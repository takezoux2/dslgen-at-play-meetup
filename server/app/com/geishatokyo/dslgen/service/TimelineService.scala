package com.geishatokyo.dslgen.service

import com.geishatokyo.dslgen.entity._
import com.geishatokyo.dslgen.Repositories
import com.geishatokyo.dslgen.repository.MySQLTweetRepository
import java.util.Date

/**
 *
 */
object TimelineService extends ITimelineService {

  val tweetRepository = new MySQLTweetRepository

  def getAll(user : User) : List[Tweet] = {
    tweetRepository.getByWriteTimeDesc()
  }
  def write(user : User,message : String) : Unit = {
    val tweet = new Tweet(0,user.id,user.nickname,message,new Date().getTime)
    tweetRepository.insert(tweet)
  }

}
     