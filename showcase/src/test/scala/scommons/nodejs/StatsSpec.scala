package scommons.nodejs

import scommons.nodejs.raw.FSConstants._
import scommons.nodejs.test.TestSpec

class StatsSpec extends TestSpec {

  it should "return file mode" in {
    //given
    val dir = os.homedir()
    val stats = fs.lstatSync(dir)
    
    //when
    val mode = stats.mode
    
    //then
    (mode & S_IFDIR) should not be 0
    (mode & S_IRUSR) should not be 0
  }
}
