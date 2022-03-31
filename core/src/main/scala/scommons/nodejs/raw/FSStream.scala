package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v12.x/api/fs.html#fs_class_fs_writestream
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

/**
  * https://nodejs.org/docs/latest-v12.x/api/fs.html#fs_class_fs_readstream
  */
@js.native
trait ReadStream extends Readable

trait CreateReadStreamOptions extends js.Object {

  val flags: js.UndefOr[String] = js.undefined
  val encoding: js.UndefOr[String] = js.undefined
  val fd: js.UndefOr[Int] = js.undefined
  val mode: js.UndefOr[Int] = js.undefined
  val autoClose: js.UndefOr[Boolean] = js.undefined
  val emitClose: js.UndefOr[Boolean] = js.undefined
  val start: js.UndefOr[Int] = js.undefined
  val end: js.UndefOr[Int] = js.undefined
  val highWaterMark: js.UndefOr[Int] = js.undefined
}
