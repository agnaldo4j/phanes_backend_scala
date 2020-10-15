package com.agnaldo4j.phanes.eventbus.system

import com.agnaldo4j.phanes.domain.Domain.{Id, Organization, System}
import com.agnaldo4j.phanes.eventbus.QueryableEvent.{
  GetOrganizationById,
  GetOrganizationByName,
  QueryableEvent
}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase
import com.agnaldo4j.phanes.usecase.system.SystemUseCase.{Fail, SingleResult}
import com.agnaldo4j.phanes.usecase.system.{
  SystemQuery,
  GetOrganizationById => GetOrganizationByIdQuery,
  GetOrganizationByName => GetOrganizationByNameQuery
}

trait SystemQueryable {
  var system: System

  def execute(event: QueryableEvent): QueryResult = {
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
      case SingleResult(result) =>
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
