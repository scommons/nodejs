package scommons.nodejs

import scommons.nodejs.test.AsyncTestSpec

class StreamSpec extends AsyncTestSpec {

  it should "write data into file" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir) shouldBe true
    val tmpFile = path.join(tmpDir, "example.txt")

    //when
    val writer = fs.createWriteStream(tmpFile)
    writer.write("hello, ")
    writer.end("world!")

    //then
    fs.existsSync(tmpFile) shouldBe true
    
    //cleanup
    fs.unlinkSync(tmpFile)
    fs.existsSync(tmpFile) shouldBe false
    
    fs.rmdirSync(tmpDir)
    fs.existsSync(tmpDir) shouldBe false
  }
}
