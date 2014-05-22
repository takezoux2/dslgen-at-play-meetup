package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.{PostMethod, GetMethod, Controller, Entity}

/**
 * Created by takeshita on 2014/05/16.
 */
class RoutesGen extends Generator[List[Controller]] {

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
               |""".stripMargin

  override def generate(model: List[Controller]): (String, String) = {
    "routes" -> (base + model.flatMap(c => c.methods.flatMap({
      case GetMethod(name,r) => {
        List(s"GET    /${c.name.decapitalize}/${name}  controllers.${c.name}Controller.${name}")
      }
      case PostMethod(name,_,_) => {
        List(s"GET   /${c.name.decapitalize}/${name}  controllers.${c.name}Controller.${name}",
        s"POST   /${c.name.decapitalize}/${name}  controllers.${c.name}Controller.do${name.capitalize}")
      }
    })).mkString("\n"))


  }
}
