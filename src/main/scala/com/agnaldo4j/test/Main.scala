package com.agnaldo4j.test

import cats.effect.IO
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Response, Status}
import com.twitter.util.Await
import io.finch._

object Main extends App with Endpoint.Module[IO] {

  val auth: Endpoint.Compiled[IO] => Endpoint.Compiled[IO] = compiled => {
    Endpoint.Compiled[IO] {
      case req if req.getParam("user", "teste") != "teste" => compiled(req)
      case req if req.uri == "/v2/hello" => compiled(req)
      case _ => IO.pure(Trace.empty -> Right(Response(Status.Unauthorized)))
    }
  }

  val api: Endpoint[IO, String] = get("hello") {
    Ok("Hello, World!")
  }

  val apiV2: Endpoint[IO, String] = get("v2" :: "hello") {
    Ok("Hello, World V2!")
  }

  val filters = Function.chain(Seq(auth))
  val compiled = filters(Bootstrap.serve[Text.Plain](apiV2 :+: api).compile)


  Await.ready(Http.server.serve("0.0.0.0:8080", Endpoint.toService(compiled)))
}
