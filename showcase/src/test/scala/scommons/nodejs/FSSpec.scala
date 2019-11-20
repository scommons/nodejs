package scommons.nodejs

import scommons.nodejs.raw.FSConstants._
import scommons.nodejs.test.AsyncTestSpec

import scala.scalajs.js.JavaScriptException

class FSSpec extends AsyncTestSpec {

  it should "fail if no such dir when readdir" in {
    //given
    val dir = s"${os.homedir()}-unknown"
    
    //when
    val result = fs.readdir(dir)
    
    //then
    result.failed.map {
      case JavaScriptException(error) =>
        error.toString should include ("no such file or directory")
    }
  }
  
  it should "return list of files when readdir" in {
    //when & then
    fs.readdir(os.homedir()).map { files =>
      files should not be empty
    }
  }
  
  it should "return stats when lstatSync" in {
    //when
    val stats = fs.lstatSync(os.homedir())
    
    //then
    stats.isDirectory shouldBe true
    stats.isFile() shouldBe false
    stats.isSymbolicLink() shouldBe false

    val mode = stats.mode
    (mode & S_IFDIR) should not be 0
    (mode & S_IRUSR) should not be 0
    
    stats.size should be > 0.0
    
    stats.atimeMs should be > 0.0
    stats.mtimeMs should be > 0.0
    stats.ctimeMs should be > 0.0
    stats.birthtimeMs should be > 0.0
  }

  it should "check if path exists when existsSync" in {
    //when & then
    fs.existsSync(s"${os.homedir()}-unknown") shouldBe false
    fs.existsSync(os.homedir()) shouldBe true
  }
  
  it should "fail if no such file when unlinkSync" in {
    //given
    val path = s"${os.homedir()}-unknown"

    //when
    val e = the[JavaScriptException] thrownBy {
      fs.unlinkSync(path)
    }

    //then
    e.toString should include ("no such file or directory")
  }
  
  it should "fail if no such dir when rmdirSync" in {
    //given
    val path = s"${os.homedir()}-unknown"

    //when
    val e = the[JavaScriptException] thrownBy {
      fs.rmdirSync(path)
    }

    //then
    e.toString should include ("no such file or directory")
  }

  it should "remove file and dir when unlinkSync/rmdirSync" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir) shouldBe true
    
    val tmpFile = path.join(tmpDir, "example.txt")
    fs.writeFileSync(tmpFile, "hello, World!!!")
    fs.existsSync(tmpFile) shouldBe true

    //when
    fs.unlinkSync(tmpFile)
    fs.rmdirSync(tmpDir)

    //then
    fs.existsSync(tmpFile) shouldBe false
    fs.existsSync(tmpDir) shouldBe false
  }
}
