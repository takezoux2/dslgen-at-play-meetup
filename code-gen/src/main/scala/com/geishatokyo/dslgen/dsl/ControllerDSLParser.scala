package com.geishatokyo.dslgen.dsl

import scala.util.parsing.combinator.RegexParsers
import com.geishatokyo.dslgen._

/**
 * Created by takeshita on 2014/05/16.
 */
class ControllerDSLParser extends RegexParsers {

  def className = "[a-zA-Z_0-9]+".r
  def paramName = "[a-zA-Z_0-9]+".r
  def methodName = "[a-zA-Z_0-9]+".r
  def path = className ~ "." ~ methodName ^^ {
    case className ~ "." ~ methodName => className + "." + methodName
  }
  def typeName : Parser[FieldType] =
    ("List" ~ "of" ~> typeName ^^ {
      case ft => FieldType.ListOf(ft)
    }) |
      (className ^^ {
    case "String" => FieldType.String
    case "BigString" => FieldType.String
    case "Int" => FieldType.Int
    case "Long" => FieldType.Long
    case s => FieldType.Class(s)
  })
  def option = "[a-zA-Z_0-9]+".r

  def controller = "controller" ~> className ~ ("{" ~> rep(method) <~ "}") ^^ {
    case name ~ methods => Controller(name,methods)
  }

  def method = getMethod | postMethod

  def getMethod = "@get" ~> methodName ~  ("{" ~>
    return_ <~ "}") ^^ {
    case methodName ~  ret => {
      GetMethod(methodName,ret)
    }
  }
  def return_ = "return" ~> typeName

  def postMethod = "@post" ~> methodName ~ ("{" ~>
    receiveParams ~ redirect <~ "}") ^^ {
    case methodName ~ (receive ~ redirect) => {
      PostMethod(methodName,receive,redirect)
    }
  }

  def redirect = "redirect" ~> path

  def receiveParams = "receive" ~ "{" ~> repsep(param,",") <~ "}"

  def param = paramName ~ ":" ~ typeName ^^ {
    case paramName ~ ":" ~ typeName => Param(paramName,typeName)
  }

  def parseDsl( dsl : String) = {
    parseAll(rep(controller),dsl) match{
      case Success(entities,_) => entities
      case e : NoSuccess => {
        println(e)
        Nil
      }
    }
  }


}



