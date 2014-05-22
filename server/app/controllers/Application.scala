package controllers

import play.api._
import play.api.mvc._
import models.UserModel

object Application extends Controller with Auth {

  def index = Authenticated( (user,req) => {
    Ok(views.html.index( UserModel(user)))
  })

}