
package com.geishatokyo.dslgen.repository

import com.geishatokyo.dslgen.entity._
import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * UserRepository
 */
trait UserRepository {
  def insert(user : User) : Option[User]
def update(user : User) : Boolean
  def getById(id : Long): Option[User]
  def getByEmail(email : String): Option[User]

  //##hold
  //##end
}


class MySQLUserRepository extends UserRepository{
  
  override def insert(user : User) = {
    DB.withTransaction {implicit conn =>{
      val id = SQL("INSERT INTO User (nickname,email,password) VALUES ({nickname},{email},{password});")
        .on('nickname -> user.nickname,'email -> user.email,'password -> user.password).executeInsert().get
      if(id > 0){
        Some(user.copy(id = id))
      }else{
        None
      }
    }}
  }
      

  override def update(user : User) = {
    DB.withTransaction {implicit conn =>{
      SQL("UPDATE User Set nickname = {nickname},email = {email},password = {password} WHERE id = {id};")
        .on('id -> user.id,'nickname -> user.nickname,'email -> user.email,'password -> user.password).executeInsert().get > 0
    }}
  }
      

  override def getById(id : Long): Option[User] = {
    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM User Where id = {id}").on('id -> id).as(User.anormParser.singleOpt)
    }
  }
    

  override def getByEmail(email : String): Option[User] = {
    DB.withTransaction {
      implicit conn =>
        SQL("SELECT * FROM User Where email = {email}").on('email -> email).as(User.anormParser.singleOpt)
    }
  }
    
  //##hold
  //##end
}
    