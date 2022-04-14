package scommons.nodejs

import org.scalactic.source.Position
import scommons.nodejs.test.TestSpec

import scala.scalajs.js

class BufferSpec extends TestSpec {

  it should "allocate new Buffer" in {
    //when & then
    Buffer.alloc(1).length shouldBe 1
    Buffer.allocUnsafe(2).length shouldBe 2
    Buffer.allocUnsafeSlow(3).length shouldBe 3
  }

  it should "return byteLength" in {
    //when & then
    Buffer.byteLength("test") shouldBe 4
  }

  it should "compare two buffers" in {
    //when & then
    Buffer.compare(Buffer.alloc(1, ' '.toInt), Buffer.alloc(2, ' '.toInt)) shouldBe -1
    Buffer.compare(Buffer.alloc(1, ' '.toInt), Buffer.alloc(1, ' '.toInt)) shouldBe 0
    Buffer.compare(Buffer.alloc(1, 'b'.toInt), Buffer.alloc(1, 'a'.toInt)) shouldBe 1
  }

  it should "concat buffers" in {
    //given
    val buffers = js.Array(Buffer.alloc(1, 'a'.toInt), Buffer.alloc(1, 'b'.toInt))

    //when & then
    Buffer.concat(buffers).toString shouldBe "ab"
  }

  it should "create new Buffer when from" in {
    //when & then
    Buffer.from(js.Array('a'.toShort, 'b'.toShort, 'c'.toShort)).toString shouldBe "abc"
    Buffer.from(Buffer.alloc(1, 'a'.toInt).buffer).toString shouldBe "a"
    Buffer.from(Buffer.alloc(2, 'a'.toInt)).toString shouldBe "aa"
    Buffer.from("abc").toString shouldBe "abc"
    Buffer.from("abc", "utf8").toString shouldBe "abc"
  }

  it should "return true if obj is a Buffer when isBuffer" in {
    //when & then
    Buffer.isBuffer(123) shouldBe false
    Buffer.isBuffer("test") shouldBe false
    Buffer.isBuffer(Buffer.from("abc").buffer) shouldBe false
    Buffer.isBuffer(Buffer.from("abc")) shouldBe true
  }

  it should "return true if supported character encoding when isEncoding" in {
    //when & then
    Buffer.isEncoding("") shouldBe false
    Buffer.isEncoding("123") shouldBe false
    Buffer.isEncoding("test") shouldBe false
    Buffer.isEncoding("utf/8") shouldBe false
    Buffer.isEncoding("hex") shouldBe true
    Buffer.isEncoding("utf-8") shouldBe true
    Buffer.isEncoding("utf8") shouldBe true
    Buffer.isEncoding("UTF8") shouldBe true
    Buffer.isEncoding("UTF-8") shouldBe true
  }

  it should "copy data from a region of buf to a region in target" in {
    //given
    val buf1 = Buffer.allocUnsafe(26)
    val buf2 = Buffer.allocUnsafe(26).fill('!'.toInt)
    
    for (i <- 0 until buf1.length) {
      buf1(i) = (i + 'a').toShort
    }
    
    //when
    buf1.copy(buf2, 8, 16, 20)

    //then
    buf2.toString("ascii", 0, 25) shouldBe "!!!!!!!!qrst!!!!!!!!!!!!!"
  }

  it should "copy data from one region to an overlapping region" in {
    //given
    val buf = Buffer.allocUnsafe(26)
    
    for (i <- 0 until buf.length) {
      buf(i) = (i + 'a').toShort
    }
    
    //when
    buf.copy(buf, 0, 4, 10)

    //then
    buf.toString shouldBe "efghijghijklmnopqrstuvwxyz"
  }

  it should "return true if includes sub-buffer content" in {
    //given
    val buf = Buffer.from("this is a buffer")
    
    //when & then
    buf.includes("this") shouldBe true
    buf.includes("is") shouldBe true
    buf.includes(Buffer.from("a buffer")) shouldBe true
    buf.includes('a'.toInt) shouldBe true
    buf.includes(Buffer.from("a buffer example")) shouldBe false
    buf.includes(Buffer.from("a buffer example").slice(0, 8)) shouldBe true
    buf.includes("this", 4) shouldBe false
  }

  it should "return index of the first occurrence of value" in {
    //given
    val buf = Buffer.from("this is a buffer")
    
    //when & then
    buf.indexOf("this") shouldBe 0
    buf.indexOf("is") shouldBe 2
    buf.indexOf(Buffer.from("a buffer")) shouldBe 8
    buf.indexOf('a'.toInt) shouldBe 8
    buf.indexOf(Buffer.from("a buffer example")) shouldBe -1
    buf.indexOf(Buffer.from("a buffer example").slice(0, 8)) shouldBe 8
    buf.indexOf("this", 4) shouldBe -1

    //given
    val utf16Buffer = Buffer.from("\u039a\u0391\u03a3\u03a3\u0395", "utf16le")
    
    //when & then
    utf16Buffer.indexOf("\u03a3", 0, "utf16le") shouldBe 4
    utf16Buffer.indexOf("\u03a3", -4, "utf16le") shouldBe 6
  }

  it should "return index of the last occurrence of value" in {
    //given
    val buf = Buffer.from("this buffer is a buffer")
    
    //when & then
    buf.lastIndexOf("this") shouldBe 0
    buf.lastIndexOf("buffer") shouldBe 17
    buf.lastIndexOf(Buffer.from("buffer")) shouldBe 17
    buf.lastIndexOf('a'.toInt) shouldBe 15
    buf.lastIndexOf(Buffer.from("yolo")) shouldBe -1
    buf.lastIndexOf("buffer", 5) shouldBe 5
    buf.lastIndexOf("buffer", 4) shouldBe -1
  }

  it should "read values" in {
    
    def buf(values: Short*): Buffer = {
      Buffer.from(js.Array[Short](values: _*))
    }
    
    //when & then
    buf(0, 0, 0, 0, 0, 0, 0, 1).readBigInt64BE() shouldBe js.BigInt(1)
    buf(1, 0, 0, 0, 0, 0, 0, 0).readBigInt64LE() shouldBe js.BigInt(1)
    buf(0, 0, 0, 0, 0xff, 0xff, 0xff, 0xff).readBigUInt64BE() shouldBe js.BigInt(4294967295d)
    buf(0, 0, 0, 0, 0xff, 0xff, 0xff, 0xff).readBigUInt64LE() shouldBe js.BigInt(18446744069414584320d)
    buf(1, 2, 3, 4, 5, 6, 7, 8).readDoubleBE() shouldBe 8.20788039913184e-304
    buf(1, 2, 3, 4, 5, 6, 7, 8).readDoubleLE() shouldBe 5.447603722011605e-270
    buf(1, 2, 3, 4).readFloatBE() shouldBe 2.387939260590663e-38
    buf(1, 2, 3, 4).readFloatLE() shouldBe 1.539989614439558e-36
    buf(5).readInt8() shouldBe 5
    buf(0, 5).readInt16BE() shouldBe 5
    buf(0, 5).readInt16LE() shouldBe 1280
    buf(0, 0, 0, 5).readInt32BE() shouldBe 5
    buf(0, 0, 0, 5).readInt32LE() shouldBe 83886080
    buf(0x12, 0x34, 0x56, 0x78, 0x90, 0xab).readIntBE(0, 6) shouldBe 20015998341291d
    buf(0x12, 0x34, 0x56, 0x78, 0x90, 0xab).readIntLE(0, 6) shouldBe -92837994154990d
    buf(1, -2).readUInt8(1) shouldBe 254
    buf(0x12, 0x34, 0x56).readUInt16BE().toHexString shouldBe "1234"
    buf(0x12, 0x34, 0x56).readUInt16LE().toHexString shouldBe "3412"
    buf(0x12, 0x34, 0x56, 0x78).readUInt32BE().toLong.toHexString shouldBe "12345678"
    buf(0x12, 0x34, 0x56, 0x78).readUInt32LE().toLong.toHexString shouldBe "78563412"
    buf(0x12, 0x34, 0x56, 0x78, 0x90, 0xab).readUIntBE(0, 6) shouldBe 20015998341291d
    buf(0x12, 0x34, 0x56, 0x78, 0x90, 0xab).readUIntLE(0, 6) shouldBe 188636982555666d
  }

  it should "return new Buffer when subarray" in {
    //given
    val buf = Buffer.allocUnsafe(26)

    for (i <- 0 until buf.length) {
      buf(i) = (i + 'a').toShort
    }

    //when & then
    buf.subarray(0, 3).toString("ascii") shouldBe "abc"
  }

  it should "swap the byte order of unsigned 16-bit integers in-place" in {
    //given
    val buf = Buffer.from(js.Array[Short](0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8))

    //when
    buf.swap16()
    
    //then
    buf.toString("hex") shouldBe "0201040306050807"
  }

  it should "swap the byte order of unsigned 32-bit integers in-place" in {
    //given
    val buf = Buffer.from(js.Array[Short](0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8))

    //when
    buf.swap32()
    
    //then
    buf.toString("hex") shouldBe "0403020108070605"
  }

  it should "swap the byte order of unsigned 64-bit integers in-place" in {
    //given
    val buf = Buffer.from(js.Array[Short](0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8))

    //when
    buf.swap64()
    
    //then
    buf.toString("hex") shouldBe "0807060504030201"
  }

  it should "write string" in {
    //given
    val buf = Buffer.alloc(10)
    
    //when
    val result = buf.write("abcd", 8)
    
    //then
    result shouldBe 2
    buf.toString("utf8", 8, 10) shouldBe "ab"
  }

  it should "write values" in {
    
    def check(size: Int)(test: Buffer => Int, bytes: Int, expected: String)(implicit pos: Position): Unit = {
      val buf = Buffer.allocUnsafe(size)
      test(buf) shouldBe bytes
      buf.toString("hex") shouldBe expected
    }

    //when & then
    check(8)(_.writeBigInt64BE(js.BigInt("0x0102030405060708")), 8, "0102030405060708")
    check(8)(_.writeBigInt64LE(js.BigInt("0x0102030405060708")), 8, "0807060504030201")
    check(8)(_.writeBigUInt64BE(js.BigInt("0xdecafafecacefade")), 8, "decafafecacefade")
    check(8)(_.writeBigUInt64LE(js.BigInt("0xdecafafecacefade")), 8, "defacecafefacade")
    check(8)(_.writeDoubleBE(123.456), 8, "405edd2f1a9fbe77")
    check(8)(_.writeDoubleLE(123.456), 8, "77be9f1a2fdd5e40")
    check(4)(_.writeFloatBE(0xcafebabe), 4, "ce540515")
    check(4)(_.writeFloatLE(0xcafebabe), 4, "150554ce")
    check(2)({ b => b.writeInt8(2); b.writeInt8(-2, 1)}, 2, "02fe")
    check(2)(_.writeInt16BE(0x0102), 2, "0102")
    check(2)(_.writeInt16LE(0x0304), 2, "0403")
    check(4)(_.writeInt32BE(0x01020304), 4, "01020304")
    check(4)(_.writeInt32LE(0x05060708), 4, "08070605")
    check(6)(_.writeIntBE(20015998341291d, 0, 6), 6, "1234567890ab")
    check(6)(_.writeIntLE(20015998341291d, 0, 6), 6, "ab9078563412")
    check(2)({ b => b.writeUInt8(2); b.writeUInt8(254, 1)}, 2, "02fe")
    check(2)(_.writeUInt16BE(0xf102), 2, "f102")
    check(2)(_.writeUInt16LE(0xf102), 2, "02f1")
    check(4)(_.writeUInt32BE(0xfeedfaceL), 4, "feedface")
    check(4)(_.writeUInt32LE(0xfeedfaceL), 4, "cefaedfe")
    check(6)(_.writeUIntBE(20015998341291d, 0, 6), 6, "1234567890ab")
    check(6)(_.writeUIntLE(20015998341291d, 0, 6), 6, "ab9078563412")
  }
}
