package com.agnaldo4j.phanes.eventbus.system

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.StorableEvent.{
  AddOrganization,
  DeleteOrganization,
  StorableEvent
}
import com.agnaldo4j.phanes.usecase.system.SystemUseCase.{Fail, Success}
import com.agnaldo4j.phanes.usecase.system.{
  SystemUseCase,
  AddOrganization => AddOrganizationCommand,
  DeleteOrganization => DeleteOrganizationCommand
}

trait SystemChangeable {
  var system: System
  val storage: Storage

  def execute(event: StorableEvent): EventResult = {
    storage.log(event)
    event match {
      case AddOrganization(name) =>
        executeAddOrganization(AddOrganizationCommand(name, system))
      case DeleteOrganization(id) =>
        executeDeleteOrganization(DeleteOrganizationCommand(id, system))
      case _ => EventResultFail(s"Event not found: $event")
    }
  }

  private def executeAddOrganization(
      command: AddOrganizationCommand
  ): EventResult = {
    SystemUseCase.execute(command) match {
      case Success(system) =>
        this.system = system
        AddOrganizationSuccess
      case Fail(message) => EventResultFail(message)
    }
  }

  private def executeDeleteOrganization(
      command: DeleteOrganizationCommand
  ): EventResult = {
    SystemUseCase.execute(command) match {
      case Success(system) =>
        this.system = system
        DeleteOrganizationSuccess
      case Fail(message) => EventResultFail(message)
    }
  }

  trait EventResult

  object AddOrganizationSuccess extends EventResult

  object DeleteOrganizationSuccess extends EventResult

  case class EventResultFail(message: String) extends EventResult
}
