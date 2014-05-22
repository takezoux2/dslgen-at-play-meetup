package com.geishatokyo.dslgen.generator

import com.geishatokyo.dslgen.{Param, PostMethod, GetMethod, Controller}

/**
  * Created by takeshita on 2014/05/23.
  */
class ServiceGen extends Generator[Controller] {
   override def generate(c: Controller): (String, String) = {
     (c.name + "Service.scala") -> code(c)
   }

   def code( c : Controller) = {

     val name = c.name
     val methods = c.methods.map({
       case g : GetMethod => {
         s"  def get${g.name.capitalize}(user : User) : ${g.result.scalaType}"
       }
       case p : PostMethod => {
         val params = p.receive.map(p => p.name + " : " + p.paramType.scalaType).mkString(",")
         s"  def ${p.name}(user : User,${params}) : Unit"
       }
     }).mkString("\n")

     s"""package com.geishatokyo.dslgen.service
       |
       |import com.geishatokyo.dslgen.entity._
       |import com.geishatokyo.dslgen.Repositories
       |
       |/**
       | *
       | */
       |trait I${name}Service {
       |
       |${methods}
       |
       |}
     """.stripMargin

   }

 }
