package com.agnaldo4j.phanes.api

import cats.effect.IO
import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.Organization
import com.agnaldo4j.phanes.domain.StorableEvent.AddOrganization
import com.agnaldo4j.phanes.persistence.relational.PersistenceConfig
import com.twitter.finagle.http.cookie.SameSite
import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.http.{Cookie, Request, Response, Status}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import org.springframework.context.annotation.AnnotationConfigApplicationContext

object Main extends App with Endpoint.Module[IO] {

  val context: AnnotationConfigApplicationContext =
    new AnnotationConfigApplicationContext()

  context.scan("com.agnaldo4j.phanes")
  context.register(classOf[PersistenceConfig])
  context.refresh()

  val auth: Endpoint.Compiled[IO] => Endpoint.Compiled[IO] = compiled => {
    Endpoint.Compiled[IO] {
      case req if req.getParam("user", "teste") != "teste" =>
        compiled(req)
      case req if req.uri == "/v2/hello" =>
        compiled(req)
      case _ =>
        IO.pure(Trace.empty -> Right(Response(Status.Unauthorized)))
    }
  }

  val api: Endpoint[IO, Organization] = get("hello") {
    val cookie = new Cookie(
      name = "token",
      value = "teste",
      httpOnly = true,
      sameSite = SameSite.Lax
    )

    context.getBean(classOf[Storage]).log(AddOrganization("Nova"))

    Ok(
      Organization(name = "Teste")
    ).withCookie(cookie)
  }

  val apiV2: Endpoint[IO, Organization] = get("v2" :: path[String]) {
    title: String =>
      context.getBean(classOf[Storage]).load()
      Ok(
        Organization(
          name = "Teste"
        )
      )
  }

  val apiV3: Endpoint[IO, Organization] =
    get("v3" :: path[String] :: path[Int]) { (title: String, age: Int) =>
      Ok(
        Organization(
          name = title
        )
      )
    }

  val policy: Cors.Policy = Cors.Policy(
    allowsOrigin = _ => Some("https://theia.com.br"),
    allowsMethods = _ => Some(Seq("GET", "POST")),
    allowsHeaders = _ => Some(Seq("Accept")),
    supportsCredentials = true
  )

  val filters = Function.chain(Seq(auth))
  val endpoints =
    Bootstrap.serve[Application.Json](apiV2 :+: api :+: apiV3).compile
  val compiled = filters(endpoints)
  val service = Endpoint.toService(compiled)

  val corsService: Service[Request, Response] =
    new Cors.HttpFilter(policy).andThen(service)

  Await.ready(Http.server.serve("0.0.0.0:8080", corsService))
}
