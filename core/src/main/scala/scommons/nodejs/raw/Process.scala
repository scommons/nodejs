package scommons.nodejs.raw

import scommons.nodejs.Process.Platform

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v12.x/api/process.html
  */
@js.native
trait Process extends js.Object {

  val stdin: EventEmitter = js.native
  val platform: Platform = js.native
  
  def chdir(directory: String): Unit = js.native
  
  def cwd(): String = js.native
  
  def exit(code: Int): Unit = js.native
}
