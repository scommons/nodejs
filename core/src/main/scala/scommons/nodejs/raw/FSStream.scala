package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v9.x/api/fs.html#fs_class_fs_writestream
  */
@js.native
trait WriteStream extends Writable

trait CreateWriteStreamOptions extends js.Object {

  val flags: js.UndefOr[String] = js.undefined
  val encoding: js.UndefOr[String] = js.undefined
  val fd: js.UndefOr[Int] = js.undefined
  val mode: js.UndefOr[Int] = js.undefined
  val autoClose: js.UndefOr[Boolean] = js.undefined
  val start: js.UndefOr[Int] = js.undefined
}
