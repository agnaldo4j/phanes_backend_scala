package com.agnaldo4j.test

import cats.effect.IO
import com.twitter.finagle.http.cookie.SameSite
import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.http.{Cookie, Request, Response, Status}
import com.twitter.finagle.{Http, Service}
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
    val cookie = new Cookie(name = "token", value = "teste", httpOnly = true, sameSite = SameSite.Lax)
    Ok(Todo(1, "Teste", true)).withCookie(cookie)
  }

  val apiV2: Endpoint[IO, Todo] = get("v2" :: path[String]) { title: String =>
    Ok(Todo(1, title, true))
  }

  val apiV3: Endpoint[IO, Todo] = get("v3" :: path[String] :: path[Int]) { (title: String, age: Int) =>
    Ok(Todo(age, title, true))
  }

  val policy: Cors.Policy = Cors.Policy(
    allowsOrigin = _ => Some("https://theia.com.br"),
    allowsMethods = _ => Some(Seq("GET", "POST")),
    allowsHeaders = _ => Some(Seq("Accept")),
    supportsCredentials = true
  )

  val filters = Function.chain(Seq(auth))
  val endpoints = Bootstrap.serve[Application.Json](apiV2 :+: api :+: apiV3).compile
  val compiled = filters(endpoints)
  val service = Endpoint.toService(compiled)

  val corsService: Service[Request, Response] = new Cors.HttpFilter(policy).andThen(service)

  Await.ready(Http.server.serve("0.0.0.0:8080", corsService))
}
