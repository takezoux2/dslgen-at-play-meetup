
package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.geishatokyo.dslgen.service._
import models._

object TimelineController extends Controller with Auth{

  def all = Authenticated( (user,req) => {
    val data = TimelineService.getAll(user)
    Ok(views.html.Timeline_all(new UserModel(user),TweetModel.from(data)))
  })
    

  val writeForm = Form(
    single(
      "message" -> text
    )
  )

  def write = Authenticated( (user,req) => {
    Ok(views.html.Timeline_write(new UserModel(user)))
  })

  def doWrite = Authenticated( (user,req) => {
    implicit val r = req

    val (message) = writeForm.bindFromRequest().get
    TimelineService.write(user,message)
    Redirect(routes.TimelineController.all)
  })
    
  //##hold
  //##end
}
    