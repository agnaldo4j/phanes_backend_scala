package com.agnaldo4j.phanes.usecase

import com.agnaldo4j.phanes.domain.Domain.Id
import com.agnaldo4j.phanes.domain.{Organization, System}

object SystemUseCase {

  def execute(command: SystemCommand): SystemResult = {
    command match {
      case AddOrganization(name, system) =>
        executeAddOrganization(name, system)
      case _ => Fail(s"Command not found ${command}")
    }
  }

  def execute(query: SystemQuery): SystemResult = {
    query match {
      case GetOrganizationByName(name, system) =>
        executeGetOrganizationByName(name, system)
      case GetOrganizationById(id, system) =>
        executeGetOrganizationById(id, system)
      case _ => Fail(s"Command not found: ${query}")
    }
  }

  private def executeGetOrganizationById(id: Id, system: System) =
    QuerySuccess(
      system.organizations.get(id)
    )

  private def executeGetOrganizationByName(name: String, system: System) =
    QuerySuccess(
      system.organizations.values.find { organization =>
        organization.name == name
      }
    )

  private def executeAddOrganization(name: String, system: System) = {
    val organization = Organization(name)
    val newState = system.copy(
      organizations =
        system.organizations ++ Map(organization.id -> organization)
    )
    Success(newState)
  }

  trait SystemCommand

  case class AddOrganization(name: String, system: System) extends SystemCommand

  trait SystemQuery

  case class GetOrganizationByName(name: String, system: System)
      extends SystemQuery

  case class GetOrganizationById(id: Id, system: System) extends SystemQuery

  trait SystemResult

  case class Success(system: System) extends SystemResult

  case class QuerySuccess(organization: Option[Organization])
      extends SystemResult

  case class Fail(message: String) extends SystemResult

}
