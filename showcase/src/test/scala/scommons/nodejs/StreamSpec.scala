package scommons.nodejs

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
    }: js.Function1[js.Error, Unit])
    val resultF = p.future

    //then
    resultF.map { _ =>
      fs.existsSync(tmpFile) shouldBe true

      //cleanup
      fs.unlinkSync(tmpFile)
      fs.existsSync(tmpFile) shouldBe false

      fs.rmdirSync(tmpDir)
      fs.existsSync(tmpDir) shouldBe false
    }
  }
}
