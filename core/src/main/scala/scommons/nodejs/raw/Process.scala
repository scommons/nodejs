package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v9.x/api/process.html
  */
@js.native
trait Process extends js.Object {

  def chdir(directory: String): Unit = js.native
  
  def cwd(): String = js.native
  
  def exit(code: Int): Unit = js.native
}
