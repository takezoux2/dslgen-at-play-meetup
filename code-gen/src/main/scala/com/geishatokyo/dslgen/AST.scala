package com.geishatokyo.dslgen

/**
 * Created by takeshita on 2014/05/16.
 */
case class Entity(name : String, fields : List[EntityField],indexes : List[EntityIndex]) {
}

case class EntityField(name : String,fieldType : FieldType,options : List[String]){
  def hasOption(op : String) = options.exists(op)
}

case class EntityIndex( fields : List[String],order : Option[String])


sealed trait FieldType{
  def mysqlType : String
  def scalaType : String
}
object FieldType{
  case object String extends FieldType{
    override def scalaType: String = "String"

    override def mysqlType: String = "VARCHAR(256)"
  }

  case object Long extends FieldType{
    override def scalaType: String = "Long"

    override def mysqlType: String = "BigInteger"
  }

  case object BigString extends FieldType{
    override def scalaType: String = "String"

    override def mysqlType: String = "TEXT"
  }

  case object Int extends FieldType{
    override def scalaType: String = "Int"

    override def mysqlType: String = "INTEGER"
  }

}