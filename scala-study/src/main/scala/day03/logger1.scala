package day03

trait Logger4 {
  def log(msg: String);
}

trait ConsoleLogger4 extends Logger4 {
  def log(msg: String) {
    println(msg)
  }
}

trait TimestampLogger4 extends ConsoleLogger4 {
  override def log(msg: String) {
    super.log(new java.util.Date() + " " + msg)
  }
}

trait ShortLogger4 extends ConsoleLogger4 {
  override def log(msg: String) {
    super.log(if (msg.length <= 15) msg else s"${msg.substring(0, 12)}...")
  }
}

class Account4 {
  protected var balance = 0.0
}

abstract class SavingsAccount4 extends Account4 with Logger4 {
  def withdraw(amount: Double) {
    if (amount > balance) log("余额不足")
    else balance -= amount
  }
}

object Main4 extends App {
  val acct1 = new SavingsAccount4 with TimestampLogger4 with ShortLogger4
  val acct2 = new SavingsAccount4 with ShortLogger4 with TimestampLogger4
  acct1.withdraw(100) // Tue Nov 28 08:38:47 CST 2017 余额不足
  acct2.withdraw(100) // Tue Nov 28 0...
}
