package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.domain.Domain.{Organization, System}
import com.agnaldo4j.phanes.domain.Event.{
  GetOrganizationById,
  GetOrganizationByName,
  QueryEvent
}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase.{
  Fail,
  QuerySuccess,
  SystemQuery,
  GetOrganizationById => GetOrganizationByIdQuery,
  GetOrganizationByName => GetOrganizationByNameQuery
}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase

trait Queryable {
  var system: System

  def execute(event: QueryEvent): QueryResult = {
    event match {
      case GetOrganizationByName(name) =>
        executeGetOrganizationByName(GetOrganizationByNameQuery(name, system))
      case GetOrganizationById(id) =>
        executeGetOrganizationById(GetOrganizationByIdQuery(id, system))
      case _ => QueryResultFail(s"Event not found: ${event}")
    }
  }

  private def executeGetOrganizationByName(
      query: GetOrganizationByNameQuery
  ): QueryResult = executeGetOrganization(query)

  private def executeGetOrganizationById(
      query: GetOrganizationByIdQuery
  ): QueryResult = executeGetOrganization(query)

  private def executeGetOrganization(query: SystemQuery): QueryResult = {
    SystemUseCase.execute(query) match {
      case QuerySuccess(result) =>
        result match {
          case Some(organization) => GetOrganizationResult(organization)
          case None               => GetOrganizationEmptyResult
        }
      case Fail(message) => QueryResultFail(message)
    }
  }

  trait QueryResult

  case class GetOrganizationResult(organization: Organization)
      extends QueryResult

  object GetOrganizationEmptyResult extends QueryResult

  case class QueryResultFail(message: String) extends QueryResult
}
