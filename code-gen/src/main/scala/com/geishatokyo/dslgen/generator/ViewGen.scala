package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen._
import com.geishatokyo.dslgen.Controller
import com.geishatokyo.dslgen.GetMethod

/**
 * Created by takeshita on 2014/05/17.
 */
class ViewGen extends Generator[(Controller,Method)] {

  override def generate(model: (Controller, Method)): (String, String) = {
    val code = model._2 match{
      case g : GetMethod => viewForGET(model._1,g)
      case p : PostMethod => viewForPOST(model._1,p)
    }
    (model._1.name + "_" + model._2.name + ".scala.html") -> code
  }

  def viewForGET( c : Controller, m : GetMethod) = {

    val modelType = m.result match{
      case FieldType.ListOf(t) => s"List[${t.scalaType}Model]"
      case t => t.scalaType + "Model"
    }
    val data = s"obj : ${modelType}"
    val showData = m.result match{
      case FieldType.ListOf(t) => {
        """@for( d <- obj){
          |  <div>
          |    @d.toString
          |  </div>
          |}
        """.stripMargin
      }
      case d => {
        """<div>
          |    @d.toString
          |</div>
        """.stripMargin
      }
    }
    s"""
      |@(user : UserModel,${data})
      |
      |@main("${c.name} ${m.name}") {
      |
      |${showData}
      |
      |}
    """.stripMargin
  }

  def viewForPOST( c : Controller, m : PostMethod) = {

    val fields = m.receive.map(p => {
      s"""
        |    <div class="form-group">
        |        <label for="${p.name}" class="col-sm-2 control-label">${p.name}</label>
        |        <div class="col-sm-10">
        |            <input type="email" class="form-control" id="${p.name}" name="${p.name}">
        |        </div>
        |    </div>
      """.stripMargin
    }).mkString("\n")

    s"""
      |@(user : UserModel)
      |
      |@main("${c.name} ${m.name}") {
      |
      |<form class="form-horizontal" role="form" action="@routes.${c.name}Controller.do${m.name.capitalize}" method="POST">
      |    ${fields}
      |    <div class="form-group">
      |        <div class="col-sm-offset-2 col-sm-10">
      |            <button type="submit" class="btn btn-default">送信</button>
      |        </div>
      |    </div>
      |</form>
      |
      |}
    """.stripMargin
  }
}
