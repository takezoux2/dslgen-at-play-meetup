package models

import com.geishatokyo.dslgen.entity._

/**
 */
case class TweetModel(tweet : Tweet) {

  def asNiceHtml = ModelSupport.asNiceHtml(tweet)
}


object TweetModel{
  /**
   * 簡易Proxyのための暗黙変換
   * @param tweet
   * @return
   */
  implicit def proxy(m : TweetModel) = m.tweet
  def from(e : Tweet) = TweetModel(e)
  def from(list : List[Tweet]) = list.map(TweetModel(_))
}