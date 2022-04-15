package scommons.nodejs.stream

import scommons.nodejs._
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.{Future, Promise}
import scala.scalajs.js

class ReadableSpec extends AsyncTestSpec {

  it should "read data from file" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    
    //when
    val resultF = readAll(fs.createReadStream(file))
    
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

  it should "create from String" in {
    //when
    val resultF = readAll(Readable.from(js.Array("hello", ", World!!!"), new ReadableOptions {
      override val highWaterMark = 2
    }))
    
    //then
    resultF.map { result =>
      result shouldBe "hello, World!!!"
    }
  }

  it should "create from Buffer" in {
    //when
    val resultF = readAll(Readable.from(Buffer.from("hello, World!!!")))
    
    //then
    resultF.map { result =>
      result shouldBe "hello, World!!!"
    }
  }
  
  private def readAll(readable: Readable): Future[String] = {
    val p = Promise[String]()
    var result = ""
    val readableListener: js.Function = { () =>
      @annotation.tailrec
      def loop(result: String): String = {
        val content = readable.read(5)
        if (content == null) result
        else loop(result + content.toString)
      }

      result = loop(result)
    }
    val errorListener: js.Function = { error: js.Error =>
      p.tryFailure(js.JavaScriptException(error))
    }
    val endListener: js.Function = { () =>
      readable.removeListener("readable", readableListener)
      readable.removeListener("error", errorListener)
      p.trySuccess(result)
    }
    readable.on("readable", readableListener)
    readable.on("error", errorListener)
    readable.once("end", endListener)
    readable.once("close", endListener)
    p.future
  }
}
