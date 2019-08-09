package day03

import java.io.PrintStream

// 初始化特质中的字段
trait  Logger8{
  def log(msg:String)
}

trait FileLogger8 extends Logger8{
  val fileName:String
  val out = new PrintStream(fileName)

  override def log(msg: String): Unit = {
    out.print(msg)
    out.flush()
  }
}

class SavingsAccount8{

}

object Main8 extends App {
  //初始化特质之前有未定义的变量 会报空指针异常 解决办法
//  val acct = new SavingsAccount8 with FileLogger8 {
//    override val fileName = "2017-11-24.log"//空指针异常
//  }

  //提前定义
  val acct = new {
    override val fileName = "2017-11-24.log"
  } with SavingsAccount8 with FileLogger8
  acct.log("heiheihei")

}
