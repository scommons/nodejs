package scommons.nodejs.stream

import scommons.nodejs._
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Promise
import scala.scalajs.js

class ReadableSpec extends AsyncTestSpec {

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
