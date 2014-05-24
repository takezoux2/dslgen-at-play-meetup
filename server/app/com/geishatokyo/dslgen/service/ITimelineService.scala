package com.geishatokyo.dslgen.service

import com.geishatokyo.dslgen.entity._
import com.geishatokyo.dslgen.Repositories

/**
 *
 */
trait ITimelineService {

  def getAll(user : User) : List[Tweet]
  def write(user : User,message : String) : Unit

}
     