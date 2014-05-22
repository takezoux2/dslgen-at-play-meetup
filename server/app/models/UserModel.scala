package models

import com.geishatokyo.dslgen.entity._

/**
 */
case class UserModel(user : User) {

}


object UserModel{
  /**
   * 簡易Proxyのための暗黙変換
   * @param user
   * @return
   */
  implicit def proxy(m : UserModel) = m.user
  def from(e : User) = UserModel(e)
  def from(list : List[User]) = list.map(UserModel(_))
}