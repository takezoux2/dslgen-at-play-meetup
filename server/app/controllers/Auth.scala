package controllers

import play.api.mvc._
import com.geishatokyo.dslgen.entity.User
import com.geishatokyo.dslgen.Repositories

/**
 * Created by takeshita on 2014/05/22.
 */
trait Auth {
  self : Controller =>


  def Authenticated(block: User => Result): Action[AnyContent] = Action(req => {
    req.session.get("userId") match{
      case Some(userId) => {
        Repositories.userRepository.getById(userId.toLong) match{
          case Some(user) => block(user)
          case None => {
            Redirect(routes.UserController.signUp())
          }
        }
      }
      case None => {
        Redirect(routes.UserController.signUp())

      }
    }

  })

}
