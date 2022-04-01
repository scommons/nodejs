package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v9.x/api/timers.html
  */
@js.native
trait Timers extends js.Object {

  /**
   * Schedules the "immediate" execution of the callback after I/O events' callbacks.
   * Returns an Immediate for use with clearImmediate().
   */
  def setImmediate(callback: js.Function0[Any]): Immediate = js.native

  /**
    * Schedules repeated execution of callback every delay milliseconds.
    * Returns a Timeout for use with clearInterval().
    */
  def setInterval(callback: js.Function0[Any], delay: Double): Timeout = js.native

  /**
    * Schedules execution of a one-time callback after delay milliseconds.
    * Returns a Timeout for use with clearTimeout().
    */
  def setTimeout(callback: js.Function0[Any], delay: Double): Timeout = js.native

  /**
    * Cancels an Immediate object created by setImmediate().
    */
  def clearImmediate(immediate: Immediate): Unit = js.native

  /**
    * Cancels a Timeout object created by setInterval().
    */
  def clearInterval(timeout: Timeout): Unit = js.native

  /**
    * Cancels a Timeout object created by setTimeout().
    */
  def clearTimeout(timeout: Timeout): Unit = js.native
}

@js.native
trait Immediate extends js.Object

@js.native
trait Timeout extends js.Object
