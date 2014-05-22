package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.Entity

/**
 * Created by takeshita on 2014/05/17.
 */
class RepositoryGen extends Generator[Entity] {

  def generate(entity : Entity) = {

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

    val declare = s"""def insert(${paramName} : ${entity.name})(implicit connection : Connection) : Option[${entity.name}]"""

    val impl =
      s"""
        |  override def insert(${paramName} : ${entity.name})(implicit connection : Connection) = {
        |    val id = SQL("INSERT INTO ${entity.name} VALUES (${placeHolders});")
        |      .on(${assign}).executeInsert().get
        |    if(id > 0){
        |      Some(${paramName}.copy(id = id))
        |    }else{
        |      None
        |    }
        |
        |  }
      """.stripMargin

    declare -> impl

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

    val declare = s"""def insert(${paramName} : ${entity.name})(implicit connection : Connection) : Option[${entity.name}]"""

    val impl =
      s"""
        |  override def insert(${paramName} : ${entity.name})(implicit connection : Connection) = {
        |    val id = SQL("INSERT INTO ${entity.name} VALUES (${placeHolders});")
        |      .on(${assign}).executeInsert().get
        |    if(id > 0){
        |      Some(${paramName}.copy(id = id))
        |    }else{
        |      None
        |    }
        |
        |  }
      """.stripMargin

    declare -> impl

  }
}
