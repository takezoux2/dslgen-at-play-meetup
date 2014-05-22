package com.geishatokyo.dslgen.generator

/**
 * Created by takeshita on 2014/05/17.
 */
trait Generator[T] {

  implicit class MyStringWrapper(str : String){
    def decapitalize = {
      if(str == null || str.length == 0) str
      else str.charAt(0).toLower + str.drop(1)
    }
  }

  def generate(model : T) : String
}

