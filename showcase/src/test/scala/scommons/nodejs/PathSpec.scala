package scommons.nodejs

import scommons.nodejs.test.TestSpec

class PathSpec extends TestSpec {

  it should "return POSIX basename" in {
    //when & then
    path.posix.basename("/foo/bar/baz/asdf/quux.html") shouldBe "quux.html"
    path.posix.basename("/foo/bar/baz/asdf/quux.html", "html") shouldBe "quux."
    path.posix.basename("/foo/bar/baz/asdf/quux.html", ".html") shouldBe "quux"
    path.posix.basename("/foo/bar/baz/asdf/quux.html", ".htm") shouldBe "quux.html"
    path.posix.basename("/foo/bar") shouldBe "bar"
    path.posix.basename("/foo/bar/") shouldBe "bar"
    path.posix.basename("/baz/..") shouldBe ".."
    path.posix.basename("qux/") shouldBe "qux"
    path.posix.basename(".") shouldBe "."
  }
  
  it should "return Windows basename" in {
    //when & then
    path.win32.basename("C:\\foo\\bar\\baz\\asdf\\quux.html") shouldBe "quux.html"
    path.win32.basename("C:\\foo\\bar\\baz\\asdf\\quux.html", "html") shouldBe "quux."
    path.win32.basename("C:\\foo\\bar\\baz\\asdf\\quux.html", ".html") shouldBe "quux"
    path.win32.basename("C:\\foo\\bar\\baz\\asdf\\quux.html", ".htm") shouldBe "quux.html"
    path.win32.basename("C:\\foo\\bar") shouldBe "bar"
    path.win32.basename("C:\\foo\\bar\\") shouldBe "bar"
    path.win32.basename("C:\\baz\\..") shouldBe ".."
    path.win32.basename("C:\\qux\\") shouldBe "qux"
    path.win32.basename(".") shouldBe "."
  }

  it should "return POSIX dirname" in {
    //when & then
    path.posix.dirname("/foo/bar/baz/asdf/quux.html") shouldBe "/foo/bar/baz/asdf"
    path.posix.dirname("/foo/bar") shouldBe "/foo"
    path.posix.dirname("/foo/bar/") shouldBe "/foo"
    path.posix.dirname("/baz/..") shouldBe "/baz"
    path.posix.dirname("qux/") shouldBe "."
    path.posix.dirname(".") shouldBe "."
  }
  
  it should "return Windows dirname" in {
    //when & then
    path.win32.dirname("C:\\foo\\bar\\baz\\asdf\\quux.html") shouldBe "C:\\foo\\bar\\baz\\asdf"
    path.win32.dirname("C:\\foo\\bar") shouldBe "C:\\foo"
    path.win32.dirname("C:\\foo\\bar\\") shouldBe "C:\\foo"
    path.win32.dirname("C:\\baz\\..") shouldBe "C:\\baz"
    path.win32.dirname("C:\\qux\\") shouldBe "C:\\"
    path.win32.dirname(".") shouldBe "."
  }

  it should "return extname" in {
    //when & then
    path.posix.extname("/foo/1.2/index.html") shouldBe ".html"
    path.extname("index.html") shouldBe ".html"
    path.extname("index.coffee.md") shouldBe ".md"
    path.extname("index.") shouldBe "."
    path.extname("index") shouldBe ""
    path.extname(".index") shouldBe ""
  }
  
  it should "return path string from an object" in {
    //when & then
    path.format(new PathObject {}) shouldBe ""
    
    path.posix.format(new PathObject {
      override val root = "/ignored"
      override val dir = "/home/user/dir"
      override val base = "file.txt"
    }) shouldBe "/home/user/dir/file.txt"
    
    path.posix.format(new PathObject {
      override val root = "/"
      override val base = "file.txt"
      override val ext = "ignored"
    }) shouldBe "/file.txt"
    
    path.posix.format(new PathObject {
      override val root = "/"
      override val name = "file"
      override val ext = ".txt"
    }) shouldBe "/file.txt"
  }
  
  it should "check POSIX isAbsolute" in {
    //when & then
    path.posix.isAbsolute("/foo/bar") shouldBe true
    path.posix.isAbsolute("/baz/..") shouldBe true
    path.posix.isAbsolute("qux/") shouldBe false
    path.posix.isAbsolute(".") shouldBe false
  }
  
  it should "check Windows isAbsolute" in {
    //when & then
    path.win32.isAbsolute("//server") shouldBe true
    path.win32.isAbsolute("\\\\server") shouldBe true
    path.win32.isAbsolute("C:/foo/..") shouldBe true
    path.win32.isAbsolute("C:\\foo\\..") shouldBe true
    path.win32.isAbsolute("bar\\baz") shouldBe false
    path.win32.isAbsolute("bar/baz") shouldBe false
    path.win32.isAbsolute(".") shouldBe false
  }
  
  it should "join paths" in {
    //when & then
    path.posix.join("test") shouldBe "test"
    path.posix.join("test/") shouldBe "test/"
    path.posix.join("/", "test", "/") shouldBe "/test/"
    path.posix.join("a", "b") shouldBe "a/b"
  }
  
  it should "normalize path" in {
    //when & then
    path.posix.normalize("/foo/bar//baz/asdf/quux/..") shouldBe "/foo/bar/baz/asdf"
    path.win32.normalize("C:\\temp\\\\foo\\bar\\..\\") shouldBe "C:\\temp\\foo\\"
    path.win32.normalize("C:////temp\\\\/\\/\\/foo/bar") shouldBe "C:\\temp\\foo\\bar"
  }
  
  it should "return an object from path string" in {
    //when & then
    assertObject(path.posix.parse("/home/user/dir/file.txt"), new PathObject {
      override val root = "/"
      override val dir = "/home/user/dir"
      override val base = "file.txt"
      override val name = "file"
      override val ext = ".txt"
    })
    assertObject(path.posix.parse("/home/user/dir/"), new PathObject {
      override val root = "/"
      override val dir = "/home/user"
      override val base = "dir"
      override val name = "dir"
      override val ext = ""
    })
    assertObject(path.posix.parse("/"), new PathObject {
      override val root = "/"
      override val dir = "/"
      override val base = ""
      override val name = ""
      override val ext = ""
    })
    
    //when & then
    assertObject(path.win32.parse("C:\\home\\user\\dir\\file.txt"), new PathObject {
      override val root = "C:\\"
      override val dir = "C:\\home\\user\\dir"
      override val base = "file.txt"
      override val name = "file"
      override val ext = ".txt"
    })
    assertObject(path.win32.parse("C:\\"), new PathObject {
      override val root = "C:\\"
      override val dir = "C:\\"
      override val base = ""
      override val name = ""
      override val ext = ""
    })
  }

  it should "return relative path from from to to" in {
    //when & then
    path.posix.relative("", "") shouldBe ""
    path.posix.relative("/data/orandea/test/aaa", "/data/orandea/impl/bbb") shouldBe "../../impl/bbb"
    path.win32.relative("C:\\orandea\\test\\aaa", "C:\\orandea\\impl\\bbb") shouldBe "..\\..\\impl\\bbb"
  }

  it should "resolve a sequence of paths or path segments into an absolute path" in {
    //when & then
    path.posix.resolve("/foo/bar", "./baz") shouldBe "/foo/bar/baz"
    path.posix.resolve("/foo/bar", "/tmp/file/") shouldBe "/tmp/file"
    
    //when & then
    val currDir = path.posix.resolve("", "")
    currDir shouldBe process.cwd()
    path.posix.resolve("wwwroot", "static_files/png/", "../gif/image.gif") shouldBe {
      s"$currDir/wwwroot/static_files/gif/image.gif"
    }
  }

  it should "return path delimiter" in {
    //when & then
    path.posix.delimiter shouldBe ":"
    path.win32.delimiter shouldBe ";"
  }

  it should "return path separator" in {
    //when & then
    path.posix.sep shouldBe "/"
    path.win32.sep shouldBe "\\"
  }
}
