package datasource

//  http://blog.scalac.io/2015/07/30/websockets-server-with-akka-http.html

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import akka.stream.ActorMaterializer
import datasource.services.{EchoService, MainService}

import scala.io.StdIn

object Server extends App {

  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  val route = MainService.route ~
    EchoService.route

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  if ( StdIn.readLine() == null){
    println("Went wrong, null from readline")
  }

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
  println("Server is down...")

}
