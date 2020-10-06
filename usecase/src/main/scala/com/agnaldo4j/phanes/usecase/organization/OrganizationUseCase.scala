package com.agnaldo4j.phanes.usecase.organization

import com.agnaldo4j.phanes.domain.Domain.Organization

object OrganizationUseCase extends Changeable {

  trait OrganizationResult

  case class Success(organization: Organization) extends OrganizationResult

  case class Fail(message: String) extends OrganizationResult
}
