package com.agnaldo4j.phanes.usecase

import com.agnaldo4j.phanes.domain.{Organization, Person, Value}

object OrganizationUseCase {

  def execute(command: OrganizationCommand): OrganizationResult = {
    command match {
      case AddValue(name, organization) =>
        executeAddValue(name, organization)
      case AddPerson(name, organization) =>
        executeAddPerson(name, organization)
      case _ => Fail(s"Command not found ${command}")
    }
  }

  private def executeAddValue(name: String, organization: Organization) = {
    val value = Value(name)
    val newState =
      organization.copy(values = organization.values ++ List(value))
    Success(newState)
  }

  private def executeAddPerson(name: String, organization: Organization) = {
    val person = Person(name)
    val newState = organization.copy(people =
      organization.people ++ Map(person.id -> person)
    )
    Success(newState)
  }

  trait OrganizationCommand

  case class AddValue(name: String, organization: Organization)
      extends OrganizationCommand

  case class AddPerson(name: String, organization: Organization)
      extends OrganizationCommand

  trait OrganizationResult

  case class Success(organization: Organization) extends OrganizationResult

  case class Fail(message: String) extends OrganizationResult
}
