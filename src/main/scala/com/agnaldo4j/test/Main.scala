package com.agnaldo4j.test

import cats.effect.IO
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Response, Status}
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

object Main extends App with Endpoint.Module[IO] {

  case class Todo(id: Int, title: String, completed: Boolean)

  val auth: Endpoint.Compiled[IO] => Endpoint.Compiled[IO] = compiled => {
    Endpoint.Compiled[IO] {
      case req if req.getParam("user", "teste") != "teste" => compiled(req)
      case req if req.uri == "/v2/hello" => compiled(req)
      case _ => IO.pure(Trace.empty -> Right(Response(Status.Unauthorized)))
    }
  }

  val api: Endpoint[IO, Todo] = get("hello") {
    Ok(Todo(1, "Teste", true))
  }

  val apiV2: Endpoint[IO, Todo] = get("v2" :: path[String]) { title: String =>
    Ok(Todo(1, title, true))
  }

  val filters = Function.chain(Seq(auth))
  val endpoints = Bootstrap.serve[Application.Json](apiV2 :+: api).compile
  val compiled = filters(endpoints)
  val service = Endpoint.toService(compiled)

  Await.ready(Http.server.serve("0.0.0.0:8080", service))
}
