package com.geishatokyo.dslgen.service

import com.geishatokyo.dslgen.entity.User
import com.geishatokyo.dslgen.Repositories

/**
 *
 */
object UserService {

  def signUp( nickname : String,email : String, password : String) : User = {
    val u = User(0,nickname,email,password)
    Repositories.userRepository.insert(u) match{
      case Some(user) => user
      case None => throw new Exception("Fail to create new user")
    }
  }

  def signIn(email : String, password : String) : User = {
    Repositories.userRepository.getByEmail(email) match{
      case Some(user) if user.password == password => user
      case _ => throw new Exception("Fail to login")
    }
  }

}
