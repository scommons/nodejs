package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * https://nodejs.org/docs/latest-v12.x/api/fs.html#fs_fs_constants_1
  */
@js.native
@JSImport("fs", "constants")
object FSConstants extends js.Object {

  //////////////////////////////////////////////////////////////////////////////
  // File Access Constants
  //
  // The following constants are meant for use with fs.access().
  //
  val F_OK: Int = js.native //Flag indicating that the file is visible to the calling process.
  val R_OK: Int = js.native //Flag indicating that the file can be read by the calling process.
  val W_OK: Int = js.native //Flag indicating that the file can be written by the calling process.
  val X_OK: Int = js.native //Flag indicating that the file can be executed by the calling process.

  
  //////////////////////////////////////////////////////////////////////////////
  // File Open Constants
  //
  // The following constants are meant for use with fs.open().
  //
  val O_APPEND: Int = js.native //Flag indicating that data will be appended to the end of the file.
  val O_CREAT: Int = js.native //Flag indicating to create the file if it does not already exist.
  
  val O_EXCL: Int = js.native //Flag indicating that opening a file should fail
                              //  if the O_CREAT flag is set and the file already exists.
  
  val O_RDONLY: Int = js.native //Flag indicating to open a file for read-only access.
  val O_RDWR: Int = js.native //Flag indicating to open a file for read-write access.

  val O_TRUNC: Int = js.native //Flag indicating that if the file exists and is a regular file,
                               //  and the file is opened successfully for write access,
                               //  its length shall be truncated to zero.
  
  val O_WRONLY: Int = js.native //Flag indicating to open a file for write-only access.
  
  val O_NOCTTY: js.UndefOr[Int] = js.native //Flag indicating that if path identifies a terminal device,
                                //  opening the path shall not cause that terminal to become
                                //  the controlling terminal for the process
                                //  (if the process does not already have one).
  
  val O_DIRECTORY: js.UndefOr[Int] = js.native //Flag indicating that the open should fail if the path is not a directory.
  
  val O_NOATIME: js.UndefOr[Int] = js.native //Flag indicating reading accesses to the file system
                                 //  will no longer result in an update to the atime
                                 //  information associated with the file.
                                 //This flag is available on Linux operating systems only.
  
  val O_NOFOLLOW: js.UndefOr[Int] = js.native //Flag indicating that the open should fail if the path is a symbolic link.
  
  val O_SYNC: js.UndefOr[Int] = js.native //Flag indicating that the file is opened for synchronized I/O
                              //  with write operations waiting for file integrity.
  
  val O_DSYNC: js.UndefOr[Int] = js.native //Flag indicating that the file is opened for synchronized I/O
                               //  with write operations waiting for data integrity.
  
  val O_SYMLINK: js.UndefOr[Int] = js.native //Flag indicating to open the symbolic link itself
                                 //  rather than the resource it is pointing to.
  
  val O_DIRECT: js.UndefOr[Int] = js.native //When set, an attempt will be made to minimize caching effects of file I/O.
  val O_NONBLOCK: js.UndefOr[Int] = js.native //Flag indicating to open the file in nonblocking mode when possible.

  
  //////////////////////////////////////////////////////////////////////////////
  // File Type Constants
  //
  // The following constants are meant for use with the fs.Stats object's mode property
  // for determining a file's type.
  //
  val S_IFCHR: Int = js.native //File type constant for a character-oriented device file.
  val S_IFDIR: Int = js.native //File type constant for a directory.
  val S_IFLNK: Int = js.native //File type constant for a symbolic link.
  val S_IFMT: Int = js.native //Bit mask used to extract the file type code.
  val S_IFREG: Int = js.native //File type constant for a regular file.
  
  val S_IFBLK: js.UndefOr[Int] = js.native //File type constant for a block-oriented device file.
  val S_IFIFO: js.UndefOr[Int] = js.native //File type constant for a FIFO/pipe.
  val S_IFSOCK: js.UndefOr[Int] = js.native //File type constant for a socket.
  
  
  //////////////////////////////////////////////////////////////////////////////
  // File Mode Constants
  //
  // The following constants are meant for use with the fs.Stats object's mode property
  // for determining the access permissions for a file.
  //
  val S_IRWXU: js.UndefOr[Int] = js.native //File mode indicating readable, writable, and executable by owner.
  val S_IRUSR: js.UndefOr[Int] = js.native //File mode indicating readable by owner.
  val S_IWUSR: js.UndefOr[Int] = js.native //File mode indicating writable by owner.
  val S_IXUSR: js.UndefOr[Int] = js.native //File mode indicating executable by owner.
  val S_IRWXG: js.UndefOr[Int] = js.native //File mode indicating readable, writable, and executable by group.
  val S_IRGRP: js.UndefOr[Int] = js.native //File mode indicating readable by group.
  val S_IWGRP: js.UndefOr[Int] = js.native //File mode indicating writable by group.
  val S_IXGRP: js.UndefOr[Int] = js.native //File mode indicating executable by group.
  val S_IRWXO: js.UndefOr[Int] = js.native //File mode indicating readable, writable, and executable by others.
  val S_IROTH: js.UndefOr[Int] = js.native //File mode indicating readable by others.
  val S_IWOTH: js.UndefOr[Int] = js.native //File mode indicating writable by others.
  val S_IXOTH: js.UndefOr[Int] = js.native //File mode indicating executable by others.
}
