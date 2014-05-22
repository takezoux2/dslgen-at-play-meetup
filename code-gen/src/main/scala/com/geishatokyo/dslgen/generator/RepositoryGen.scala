package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.Entity

/**
 * Created by takeshita on 2014/05/17.
 */
class RepositoryGen extends Generator[Entity] {

  def generate(entity : Entity) = {
    (entity.name + "Repository.scala") -> code(entity)
  }

  def code(entity : Entity) = {

    val name = entity.name
    val (declarations,impls) = (insertCode(entity) :: updateCode(entity) :: Nil).unzip


    s"""
      |package com.geishatokyo.dslgen.repository
      |
      |import com.geishatokyo.dslgen.entity.User
      |import anorm._
      |import play.api.db.DB
      |import play.api.Play.current
      |
      |/**
      | * ${name}Repository
      | */
      |trait ${name}Repository {
      |  ${declarations.mkString("\n")}
      |}
      |
      |
      |class MySQL${name}Repository extends ${name}Repository{
      |  ${impls.mkString("\n")}
      |}
    """.stripMargin
  }


  def insertCode(entity : Entity) = {

    val paramName = entity.name.decapitalize
    val fields = entity.fields.filter(!_.hasOption("PK"))
    val placeHolders = fields.map(f => {
      "{" + f.name + "}"
    }).mkString(",")
    val assign = fields.map(f => {
      s"'${f.name} -> ${paramName}.${f.name}"
    }).mkString(",")
    val fieldNames = fields.map(_.name).mkString(",")

    val declare = s"""def insert(${paramName} : ${entity.name}) : Option[${entity.name}]"""

    val impl =
      s"""
        |  override def insert(${paramName} : ${entity.name}) = {
        |    DB.withTransaction {implicit conn =>{
        |      val id = SQL("INSERT INTO ${entity.name} (${fieldNames}) VALUES (${placeHolders});")
        |        .on(${assign}).executeInsert().get
        |      if(id > 0){
        |        Some(${paramName}.copy(id = id))
        |      }else{
        |        None
        |      }
        |    }}
        |  }
      """.stripMargin

    declare -> impl

  }

  def updateCode(entity : Entity) = {

    val paramName = entity.name.decapitalize
    val fields = entity.fields.filter(!_.hasOption("PK"))
    val setDeclaration = fields.map(f => {
      s"${f.name} = {${f.name}}"
    }).mkString(",")
    val assign = fields.map(f => {
      s"'${f.name} -> ${paramName}.${f.name}"
    }).mkString(",")

    val declare = s"""def update(${paramName} : ${entity.name}) : Option[${entity.name}]"""

    val impl =
      s"""
        |  override def update(${paramName} : ${entity.name}) = {
        |    DB.withTransaction {implicit conn =>{
        |      SQL("UPDATE ${entity.name} Set ${setDeclaration} WHERE id = {id};")
        |        .on('id -> ${paramName}.id,${assign}).executeInsert().get
        |    }}
        |  }
      """.stripMargin

    declare -> impl
  }

}
