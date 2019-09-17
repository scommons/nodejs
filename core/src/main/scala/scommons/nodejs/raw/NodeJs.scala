package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
trait NodeJs extends Timers

@js.native
@JSGlobal("global")
object NodeJs extends NodeJs {
  
  val process: Process = js.native
}
