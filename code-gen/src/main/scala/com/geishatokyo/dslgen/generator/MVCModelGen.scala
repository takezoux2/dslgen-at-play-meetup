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
      |  def asNiceHtml = ModelSupport.asNiceHtml(${e.name.decapitalize})
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
