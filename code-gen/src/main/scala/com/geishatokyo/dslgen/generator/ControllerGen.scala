package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen._
import com.geishatokyo.dslgen.GetMethod
import com.geishatokyo.dslgen.PostMethod
import com.geishatokyo.dslgen.Param
import com.geishatokyo.dslgen.Controller

/**
 * Created by takeshita on 2014/05/23.
 */
class ControllerGen extends Generator[Controller] {
  override def generate(c: Controller): (String, String) = {
    (c.name + "Controller.scala") -> code(c)
  }

  def code( c : Controller) = {

    val name = c.name
    val methods = c.methods.map({
      case g : GetMethod => getMethod(c,g)
      case p : PostMethod => postMethod(c,p)
    }).mkString("\n")

    s"""
      |package controllers
      |
      |import play.api._
      |import play.api.mvc._
      |import play.api.data._
      |import play.api.data.Forms._
      |import com.geishatokyo.dslgen.service._
      |import models._
      |
      |object ${name}Controller extends Controller with Auth{
      |${methods}
      |  //##hold
      |  //##end
      |}
    """.stripMargin

  }

  def getMethod( c : Controller,getMethod : GetMethod) = {

    val singleType = getMethod.result match{
      case FieldType.ListOf(t) => t.scalaType
      case t => t.scalaType
    }

    s"""
      |  def ${getMethod.name} = Authenticated( (user,req) => {
      |    val data = ${c.name}Service.get${getMethod.name.capitalize}(user)
      |    Ok(views.html.${c.name}_${getMethod.name}(new UserModel(user),${singleType}Model.from(data)))
      |  })
    """.stripMargin
  }

  val ScalaClassToForm = Map(
    "String" -> "text",
    "Int" -> "int"
  )

  def postMethod(c : Controller,postMethod : PostMethod) = {

    val formParams = postMethod.receive.map({
      case Param(name,t) => {
        s"""      "${name}" -> ${ScalaClassToForm(t.scalaType)}"""
      }
    }).mkString(",\n")
    val params = postMethod.receive.map(_.name).mkString(",")
    val formTuple = if(postMethod.receive.size > 1) "tuple" else "single"

    s"""
      |  val ${postMethod.name}Form = Form(
      |    ${formTuple}(
      |${formParams}
      |    )
      |  )
      |
      |  def ${postMethod.name} = Authenticated( (user,req) => {
      |    Ok(views.html.${c.name}_${postMethod.name}(new UserModel(user)))
      |  })
      |
      |  def do${postMethod.name.capitalize} = Authenticated( (user,req) => {
      |    implicit val r = req
      |
      |    val (${params}) = ${postMethod.name}Form.bindFromRequest().get
      |    ${c.name}Service.${postMethod.name}(user,${params})
      |    Redirect(routes.${postMethod.redirect})
      |  })
    """.stripMargin
  }
}
