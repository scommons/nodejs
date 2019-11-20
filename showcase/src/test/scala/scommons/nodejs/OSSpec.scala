package scommons.nodejs

import scommons.nodejs.test.TestSpec

class OSSpec extends TestSpec {

  it should "return home dir when homedir" in {
    //when & then
    os.homedir() should not be empty
  }
  
  it should "return tmp dir when tmpdir" in {
    //when & then
    os.tmpdir() should not be empty
  }
}
