package models

import play.api.templates.Html
import java.lang.reflect.Modifier

/**
 * Created by takezoux2 on 2014/05/23.
 */
object ModelSupport {

  val objMethods = classOf[Object].getMethods.map(_.getName).toSet ++ Seq("productIterator","productPrefix","productArity")

  def asNiceHtml(obj : AnyRef) = {

    Html(obj.getClass.getMethods.map(m => {
      if(m.getParameterTypes.length == 0 && !Modifier.isStatic(m.getModifiers) &&
        !m.getName.contains("$") &&
        !objMethods.contains(m.getName)){
        <div class="row">
          <div class="col-sm-4">{m.getName()}</div><div class="col-sm-8">{m.invoke(obj).toString}</div>
        </div>.toString()
      }else{
        ""
      }
    }).mkString("\n"))

  }

}
