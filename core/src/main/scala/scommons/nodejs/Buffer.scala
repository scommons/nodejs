package scommons.nodejs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}
import scala.scalajs.js.|

/**
  * https://nodejs.org/docs/latest-v12.x/api/buffer.html
  */
@js.native
@JSGlobal
object Buffer extends js.Object {

  def alloc(size: Int,
            fill: js.UndefOr[String | Buffer | Uint8Array | Int] = js.native,
            encoding: js.UndefOr[String] = js.native): Buffer = js.native
  def allocUnsafe(size: Int): Buffer = js.native
  def allocUnsafeSlow(size: Int): Buffer = js.native
  
  def byteLength(string: String | Buffer | Uint8Array | ArrayBuffer,
                 encoding: js.UndefOr[String] = js.native): Int = js.native

  def compare(buf1: Buffer | Uint8Array,
              buf2: Buffer | Uint8Array): Int = js.native

  def concat(list: js.Array[Buffer] | js.Array[Uint8Array],
             totalLength: Int = js.native): Buffer = js.native

  def from(arrayBuffer: ArrayBuffer,
           byteOffset: js.UndefOr[Int] = js.native,
           length: js.UndefOr[Int] = js.native): Buffer = js.native
  def from(from: String | js.Array[Short] | Buffer | Uint8Array): Buffer = js.native
  def from(string: String, encoding: String): Buffer = js.native

  def isBuffer(obj: js.Any): Boolean = js.native
  def isEncoding(encoding: String): Boolean = js.native
}

@js.native
trait Buffer extends Uint8Array {

  def copy(target: Buffer | Uint8Array,
           targetStart: Int = js.native,
           sourceStart: Int = js.native,
           sourceEnd: Int = js.native): Int = js.native

  override def subarray(start: Int = js.native, end: Int = js.native): Buffer = js.native

  def slice(start: Int = js.native, end: Int = js.native): Buffer = js.native

  def swap16(): Buffer = js.native
  def swap32(): Buffer = js.native
  def swap64(): Buffer = js.native

  def fill(value: String | Buffer | Uint8Array | Int,
           offset: Int = js.native,
           end: Int = js.native,
           encoding: String = js.native): Buffer = js.native

  def includes(value: String | Buffer | Uint8Array | Int,
               byteOffset: Int = js.native,
               encoding: String = js.native): Boolean = js.native

  def indexOf(value: String | Buffer | Uint8Array | Int,
              byteOffset: Int = js.native,
              encoding: String = js.native): Int = js.native

  def lastIndexOf(value: String | Buffer | Uint8Array | Int,
                  byteOffset: Int = js.native,
                  encoding: String = js.native): Int = js.native
  
  def readBigInt64BE(offset: Int = js.native): js.BigInt = js.native
  def readBigInt64LE(offset: Int = js.native): js.BigInt = js.native
  def readBigUInt64BE(offset: Int = js.native): js.BigInt = js.native
  def readBigUInt64LE(offset: Int = js.native): js.BigInt = js.native
  def readDoubleBE(offset: Int = js.native): Double = js.native
  def readDoubleLE(offset: Int = js.native): Double = js.native
  def readFloatBE(offset: Int = js.native): Float = js.native
  def readFloatLE(offset: Int = js.native): Float = js.native
  def readInt8(offset: Int = js.native): Short = js.native
  def readInt16BE(offset: Int = js.native): Short = js.native
  def readInt16LE(offset: Int = js.native): Short = js.native
  def readInt32BE(offset: Int = js.native): Int = js.native
  def readInt32LE(offset: Int = js.native): Int = js.native
  def readIntBE(offset: Int, byteLength: Int): Double = js.native
  def readIntLE(offset: Int, byteLength: Int): Double = js.native
  def readUInt8(offset: Int = js.native): Short = js.native
  def readUInt16BE(offset: Int = js.native): Int = js.native
  def readUInt16LE(offset: Int = js.native): Int = js.native
  def readUInt32BE(offset: Int = js.native): Double = js.native
  def readUInt32LE(offset: Int = js.native): Double = js.native
  def readUIntBE(offset: Int, byteLength: Int): Double = js.native
  def readUIntLE(offset: Int, byteLength: Int): Double = js.native

  def write(string: String,
            offset: Int = js.native,
            length: Int = js.native,
            encoding: String = js.native): Int = js.native

  def writeBigInt64BE(value: js.BigInt, offset: Int = js.native): Int = js.native
  def writeBigInt64LE(value: js.BigInt, offset: Int = js.native): Int = js.native
  def writeBigUInt64BE(value: js.BigInt, offset: Int = js.native): Int = js.native
  def writeBigUInt64LE(value: js.BigInt, offset: Int = js.native): Int = js.native
  def writeDoubleBE(value: Double, offset: Int = js.native): Int = js.native
  def writeDoubleLE(value: Double, offset: Int = js.native): Int = js.native
  def writeFloatBE(value: Float, offset: Int = js.native): Int = js.native
  def writeFloatLE(value: Float, offset: Int = js.native): Int = js.native
  def writeInt8(value: Short, offset: Int = js.native): Int = js.native
  def writeInt16BE(value: Short, offset: Int = js.native): Int = js.native
  def writeInt16LE(value: Short, offset: Int = js.native): Int = js.native
  def writeInt32BE(value: Int, offset: Int = js.native): Int = js.native
  def writeInt32LE(value: Int, offset: Int = js.native): Int = js.native
  def writeIntBE(value: Double, offset: Int, byteLength: Int): Int = js.native
  def writeIntLE(value: Double, offset: Int, byteLength: Int): Int = js.native
  def writeUInt8(value: Short, offset: Int = js.native): Int = js.native
  def writeUInt16BE(value: Int, offset: Int = js.native): Int = js.native
  def writeUInt16LE(value: Int, offset: Int = js.native): Int = js.native
  def writeUInt32BE(value: Double, offset: Int = js.native): Int = js.native
  def writeUInt32LE(value: Double, offset: Int = js.native): Int = js.native
  def writeUIntBE(value: Double, offset: Int, byteLength: Int): Int = js.native
  def writeUIntLE(value: Double, offset: Int, byteLength: Int): Int = js.native

  override def toString: String = js.native

  def toString(encoding: String,
               start: Int = js.native,
               end: Int = js.native): String = js.native
}
