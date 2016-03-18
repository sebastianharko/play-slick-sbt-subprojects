package com.seb.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = apps.schema ++ container.schema ++ server.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table apps
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param port Database column port SqlType(int4)
   *  @param containerId Database column container_id SqlType(int4) */
  case class AppsRow(id: Int, name: String, port: Int, containerId: Int)
  /** GetResult implicit for fetching AppsRow objects using plain SQL queries */
  implicit def GetResultAppsRow(implicit e0: GR[Int], e1: GR[String]): GR[AppsRow] = GR{
    prs => import prs._
    AppsRow.tupled((<<[Int], <<[String], <<[Int], <<[Int]))
  }
  /** Table description of table Apps. Objects of this class serve as prototypes for rows in queries. */
  class Apps(_tableTag: Tag) extends Table[AppsRow](_tableTag, "Apps") {
    def * = (id, name, port, containerId) <> (AppsRow.tupled, AppsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(port), Rep.Some(containerId)).shaped.<>({r=>import r._; _1.map(_=> AppsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column port SqlType(int4) */
    val port: Rep[Int] = column[Int]("port")
    /** Database column container_id SqlType(int4) */
    val containerId: Rep[Int] = column[Int]("container_id")

    /** Foreign key referencing container (database name fk_Apps_Container_1) */
    lazy val containerFk = foreignKey("fk_Apps_Container_1", containerId, container)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table apps */
  lazy val apps = new TableQuery(tag => new Apps(tag))

  /** Entity class storing rows of table container
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param image Database column Image SqlType(varchar)
   *  @param serverId Database column server_id SqlType(int4) */
  case class ContainerRow(id: Int, image: String, serverId: Int)
  /** GetResult implicit for fetching ContainerRow objects using plain SQL queries */
  implicit def GetResultContainerRow(implicit e0: GR[Int], e1: GR[String]): GR[ContainerRow] = GR{
    prs => import prs._
    ContainerRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table Container. Objects of this class serve as prototypes for rows in queries. */
  class Container(_tableTag: Tag) extends Table[ContainerRow](_tableTag, "Container") {
    def * = (id, image, serverId) <> (ContainerRow.tupled, ContainerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(image), Rep.Some(serverId)).shaped.<>({r=>import r._; _1.map(_=> ContainerRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column Image SqlType(varchar) */
    val image: Rep[String] = column[String]("Image")
    /** Database column server_id SqlType(int4) */
    val serverId: Rep[Int] = column[Int]("server_id")

    /** Foreign key referencing server (database name fk_Container_Server_1) */
    lazy val serverFk = foreignKey("fk_Container_Server_1", serverId, server)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table container */
  lazy val container = new TableQuery(tag => new Container(tag))

  /** Entity class storing rows of table server
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param name Database column name SqlType(varchar) */
  case class ServerRow(id: Int, name: String)
  /** GetResult implicit for fetching ServerRow objects using plain SQL queries */
  implicit def GetResultServerRow(implicit e0: GR[Int], e1: GR[String]): GR[ServerRow] = GR{
    prs => import prs._
    ServerRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table Server. Objects of this class serve as prototypes for rows in queries. */
  class Server(_tableTag: Tag) extends Table[ServerRow](_tableTag, "Server") {
    def * = (id, name) <> (ServerRow.tupled, ServerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ServerRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table server */
  lazy val server = new TableQuery(tag => new Server(tag))
}
