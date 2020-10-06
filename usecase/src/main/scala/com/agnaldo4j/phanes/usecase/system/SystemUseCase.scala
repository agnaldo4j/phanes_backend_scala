package com.agnaldo4j.phanes.usecase.system

import com.agnaldo4j.phanes.domain.Domain.{Organization, System}

object SystemUseCase extends Changeable with Queryable {

  trait SystemResult

  case class Success(system: System) extends SystemResult

  case class QuerySuccess(organization: Option[Organization])
      extends SystemResult

  case class Fail(message: String) extends SystemResult

}
