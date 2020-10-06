package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.Event.{AddOrganization, Event}
import com.agnaldo4j.phanes.usecase.SystemUseCase
import com.agnaldo4j.phanes.usecase.SystemUseCase.{
  Fail,
  Success,
  AddOrganization => AddOrganizationCommand
}

trait Changeable {
  var system: System
  val storage: Storage

  def execute(event: Event): EventResult = {
    val result = event match {
      case AddOrganization(name) =>
        executeAddOrganization(AddOrganizationCommand(name, system))
      case _ => EventResultFail(s"Event not found: $event")
    }
    storage.log(event)
    result
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

  trait EventResult

  object AddOrganizationSuccess extends EventResult

  case class EventResultFail(message: String) extends EventResult
}
