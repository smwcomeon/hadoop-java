package day03

//富特质
trait Logger6 {
  def log(msg: String)

  def info(msg: String) {
    log("INFO: " + msg)
  }

  def warn(msg: String) {
    log("WARN: " + msg)
  }

  def severe(msg: String) {
    log("SEVERE: " + msg)
  }
}

trait ConsoleLogger6 extends Logger6 {
  def log(msg: String) {
    println(msg)
  }
}

class Account6 {
  protected var balance = 0.0
}

abstract class SavingsAccount6 extends Account6 with Logger6 {
  def withdraw(amount: Double) {
    if (amount > balance) severe("余额不足")
    else balance -= amount
  }
}

object Main6 extends App {
  val acct = new SavingsAccount6 with ConsoleLogger6
  acct.withdraw(100)
}

