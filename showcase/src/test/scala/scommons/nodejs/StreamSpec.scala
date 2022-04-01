package scommons.nodejs

import scommons.nodejs.raw.FileOptions
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Promise
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

  it should "read data from file" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    val p = Promise[String]()
    
    //when
    var result = ""
    val readable = fs.createReadStream(file)
    val readableListener: js.Function = { () =>
      @annotation.tailrec
      def loop(result: String): String = {
        val content = readable.read(5)
        if (content == null) result
        else loop(result + content.toString)
      }

      result = loop(result)
    }
    readable.on("readable", readableListener)
    readable.once("end", { () =>
      readable.removeListener("readable", readableListener)
      p.success(result)
    })
    val resultF = p.future
    
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
