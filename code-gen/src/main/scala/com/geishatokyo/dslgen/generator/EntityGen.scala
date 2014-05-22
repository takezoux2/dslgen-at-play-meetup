package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.Entity

/**
 * Created by takeshita on 2014/05/16.
 */
class EntityGen extends Generator[Entity] {

  val parseMethod = Map(
    "String" -> "str",
    "Int" -> "int",
    "Long" -> "long"
  )

  def generate(entity : Entity)  = {
    (entity.name + ".scala") -> code(entity)
  }
  def code(entity : Entity) = {
    def fields = entity.fields.map(f => {
      s"${f.name} : ${f.fieldType.scalaType}"
    }).mkString(",")

    def mapping = entity.fields.map(f => {
      s"""${f.name} <- ${parseMethod(f.fieldType.scalaType)}("${f.name}")"""
    }).mkString("\n    ")

    def fieldNames = entity.fields.map(_.name).mkString(",")

    s"""package com.geishatokyo.dslgen.entity
      |import anorm.SqlParser._
      |
      |/**
      | * Auto-generated code
      | */
      |case class ${entity.name}(${fields}) {
      |
      |}
      |
      |object ${entity.name}{
      |
      |  val anormParser = for{
      |    ${mapping}
      |  } yield ${entity.name}(${fieldNames})
      |
      |}""".stripMargin


  }

}
