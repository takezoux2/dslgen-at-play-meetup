package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.geishatokyo.dslgen.service.UserService

object UserController extends Controller {

  val signUpForm = Form(
    tuple(
      "nickname" -> text,
      "email" -> text,
      "password" -> text
    )
  )



  def signUp = Action {
    Ok(views.html.sign_up())
  }
  def doSignUp = Action(implicit req => {
    val (nickname,email,password) = signUpForm.bindFromRequest().get

    val user = UserService.signUp(nickname,email,password)

    Redirect(routes.Application.index()).withSession("userId" -> user.id.toString)
  })

  val singInForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    )
  )
  def signIn = Action{
    Ok(views.html.sign_in())
  }
  def doSignIn = Action{implicit req => {
    val (email,password) = singInForm.bindFromRequest().get

    val user = UserService.signIn(email,password)

    Redirect(routes.Application.index()).withSession("userId" -> user.id.toString)
  }}

  def doSignOut = Action{
    Ok(views.html.sign_in()).withNewSession
  }

}