package scommons.nodejs

import scommons.nodejs.test.TestSpec

class ProcessSpec extends TestSpec {

  it should "return current working directory" in {
    //when & then
    process.cwd() should not be empty
  }
  
  it should "change current working directory" in {
    //given
    val currDir = process.cwd()
    currDir should not be os.homedir()
    
    //when
    process.chdir(os.homedir())
    
    //then
    process.cwd() shouldBe os.homedir()
    
    //cleanup
    process.chdir(currDir)
  }
}
