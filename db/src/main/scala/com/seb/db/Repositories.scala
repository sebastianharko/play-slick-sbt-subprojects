package com.seb.db

import com.seb.db.Tables._
import com.seb.model.{ServerGet, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scalacache.ScalaCache

trait ServerRepository {
  def getServers(nameFilter: Option[String] = None, clientId: Int): Future[Seq[ServerGet]]
}

class ServerRepositoryImpl(val dbConfigProvider: DatabaseConfigProvider) extends ServerRepository with HasDatabaseConfigProvider[JdbcProfile]  {

  import driver.api._

  def getServers(nameFilter: Option[String] = None, clientId: Int): Future[Seq[ServerGet]] = {

    val basicQuery = { for { s <- server } yield s }

    val query = nameFilter.map { name => basicQuery.filter(server => server.name === name) }.getOrElse(basicQuery)

    db.run(query.result).map {
      rows => rows.map { item => ServerGet(item.id, item.name)}
    }
  }

}

trait UserRepository {
  def getUser(id: Int): Future[User]
}

class UserRepositoryImpl(val dbConfigProvider: DatabaseConfigProvider, val scalaCache: ScalaCache) extends UserRepository with HasDatabaseConfigProvider[JdbcProfile]{

  def getUser(id: Int): Future[User] = {
    Future.successful(User(id, canListServers = true))
  }

  class CachedOps {

    import concurrent.duration._
    import language.postfixOps
    import scalacache._
    import memoization._

    implicit val scalaCache = UserRepositoryImpl.this.scalaCache // from outer class

    def getUser(id: Int): Future[User] = memoize(60 seconds) {
      UserRepositoryImpl.this.getUser(id) // from outer class
    }
  }

  val cache = new CachedOps

}
