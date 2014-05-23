package models

import com.geishatokyo.dslgen.entity._

/**
 */
case class UserModel(user : User) {

  def asNiceHtml = {
    classOf[User].getMethods.map(m => {
      if(m.getParameterTypes.length == 0 &&
        (m.getName != "toString" && m.getName != "hashCode")){
        <div class="row">
          <div class="col-sm-4">{m.getName}</div><div class="col-sm-8">{m.invoke(user).toString}</div>
        </div>
      }
    }).mkString("\n")
  }

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