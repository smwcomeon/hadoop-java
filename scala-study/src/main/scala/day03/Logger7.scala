package day03
//  特质构造顺序
trait Logger7 {
  println("我在Logger7特质构造器中，嘿嘿嘿。。。")
  def log(msg: String)
}

trait ConsoleLogger7 extends Logger7 {
  println("我在ConsoleLogger7特质构造器中，嘿嘿嘿。。。")
  def log(msg: String) {
    println(msg)
  }
}

trait ShortLogger7 extends Logger7 {
  val maxLength: Int
  println("我在ShortLogger7特质构造器中，嘿嘿嘿。。。")

  abstract override def log(msg: String) {
    super.log(if (msg.length <= maxLength) msg else s"${msg.substring(0, maxLength - 3)}...")
  }
}

class Account7 {
  println("我在Account7构造器中，嘿嘿嘿。。。")
  protected var balance = 0.0
}

abstract class SavingsAccount7 extends Account7 with ConsoleLogger7 with ShortLogger7{
  println("我再SavingsAccount7构造器中")
  var interest = 0.0
  override val maxLength: Int = 20
  def withdraw(amount: Double) {
    if (amount > balance) log("余额不足")
    else balance -= amount
  }
}

object Main7 extends App {
  val acct = new SavingsAccount7 with ConsoleLogger7 with ShortLogger7
  acct.withdraw(100)
  println(acct.maxLength)
}
