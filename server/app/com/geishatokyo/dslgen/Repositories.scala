package com.geishatokyo.dslgen

import com.geishatokyo.dslgen.repository.{UserRepository, MySQLUserRepository}

/**
 * Created by takeshita on 2014/05/21.
 */
object Repositories {

  val userRepository : UserRepository = new MySQLUserRepository

}
