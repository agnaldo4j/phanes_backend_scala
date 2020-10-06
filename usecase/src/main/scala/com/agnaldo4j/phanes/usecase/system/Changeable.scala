package com.agnaldo4j.phanes.usecase.system

import com.agnaldo4j.phanes.domain.Domain.{Id, Organization, System}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase._

trait Changeable {
  def execute(command: SystemCommand): SystemResult = {
    command match {
      case AddOrganization(name, system) =>
        executeAddOrganization(name, system)
      case DeleteOrganization(id, system) =>
        executeDeleteOrganization(id, system)
      case _ => Fail(s"Command not found ${command}")
    }
  }

  private def executeAddOrganization(name: String, system: System) = {
    val organization = Organization(name)
    val newState = system.copy(
      organizations =
        system.organizations ++ Map(organization.id -> organization)
    )
    Success(newState)
  }

  private def executeDeleteOrganization(
      id: Id,
      system: System
  ): SystemResult = {
    system.organizations.get(id) match {
      case Some(_) =>
        val newState =
          system.copy(organizations = system.organizations.removed(id))
        Success(newState)
      case None => Fail(s"Organization not found with id: $id")
    }
  }

  trait SystemCommand

  case class AddOrganization(name: String, system: System) extends SystemCommand

  case class DeleteOrganization(id: Id, system: System) extends SystemCommand
}
