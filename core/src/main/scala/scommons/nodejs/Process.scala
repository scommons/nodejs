package scommons.nodejs

import scommons.nodejs.Process._
import scommons.nodejs.raw.NodeJs.{process => native}

import scala.scalajs.js

trait Process {

  val stdin: raw.EventEmitter = native.stdin
  val platform: Platform = native.platform

  def chdir(directory: String): Unit = native.chdir(directory)

  def cwd(): String = native.cwd()

  def exit(code: Int): Unit = native.exit(code)
}

object Process extends Process {

  trait Platform extends js.Object

  object Platform {
    val aix: Platform = "aix".asInstanceOf[Platform]
    val darwin: Platform = "darwin".asInstanceOf[Platform]
    val freebsd: Platform = "freebsd".asInstanceOf[Platform]
    val linux: Platform = "linux".asInstanceOf[Platform]
    val openbsd: Platform = "openbsd".asInstanceOf[Platform]
    val sunos: Platform = "sunos".asInstanceOf[Platform]
    val win32: Platform = "win32".asInstanceOf[Platform]
  }

}
