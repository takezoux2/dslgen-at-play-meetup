package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.Entity

/**
 * Created by takeshita on 2014/05/23.
 */
class MVCModelGen extends Generator[Entity] {
  override def generate(e: Entity): (String, String) = {


    val code = s"""package models
      |
      |import com.geishatokyo.dslgen.entity._
      |
      |/**
      | */
      |case class ${e.name}Model(${e.name.decapitalize} : ${e.name}) {
      |
      |  def asNiceHtml = {
      |    classOf[${e.name}].getMethods.map(m => {
      |      if(m.getParameterTypes.length == 0 &&
      |        (m.getName != "toString" && m.getName != "hashCode")){
      |        <div class="row">
      |          <div class="col-sm-4">{m.getName}</div><div class="col-sm-8">{m.invoke(${e.name.decapitalize}).toString}</div>
      |        </div>
      |      }
      |    }).mkString("\n")
      |  }
      |}
      |
      |
      |object ${e.name}Model{
      |  /**
      |   * 簡易Proxyのための暗黙変換
      |   * @param ${e.name.decapitalize}
      |   * @return
      |   */
      |  implicit def proxy(m : ${e.name}Model) = m.${e.name.decapitalize}
      |  def from(e : ${e.name}) = ${e.name}Model(e)
      |  def from(list : List[${e.name}]) = list.map(${e.name}Model(_))
      |}""".stripMargin

    (e.name + "Model.scala") -> code
  }
}
