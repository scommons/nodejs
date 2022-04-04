package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

/**
  * https://nodejs.org/docs/latest-v12.x/api/child_process.html
  */
@js.native
@JSImport("child_process", JSImport.Default)
object ChildProcess extends js.Object {

  def exec(command: String,
           options: js.UndefOr[ChildProcessOptions] = js.native,
           callback: js.UndefOr[js.Function3[js.Error, js.Object, js.Object, Unit]] = js.native
          ): ChildProcess = js.native

  def spawn(command: String,
            args: js.UndefOr[js.Array[String]] = js.native,
            options: js.UndefOr[ChildProcessOptions] = js.native
           ): ChildProcess = js.native
  
}

@js.native
trait ChildProcess extends EventEmitter {

  val spawnargs: js.Array[String] = js.native
  val spawnfile: String = js.native
  val exitCode: js.UndefOr[Int] = js.native

  val stdin: Writable = js.native
  val stdout: Readable = js.native
  val stderr: Readable = js.native
  
  def kill(signal: js.UndefOr[Int | String] = js.native): Boolean = js.native
}

trait ChildProcessOptions extends js.Object {

  val cwd: js.UndefOr[String] = js.undefined
  val env: js.UndefOr[js.Object] = js.undefined
  val encoding: js.UndefOr[String] = js.undefined
  val shell: js.UndefOr[Boolean | String] = js.undefined
  val windowsHide: js.UndefOr[Boolean] = js.undefined
  
  val detached: js.UndefOr[Boolean] = js.undefined
  val stdio: js.UndefOr[String | js.Array[js.Any]] = js.undefined
}
