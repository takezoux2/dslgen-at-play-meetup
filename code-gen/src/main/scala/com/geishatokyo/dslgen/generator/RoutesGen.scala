package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.{PostMethod, GetMethod, Controller, Entity}

/**
 * Created by takeshita on 2014/05/16.
 */
class RoutesGen extends Generator[Controller] {

  val base = """# Routes
               |# This file defines all application routes (Higher priority routes first)
               |# ~~~~
               |
               |# Home page
               |GET     /                           controllers.Application.index
               |GET     /sign_up                    controllers.UserController.signUp
               |POST    /do_sign_up                 controllers.UserController.doSignUp
               |GET     /sign_in                    controllers.UserController.signIn
               |POST    /do_sign_in                 controllers.UserController.doSignIn
               |
               |# Map static resources from the /public folder to the /assets URL path
               |GET     /assets/*file               controllers.Assets.at(path="/public", file)
               |
               |# generated routes
               |"""

  override def generate(model: Controller): (String, String) = {
    "routes" -> (base + model.methods.map({
      case GetMethod(name,r) => {
        s"GET    /${model.name.decapitalize}/${name}  controllers.${model.name}Controller.${name}"
      }
      case PostMethod(name,_,_) => {
        s"POST   /${model.name.decapitalize}/${name}  controllers.${model.name}Controller.${name}"
      }
    }).mkString("\n"))


  }
}
