package scommons.nodejs.util

import scommons.nodejs._
import scommons.nodejs.raw.CreateReadStreamOptions
import scommons.nodejs.stream.Readable
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Future
import scala.scalajs.js

class StreamReaderSpec extends AsyncTestSpec {

  it should "fail with error when readNextBytes" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    val readable = fs.createReadStream(file)
    val reader = new StreamReader(readable)
    
    def loop(result: String): Future[String] = {
      reader.readNextBytes(5).flatMap {
        case None => Future.successful(result)
        case Some(content) =>

          //when
          readable.destroy(js.Error("test error"))

          loop(result + content.toString)
      }
    }
    val resultF = loop("")
    
    //then
    resultF.failed.map { ex =>
      ex.toString should include ("test error")
    }.andThen { case _ =>
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "end stream without error when readNextBytes" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    val readable = fs.createReadStream(file, new CreateReadStreamOptions {
      override val highWaterMark: js.UndefOr[Int] = 5
    })
    val reader = new StreamReader(readable)
    
    def loop(result: String): Future[String] = {
      reader.readNextBytes(5).flatMap {
        case None => Future.successful(result)
        case Some(content) =>
          content.length shouldBe 5

          //when
          readable.destroy()

          loop(result + content.toString)
      }
    }
    val resultF = loop("")
    
    //then
    resultF.map { result =>
      result shouldBe "hello"
    }.andThen { case _ =>
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "read data when readNextBytes" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    val reader = new StreamReader(fs.createReadStream(file, new CreateReadStreamOptions {
      override val highWaterMark: js.UndefOr[Int] = 2
    }))
    
    def loop(result: String): Future[String] = {
      reader.readNextBytes(4).flatMap {
        case None => Future.successful(result)
        case Some(content) =>
          content.length should be <= 4
          loop(result + content.toString)
      }
    }
    
    //when
    val resultF = loop("")
    
    //then
    resultF.map { result =>
      result shouldBe "hello, World!!!"
    }.andThen { case _ =>
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "read all lines when readAllLines" in {
    //given
    val expectedContent =
      """some long text
        |,
        |
        |more text at the end""".stripMargin
    val reader = new StreamReader(Readable.from(Buffer.from(expectedContent)))
    var result = ""
    
    //when
    val resultF = reader.readAllLines { line =>
      line should not include("\n")

      if (result != "") {
        result = result + "\n"
      }
      result = result + line
    }
    
    //then
    resultF.map { _ =>
      result shouldBe expectedContent
    }
  }
}
