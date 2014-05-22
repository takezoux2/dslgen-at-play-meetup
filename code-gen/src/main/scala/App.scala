import com.geishatokyo.dslgen.dsl.DSLParser
import java.io.FileInputStream

/**
 * Created by takeshita on 2014/05/16.
 */
object App {


  def main(args : Array[String]) : Unit = {

    val dsl = read("code-gen/dsl/model.dsl")

    val parser = new DSLParser

    val entities = parser.parseDsl(dsl)

    println(entities)
  }

  def read(file : String) = {
    val fis = new FileInputStream(file)
    val d = new Array[Byte](fis.available())
    fis.read(d)
    fis.close()
    new String(d,"utf-8")
  }

}
