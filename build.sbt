

lazy val root =
        project.in( file(".") )
   .aggregate(server)

  
lazy val server = project.in(file("server"))
  .settings(
    name := "dsl_and_codegen",
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      jdbc,
      anorm,
      cache,
      "mysql" % "mysql-connector-java" % "5.1.27"
    )
  ).settings(
    play.Project.playScalaSettings :_*
  )
  
lazy val codeGen = project.in(file("code-gen"))
  .settings(
    name := "code-generator",
    version := "1.0-SNAPSHOT"
  )