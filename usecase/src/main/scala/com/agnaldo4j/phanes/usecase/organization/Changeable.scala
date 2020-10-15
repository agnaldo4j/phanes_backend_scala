package com.agnaldo4j.phanes.usecase.organization

import com.agnaldo4j.phanes.domain.Domain.{Organization, Person, Value}
import com.agnaldo4j.phanes.usecase.organization.OrganizationUseCase.{
  Fail,
  OrganizationResult,
  Success
}

trait Changeable {
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
    val value = Value(name = name)
    val newState =
      organization.copy(values = organization.values ++ List(value))
    Success(newState)
  }

  private def executeAddPerson(name: String, organization: Organization) = {
    val person = Person(name = name)
    val newState = organization.copy(people =
      organization.people ++ Map(person.id -> person)
    )
    Success(newState)
  }
}

trait OrganizationCommand

case class AddValue(name: String, organization: Organization)
    extends OrganizationCommand

case class AddPerson(name: String, organization: Organization)
    extends OrganizationCommand
