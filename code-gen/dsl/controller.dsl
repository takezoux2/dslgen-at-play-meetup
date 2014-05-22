
controller Timeline{
  @get all{
    return List of Tweet
  }
  @post write{
    receive {
      message : String
    }
    redirect Timeline.all
  }

}