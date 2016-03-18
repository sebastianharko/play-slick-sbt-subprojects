import java.time.Clock

import com.google.inject.{Singleton, Provides, AbstractModule}
import com.seb.db.{UserRepository, ServerRepository}
import play.api.db.slick.DatabaseConfigProvider
import services.{ApplicationTimer, AtomicCounter, Counter}

import scalacache.ScalaCache


class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])
  }

  @Provides
  @Singleton
  def getScalaCache: ScalaCache = {
    import scalacache._
    import guava._
    val scalaCache = ScalaCache(GuavaCache())
    scalaCache
  }

  @Provides
  @Singleton
  def getServerRepository(dbConfigProvider: DatabaseConfigProvider): ServerRepository = {
    new ServerRepository(dbConfigProvider)
  }

  @Provides
  @Singleton
  def getUserRepository(dbConfigProvider: DatabaseConfigProvider, scalaCache: ScalaCache): UserRepository = {
    new UserRepository(dbConfigProvider, scalaCache)
  }


}
