package com.geishatokyo.dslgen.repository

import com.geishatokyo.dslgen.entity.User
import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * UserRepository
 */
trait UserRepository {


  def insert(user : User): Option[User]

  def update(user : User) : Option[User]

  def getByEmail(email : String) : Option[User]

  def getById(id : Long): Option[User]



}


class MySQLUserRepository extends UserRepository{
  override def getByEmail(email: String) = {
    DB.withTransaction{ implicit conn =>
      SQL("SELECT * FROM User Where email = {email}").on('email -> email).as(User.anormParser.singleOpt)
    }
  }

  override def update(user: User) = {
    DB.withTransaction {
      implicit conn =>
        if (SQL("UPDATE User SET nickname = {nickname},email = {email},password = {password} where id ={id}")
          .on('nickname -> user.nickname, 'email -> user.email, 'password -> user.password, 'id -> user.id).execute()) {
          Some(user)
        } else {
          None
        }
    }
  }

  override def insert(user: User) = {
    DB.withConnection {
      implicit conn =>
        val id = SQL("INSERT INTO User (nickname,email,password) VALUES ({nickname},{email},{password});")
          .on('nickname -> user.nickname, 'email -> user.email, 'password -> user.password).executeInsert().get
        if (id > 0) {
          Some(user.copy(id = id))
        } else {
          None
        }
    }

  }

  override def getById(id: Long): Option[User] = {

    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM User Where id = {id}").on('id -> id).as(User.anormParser.singleOpt)
    }
  }

}
