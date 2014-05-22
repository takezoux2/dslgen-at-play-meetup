package models

import com.geishatokyo.dslgen.entity.User

/**
 * Created by takeshita on 2014/05/22.
 */
case class UserModel(user : User) {

}


object UserModel{
  /**
   * 簡易Proxyのための暗黙変換
   * @param um
   * @return
   */
  implicit def toUser(um : UserModel) = um.user
}
