package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array

/**
 * https://nodejs.org/docs/latest-v12.x/api/stream.html#stream_class_stream_readable
 */
@js.native
trait Readable extends EventEmitter {

  def pause(): Readable = js.native
  def resume(): Readable = js.native

  def read(size: js.UndefOr[Int] = js.native): Uint8Array = js.native

  def destroy(error: js.UndefOr[js.Error] = js.native): Readable = js.native
}
