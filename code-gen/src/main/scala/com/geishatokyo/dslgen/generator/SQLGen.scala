package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.Entity

/**
 * Created by takeshita on 2014/05/16.
 */
class SQLGen extends Generator[Entity] {


  def generate(entity : Entity)  = {
    (entity.name + ".sql") -> code(entity)
  }
  def code(entity : Entity) = {
    def fields = entity.fields.map(f => {
      val col = s"  ${f.name} ${f.fieldType.mysqlType}"

      if(f.hasOption("PK")){
        col + " PRIMARY KEY AUTO_INCREMENT"
      }else if(f.hasOption("Emoji")) {
        col + " character set utf8mb4"
      }else if(f.hasOption("Unique")){
        col + " UNIQUE"
      }else{
        col
      }
    }).mkString(",\n")

    def indexes = entity.indexes.map(i => {
      s"  INDEX (${i.fields.mkString(",")})"
    }).mkString(",\n")
    s"""
      |CREATE TABLE IF NOT EXISTS ${entity.name}(
      |${fields}${if(indexes.length > 0) ",\n" else ""}
      |${indexes}
      |);
      |
    """.stripMargin


  }

}
