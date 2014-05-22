package com.geishatokyo.dslgen.dsl

import scala.util.parsing.combinator.RegexParsers
import com.geishatokyo.dslgen.{EntityIndex, Entity, EntityField, FieldType}

/**
 * Created by takeshita on 2014/05/16.
 */
class ModelDSLParser extends RegexParsers {

  def className = "[a-zA-Z_0-9]+".r
  def fieldName = "[a-zA-Z_0-9]+".r
  def typeName = className ^^ {
    case "String" => FieldType.String
    case "BigString" => FieldType.String
    case "Int" => FieldType.Int
    case "Long" => FieldType.Long
  }
  def option = "[a-zA-Z_0-9]+".r

  def order= "asc" | "desc"

  def entity = "entity" ~> className ~ ("{" ~> entityFieldArea <~ "}") ~ opt(entityIndexArea) ^^ {
    case className ~ fields ~ indexes => {
      Entity(className,fields,indexes.getOrElse(Nil))
    }
  }
  def entityFieldArea = repsep(entityField,",")
  def entityField = (fieldName <~ ":") ~ typeName ~ rep(option) ^^ {
    case fieldName ~ fieldType ~ options => {
      EntityField(fieldName,fieldType,options)
    }
  }
  def entityIndexArea = "index" ~ "{" ~> repsep(entityIndex,",") <~ "}"
  def entityIndex = ("(" ~> repsep(fieldName,",") <~ ")") ~ opt("." ~> order) ^^ {
    case fields ~ order => {
      EntityIndex(fields,order)
    }
  }

  def parseDsl( dsl : String) = {
    parseAll(rep(entity),dsl) match{
      case Success(entities,_) => entities
      case e : NoSuccess => {
        println(e)
        Nil
      }
    }
  }


}



