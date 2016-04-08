name := "SBT Sub Projects"

version := "1.0"

val commonSettings = Seq(
  organization := "com.seb",
  version := "0.1",
  scalaVersion := "2.11.8",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
)


resolvers += Resolver.jcenterRepo

lazy val scalazDependency = "org.scalaz" %% "scalaz-core" % "7.2.1"

lazy val playSlickDependency = "com.typesafe.play" %% "play-slick" % "2.0.0"

lazy val dbDependencies = Seq(
  "com.typesafe.slick" %% "slick" % "3.0.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.0.1",
  "com.h2database" % "h2" % "1.4.187",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.github.cb372" % "scalacache-core_2.11" % "0.7.5",
  "com.github.cb372" % "scalacache-guava_2.11" % "0.7.5",
  playSlickDependency
)

lazy val testDependencies = Seq (
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

lazy val playDependencies = Seq (
  cache,
  ws,
  "org.json4s" % "json4s-core_2.11" % "3.3.0",
  "org.json4s" % "json4s-jackson_2.11" % "3.3.0",
  playSlickDependency,
  "com.iheart" %% "play-swagger" % "0.2.1-PLAY2.5",
  "org.webjars" % "swagger-ui" % "2.1.4",
  scalazDependency,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test"
)

lazy val db = project.in(file("db"))
  .settings(commonSettings:_*)
  .settings(libraryDependencies ++= dbDependencies)
  .settings(slick <<= slickCodeGenTask)
  .dependsOn(codegen)

lazy val codegen = project.in(file("codegen"))
  .settings(commonSettings:_*)
  .settings(libraryDependencies ++= dbDependencies)

lazy val slick = TaskKey[Seq[File]]("gen-tables")

lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams, sourceDirectory) map { (dir, cp, r, s, sDir) =>
     val outputDir = (sDir.getPath + "/main/scala/") // place generated files in sbt's managed sources folder
     toError(r.run("com.seb.codegen.CustomizedCodeGenerator", cp.files, Array(outputDir), s.log))
     val fname = outputDir + "/" + "com/seb/db/Tables.scala"
     Seq(file(fname))
   }

lazy val frontend = project.in(file("frontend"))
  .settings(commonSettings:_*)
  .settings(initialCommands in console :=
    """import com.typesafe.config.ConfigFactory;
      |import slick.jdbc.JdbcBackend.Database;
      |val config = ConfigFactory.load();
      |val db = Database.forURL(config.getString("slick.dbs.default.db.url"));
      |import com.seb.db.Tables._
      |import slick.driver.PostgresDriver
      |import slick.driver.PostgresDriver._
      |import slick.driver.PostgresDriver.api
      |import slick.driver.PostgresDriver.api._
      |""".stripMargin)
  .settings(libraryDependencies ++= (testDependencies ++ playDependencies))
  .enablePlugins(PlayScala)
  .dependsOn(db)

/* use Slick from Scala console like this:
$ sbt
> project frontend
[frontend] $ console
scala> val basicQuery = { for { s <- server } yield s }
basicQuery: slick.lifted.Query[com.seb.db.Tables.Server,com.seb.db.Tables.ServerRow,Seq] = Rep(Bind)
scala> val r = db.run(basicQuery.result)
r: scala.concurrent.Future[Seq[com.seb.db.Tables.ServerRow]] = List()
scala> r.value // wait a bit for the result
res1: Option[scala.util.Try[Seq[com.seb.db.Tables.ServerRow]]] = Some(Success(Vector(ServerRow(0,Linux), ServerRow(1,Windows))))
*/


lazy val main = project.in(file("."))
  .aggregate(db, frontend)
