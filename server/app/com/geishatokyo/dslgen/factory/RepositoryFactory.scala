package com.geishatokyo.dslgen.factory

import com.geishatokyo.dslgen.repository.{MySQLUserRepository, UserRepository}

/**
 * Created by takeshita on 2014/05/16.
 */
trait RepositoryFactory {

  implicit def userRepository : UserRepository

}

class MySQLRepositoryFactory{

  lazy val userRepository = new MySQLUserRepository

}
