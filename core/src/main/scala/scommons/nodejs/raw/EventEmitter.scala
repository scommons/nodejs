package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v9.x/api/events.html#events_class_eventemitter
  */
@js.native
trait EventEmitter extends js.Object {

  /**
    * Adds the listener function to the end of the listeners array for the event named eventName.
    * No checks are made to see if the listener has already been added.
    * Multiple calls passing the same combination of eventName and listener will result in the listener being added,
    * and called, multiple times.
    */
  def on(eventName: String, listener: js.Function): EventEmitter = js.native

  /**
    * Adds a one-time listener function for the event named eventName.
    * The next time eventName is triggered, this listener is removed and then invoked.
    */
  def once(eventName: String, listener: js.Function): EventEmitter = js.native

  /**
    * Alias for `emitter.on(eventName, listener)`.
    */
  def addListener(eventName: String, listener: js.Function): EventEmitter = js.native

  /**
    * Removes the specified listener from the listener array for the event named eventName.
    * 
    * Will remove, at most, one instance of a listener from the listener array.
    * If any single listener has been added multiple times to the listener array for the specified eventName,
    * then removeListener must be called multiple times to remove each instance.
    */
  def removeListener(eventName: String, listener: js.Function): EventEmitter = js.native

  /**
    * Synchronously calls each of the listeners registered for the event named `eventName`,
    * in the order they were registered, passing the supplied arguments to each.
    * 
    * @return `true` if the event had listeners, `false` otherwise
    */
  def emit(eventName: String, args: js.Any*): Boolean = js.native
}
