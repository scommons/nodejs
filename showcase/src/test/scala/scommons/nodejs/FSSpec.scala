package scommons.nodejs

import org.scalatest.Succeeded
import scommons.nodejs.raw.FSConstants._
import scommons.nodejs.raw.{FSConstants, FileOptions}
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JavaScriptException
import scala.scalajs.js.typedarray.Uint8Array
import scala.Ordering.Double.IeeeOrdering

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
  
  it should "fail if newPath is dir when rename" in {
    //given
    val tmpDir1 = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val tmpDir2 = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir1) shouldBe true

    val file = path.join(tmpDir1, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    fs.existsSync(file) shouldBe true

    val oldPath = file
    val newPath = tmpDir2

    //when
    val result = fs.rename(oldPath, newPath)

    //then
    result.failed.map {
      case JavaScriptException(error) =>
        error.toString should include ("EISDIR: illegal operation on a directory")
    }.andThen {
      case _ =>
        //cleanup
        fs.unlinkSync(file)
        fs.rmdirSync(tmpDir1)
        fs.rmdirSync(tmpDir2)
    }
  }

  it should "move file when rename" in {
    //given
    val tmpDir1 = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val tmpDir2 = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir1) shouldBe true
    fs.existsSync(tmpDir2) shouldBe true

    val file = path.join(tmpDir1, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    fs.existsSync(file) shouldBe true

    val oldPath = file
    val newPath = path.join(tmpDir2, "example.txt")
    fs.existsSync(oldPath) shouldBe true
    fs.existsSync(newPath) shouldBe false

    //when
    val result = fs.rename(oldPath, newPath)

    //then
    result.map { _ =>
      fs.existsSync(oldPath) shouldBe false
      fs.existsSync(newPath) shouldBe true
    }.andThen {
      case _ =>
        //cleanup
        fs.unlinkSync(newPath)
        fs.rmdirSync(tmpDir2)
        fs.rmdirSync(tmpDir1)
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
    
    val dir = path.join(tmpDir, "test")
    fs.mkdirSync(dir)
    fs.existsSync(dir) shouldBe true
    
    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    fs.existsSync(file) shouldBe true

    //when
    fs.rmdirSync(dir)
    fs.unlinkSync(file)
    fs.rmdirSync(tmpDir)

    //then
    fs.existsSync(dir) shouldBe false
    fs.existsSync(file) shouldBe false
    fs.existsSync(tmpDir) shouldBe false
  }
  
  it should "fail if file not exists when openSync(read)" in {
    //given
    val path = s"${os.homedir()}-unknown"

    //when
    val ex = the[JavaScriptException] thrownBy {
      fs.openSync(path, FSConstants.O_RDONLY)
    }

    //then
    inside(ex) { case JavaScriptException(error: raw.Error) =>
      error.code shouldBe "ENOENT"
      error.toString should include ("no such file or directory")
    }
  }
  
  it should "fail if file already exists when openSync(write)" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir) shouldBe true

    val file = path.join(tmpDir, "example.txt")
    fs.writeFileSync(file, "hello, World!!!")
    fs.existsSync(file) shouldBe true

    //when
    val ex = the[JavaScriptException] thrownBy {
      fs.openSync(file, FSConstants.O_CREAT | FSConstants.O_EXCL)
    }

    //then
    inside(ex) { case JavaScriptException(error: raw.Error) =>
      error.code shouldBe "EEXIST"
      error.toString should include ("already exists")
    }
    
    //cleanup
    fs.unlinkSync(file)
    fs.rmdirSync(tmpDir)
    Succeeded
  }
  
  it should "copy file when read/write" in {
    //given
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    fs.existsSync(tmpDir) shouldBe true
    
    val file1 = path.join(tmpDir, "example.txt")
    val file2 = path.join(tmpDir, "example2.txt")
    fs.writeFileSync(file1, "hello, World!!!")
    fs.existsSync(file1) shouldBe true
    
    val stats1 = fs.lstatSync(file1)
    val fd1 = fs.openSync(file1, FSConstants.O_RDONLY)
    val fd2 = fs.openSync(file2, FSConstants.O_CREAT | FSConstants.O_WRONLY | FSConstants.O_EXCL)
    val buff = new Uint8Array(5)

    //when
    def loop(): Future[Unit] = {
      fs.read(fd1, buff, 0, buff.length).flatMap { bytesRead =>
        if (bytesRead == 0) {
          fs.futimesSync(fd2, stats1.atimeMs / 1000, stats1.mtimeMs / 1000)
          Future.unit
        }
        else {
          fs.write(fd2, buff, 0, bytesRead).flatMap { bytesWrite =>
            bytesWrite shouldBe bytesRead
            loop()
          }
        }
      }
    }
    val resultF = loop()

    resultF.map { _ =>
      //then
      fs.closeSync(fd1)
      fs.closeSync(fd2)
      
      val stats2 = fs.lstatSync(file2)
      stats2.size shouldBe stats1.size
      toDateTimeStr(stats2.atimeMs) shouldBe toDateTimeStr(stats1.atimeMs)
      toDateTimeStr(stats2.mtimeMs) shouldBe toDateTimeStr(stats1.mtimeMs)
      
      fs.readFileSync(file2, new FileOptions {
        override val encoding = "utf8"
      }) shouldBe "hello, World!!!"
    
      //cleanup
      fs.unlinkSync(file1)
      fs.unlinkSync(file2)
      fs.rmdirSync(tmpDir)
      Succeeded
    }
  }

  private def toDateTimeStr(dtimeMs: Double): String = {
    val date = new js.Date(dtimeMs)
    s"${date.toLocaleDateString()} ${date.toLocaleTimeString()}"
  }
}
