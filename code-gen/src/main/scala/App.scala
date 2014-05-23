import com.geishatokyo.dslgen.dsl.{ModelDSLParser, ControllerDSLParser}
import com.geishatokyo.dslgen.generator._
import com.geishatokyo.dslgen.{PostMethod, GetMethod}
import java.io.{FileOutputStream, File, FileInputStream}

/**
 * Created by takeshita on 2014/05/16.
 */
object App {


  def main(args : Array[String]) : Unit = {
    genModel()
    genController()
  }
  def genModel() = {
    val dsl = read("code-gen/dsl/model.dsl")

    val parser = new ModelDSLParser
    val entities = parser.parseDsl(dsl)

    val entityGen = new EntityGen()
    entities.foreach(e => {
      val (filename,code) = entityGen.generate(e)
      write("server/app/com/geishatokyo/dslgen/entity/" + filename,code)
    })

    val repoGen = new RepositoryGen()
    entities.foreach(e => {
      val (filename,code) = repoGen.generate(e)
      write("server/app/com/geishatokyo/dslgen/repository/" + filename,code)
    })

    val sqlGen = new SQLGen()
    entities.foreach(e => {
      val (filename,code) = sqlGen.generate(e)
      write("sql/" + filename,code)
    })
    val mvcModelGen = new MVCModelGen()
    entities.foreach(e => {
      val (filename,code) = mvcModelGen.generate(e)
      write("server/app/models/" + filename,code,false)
    })
    println(entities)
  }

  def genController() = {
    val dsl = read("code-gen/dsl/controller.dsl")
    val controllers = new ControllerDSLParser().parseDsl(dsl)

    val contGen = new ControllerGen
    controllers.foreach(c => {
      val (filename,code) = contGen.generate(c)
      write("server/app/controllers/" + filename,code)
    })

    val serviceGen = new ServiceGen
    controllers.foreach(c => {
      val (iFilename,iCode) = serviceGen.generateInterface(c)
      write("server/app/com/geishatokyo/dslgen/service/" + iFilename,iCode)
      val (filename,code) = serviceGen.generate(c)
      write("server/app/com/geishatokyo/dslgen/service/" + filename,code,false)
    })

    val viewGen = new ViewGen
    controllers.foreach(c => {
      c.methods.foreach( m => {
        val (filename,code) = viewGen.generate(c -> m)
        write("server/app/views/" + filename,code)
      })
    })

    val routesGen = new RoutesGen()

    {
      val (filename,code) = routesGen.generate(controllers)
      write("server/conf/" + filename,code)
    }

  }


  def read(file : String) = {
    val fis = new FileInputStream(file)
    val d = new Array[Byte](fis.available())
    fis.read(d)
    fis.close()
    new String(d,"utf-8")
  }

  def write(filePath : String,code : String,overwrite : Boolean = true) : Unit = {
    val f = new File(filePath)
    if(!f.getParentFile.exists()){
      f.getParentFile.mkdirs()
    }

    if(!overwrite && f.exists()) return

    val fos = new FileOutputStream(f)
    fos.write(code.getBytes("utf-8"))
    fos.close()


  }


}
