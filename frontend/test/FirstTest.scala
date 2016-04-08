import com.google.inject.{AbstractModule, Provides, Singleton}
import com.seb.db.{UserRepository, ServerRepository, ServerRepositoryImpl, UserRepositoryImpl}
import com.seb.model.{ServerGet, User}
import controllers.HomeController
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Configuration, Environment}

import scala.concurrent.Future
import scalacache.ScalaCache
class ExampleControllerTest extends FunSuite with Results with ScalaFutures {

  class TestModule extends AbstractModule {

    def configure = {

    }

    @Provides
    @Singleton
    def getUserRepository(): UserRepository = {
      new UserRepository {
        override def getUser(id: Int): Future[User] = {
          Future.successful(User(42, canListServers = true))
        }
      }
    }

    @Provides
    @Singleton
    def getServerRepository():ServerRepository = {
      new ServerRepository {
        override def getServers(nameFilter: Option[String] = None, clientId: Int):
          Future[Seq[ServerGet]]  = {
            Future.successful(List(ServerGet(42, "Windows")))
        }
      }
    }
  }


  val injector = new GuiceApplicationBuilder().disable[Module].bindings(new TestModule).build().injector

  test("simple example") {
      val controller = injector.instanceOf(classOf[HomeController])
      val sr = injector.instanceOf(classOf[ServerRepository])
      assert(sr.getServers(None, 42).futureValue.size === 1)
      val result: Future[Result] = controller.getServers.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      val code = status(result)
      assert(code === 200)
      assert(bodyText.nonEmpty)
      assert(bodyText === """[{"id":42,"name":"Windows"}]""")

  }


}