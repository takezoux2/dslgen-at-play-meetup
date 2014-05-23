package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.{EntityIndex, EntityField, Entity}

/**
 * Created by takeshita on 2014/05/17.
 */
class RepositoryGen extends Generator[Entity] {

  def generate(entity : Entity) = {
    (entity.name + "Repository.scala") -> code(entity)
  }

  def code(entity : Entity) = {

    val name = entity.name

    val uniques = entity.fields.filter(f => f.hasOption("PK") || f.hasOption("Unique")).map(f => getUnique(entity,f))
    val indexes = entity.indexes.map(i => getIndex(entity,i))
    val (declarations,impls) = (insertCode(entity) :: updateCode(entity) :: (uniques ::: indexes)).unzip


    s"""
      |package com.geishatokyo.dslgen.repository
      |
      |import com.geishatokyo.dslgen.entity._
      |import anorm._
      |import play.api.db.DB
      |import play.api.Play.current
      |
      |/**
      | * ${name}Repository
      | */
      |trait ${name}Repository {
      |  ${declarations.mkString("\n")}
      |
      |  //##hold
      |  //##end
      |}
      |
      |
      |class MySQL${name}Repository extends ${name}Repository{
      |  ${impls.mkString("\n")}
      |  //##hold
      |  //##end
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

    val declare = s"""def update(${paramName} : ${entity.name}) : Boolean"""

    val impl =
      s"""
        |  override def update(${paramName} : ${entity.name}) = {
        |    DB.withTransaction {implicit conn =>{
        |      SQL("UPDATE ${entity.name} Set ${setDeclaration} WHERE id = {id};")
        |        .on('id -> ${paramName}.id,${assign}).executeInsert().get > 0
        |    }}
        |  }
      """.stripMargin

    declare -> impl
  }

  def getUnique( entity : Entity, field : EntityField) = {
    val declare = s"""  def getBy${field.name.capitalize}(${field.name} : ${field.fieldType.scalaType}): Option[${entity.name}]"""
    val impl = s"""
      |  override def getBy${field.name.capitalize}(${field.name} : ${field.fieldType.scalaType}): Option[${entity.name}] = {
      |    DB.withTransaction {
      |      implicit conn =>
      |        SQL("SELECT * FROM ${entity.name} Where ${field.name} = {${field.name}}").on('${field.name} -> ${field.name}).as(${entity.name}.anormParser.singleOpt)
      |    }
      |  }
    """.stripMargin

    declare -> impl
  }

  def getIndex(entity : Entity,index : EntityIndex) = {

    val name = index.fields.map(_.capitalize).mkString + index.order.map(_.capitalize).getOrElse("")

    val fields = if(index.order.isDefined){
      index.fields.dropRight(1).map(n => entity.fields.find(_.name == n).get)
    }else {
      index.fields.map(n => entity.fields.find(_.name == n).get)
    }
    val params = {
      fields.map(f => {
        s"${f.name} : ${f.fieldType.scalaType}"
      }).mkString
    }
    val conditions = if(fields.size > 0)"WHERE" + fields.map(f => {
      s"${f.name} = {${f.name}}"
    }).mkString(" and ")
    else ""

    val asign = fields.map(f => {
      s"'${f.name} -> ${f.name}"
    }).mkString(",")
    val orderBy = index.order match{
      case Some(ascOrDesc) => s"ORDER BY ${index.fields.last} ${ascOrDesc}"
      case None => ""
    }

    val declare = s"""  def getBy${name}(${params}): List[${entity.name}]"""
    val impl = s"""
      |  override def getBy${name}(${params}): List[${entity.name}] = {
      |    DB.withTransaction {
      |      implicit conn =>
      |        SQL("SELECT * FROM ${entity.name} ${conditions} ${orderBy}").on(${asign}).as(${entity.name}.anormParser.*)
      |    }
      |  }
    """.stripMargin

    declare -> impl

  }

}
