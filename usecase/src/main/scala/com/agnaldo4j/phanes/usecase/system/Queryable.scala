package com.agnaldo4j.phanes.usecase.system

import com.agnaldo4j.phanes.domain.Domain.{Id, System}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase.{
  Fail,
  QuerySuccess,
  SystemResult
}

trait Queryable {
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

  trait SystemQuery

  case class GetOrganizationByName(name: String, system: System)
      extends SystemQuery

  case class GetOrganizationById(id: Id, system: System) extends SystemQuery
}
