package util

import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization

import scala.concurrent.Future


class UnauthorizedException(message: String = null) extends Exception

trait UsefulImplicits {

  implicit def boolean2future4auth(v: Boolean): Future[Unit] = {
    v match {
      case true => Future.successful()
      case false => Future.failed(new UnauthorizedException)
    }
  }

}

trait Support4Json4s {

  implicit val formats = Serialization.formats(NoTypeHints)

}
