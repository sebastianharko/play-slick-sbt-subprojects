package controllers

import javax.inject._

import _root_.util.{Support4Json4s, UnauthorizedException, UsefulImplicits}
import com.seb.db.{ServerRepository, UserRepository, ServerRepositoryImpl, UserRepositoryImpl}
import com.seb.model.ServerGet
import org.json4s.jackson.Serialization.write
import play.api.cache.Cached
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

@Singleton
class HomeController @Inject()(userRepository: UserRepository, serverRepository: ServerRepository)(implicit exec: ExecutionContext) extends
  Controller with Support4Json4s with UsefulImplicits {

  def index = Action {
    Ok(views.html.index("Your awesome application is ready."))
  }

  def getServers = Action.async {

    // same thing as client id
    val userId = Random.nextInt() // assume we get this from JWT claim set

    val result = for {
      user <- userRepository.getUser(userId)
      canListServers <- user.canListServers
      list <- serverRepository.getServers(None, userId)
    } yield list

    result.map {
      (serverList: Seq[ServerGet]) => Ok(write(serverList)).as("application/json")
    }.recover {
      case e: UnauthorizedException => Unauthorized(write("error" -> "you are not allowed to get the server list")).as("application/json")
      case _ => InternalServerError(write("error" -> "internal server error"))
    }

  }

}

import com.iheart.playSwagger.SwaggerSpecGenerator
import play.api.libs.concurrent.Execution.Implicits._

@Singleton
class ApiSpecs @Inject() (cached: Cached) extends Controller {
  implicit val cl = getClass.getClassLoader

  val domainPackage = "com.seb.model"
  private lazy val generator = SwaggerSpecGenerator(domainPackage)

  def specs =
    Action.async { _ =>
      Future.fromTry(generator.generate()).map(Ok(_)) //generate() can also taking in an optional arg of the route file name.
    }


}
