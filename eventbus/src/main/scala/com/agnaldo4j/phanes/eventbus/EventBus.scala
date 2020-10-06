package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.domain.Domain.Id
import com.agnaldo4j.phanes.domain.{Organization, System}
import com.agnaldo4j.phanes.usecase.SystemUseCase
import com.agnaldo4j.phanes.usecase.SystemUseCase.{
  Fail,
  QuerySuccess,
  Success,
  SystemQuery,
  AddOrganization => AddOrganizationCommand,
  GetOrganizationById => GetOrganizationByIdQuery,
  GetOrganizationByName => GetOrganizationByNameQuery
}

class EventBus() {
  var system: System = System()

  def execute(event: Event): Result = {
    event match {
      case AddOrganization(name) =>
        executeAddOrganization(AddOrganizationCommand(name, system))
      case GetOrganizationByName(name) =>
        executeGetOrganizationByName(GetOrganizationByNameQuery(name, system))
      case GetOrganizationById(id) =>
        executeGetOrganizationById(GetOrganizationByIdQuery(id, system))
      case _ => EventBusFail(s"Event not found: ${event}")
    }
  }

  private def executeAddOrganization(
      command: AddOrganizationCommand
  ): Result = {
    SystemUseCase.execute(command) match {
      case Success(system) =>
        this.system = system
        AddOrganizationSuccess
      case Fail(message) => EventBusFail(message)
    }
  }

  private def executeGetOrganizationByName(
      query: GetOrganizationByNameQuery
  ): Result = executeGetOrganization(query)

  private def executeGetOrganizationById(
      query: GetOrganizationByIdQuery
  ): Result = executeGetOrganization(query)

  private def executeGetOrganization(query: SystemQuery): Result = {
    SystemUseCase.execute(query) match {
      case QuerySuccess(result) =>
        result match {
          case Some(organization) => GetOrganizationResult(organization)
          case None               => GetOrganizationEmptyResult
        }
      case Fail(message) => EventBusFail(message)
    }
  }

  trait Event

  case class AddOrganization(name: String) extends Event

  case class GetOrganizationByName(name: String) extends Event

  case class GetOrganizationById(id: Id) extends Event

  case class DeleteOrganization(id: Id) extends Event

  trait Result

  object AddOrganizationSuccess extends Result

  case class GetOrganizationResult(organization: Organization) extends Result

  object GetOrganizationEmptyResult extends Result

  case class EventBusFail(message: String) extends Result

}
