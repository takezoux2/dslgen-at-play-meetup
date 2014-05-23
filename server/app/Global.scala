import java.io.{FileInputStream, File}
import play.api.db.DB
import play.api.{GlobalSettings,Application}
import play.api.Play.current

/**
 * Created by takezoux2 on 2014/05/23.
 */
object Global extends GlobalSettings{

  override def onStart(app: Application): Unit = {

    DB.withConnection(conn => {
      new File("sql").listFiles().filter(_.getName.endsWith(".sql")).foreach(f => {
        val sql = read(f)
        println("Execute " + sql)
        conn.createStatement().execute(sql)
      })

    })

  }

  def read(file : File) = {
    val fis = new FileInputStream(file)
    val d = new Array[Byte](fis.available())
    fis.read(d)
    fis.close()
    new String(d,"utf-8")
  }
}
