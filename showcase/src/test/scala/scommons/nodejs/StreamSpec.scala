package scommons.nodejs

import scommons.nodejs.raw.{CreateReadStreamOptions, FileOptions}
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.{Future, Promise}
import scala.scalajs.js

class StreamSpec extends AsyncTestSpec {

  it should "write data into file" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir) shouldBe true
    val tmpFile = path.join(tmpDir, "example.txt")
    val p = Promise[Unit]()

    //when
    val writer = fs.createWriteStream(tmpFile)
    writer.write("hello, ", "utf8", { error =>
      error shouldBe js.undefined
      
      writer.end("world!", "utf8", { error =>
        error shouldBe js.undefined
        
        p.success(())
      }: js.Function1[js.Error, Unit])
    }: js.Function1[js.Error, Unit]) shouldBe true
    val resultF = p.future

    //then
    resultF.map { _ =>
      fs.existsSync(tmpFile) shouldBe true
      fs.readFileSync(tmpFile, new FileOptions {
        override val encoding = "utf8"
      }) shouldBe "hello, world!"

      //cleanup
      fs.unlinkSync(tmpFile)
      fs.existsSync(tmpFile) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "fail with error when read data" in {
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
      
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "end stream without error when read data" in {
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
      
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }

  it should "read data from file" in {
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
      
      //cleanup
      fs.unlinkSync(file)
      fs.existsSync(file) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }
}
