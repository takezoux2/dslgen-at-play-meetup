package com.geishatokyo.dslgen

/**
 * Created by takeshita on 2014/05/23.
 */
case class Controller(name : String,methods : List[Method]) {

}

trait Method{
  def name : String
}
case class GetMethod(name : String,result : FieldType) extends Method
case class PostMethod(name : String,receive : List[Param],redirect : String) extends Method

case class Param(name : String, paramType : FieldType)