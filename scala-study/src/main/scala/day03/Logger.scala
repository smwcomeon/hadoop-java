package day03

trait Logger {
  def log(msg: String)
}

class ConsoleLogger extends Logger with Cloneable with Serializable {
  def log(msg: String) {
    println(msg)
  }
}

//===================================

trait ConsoleLogger2 {
  def log(msg: String) {
    println(msg)
  }
}

class Account {
  protected var balance = 0.0
}

class SavingsAccount extends Account with ConsoleLogger2 {
  def withdraw(amount: Double) {
    if (amount > balance) log("余额不足")
    else balance -= amount
  }
}

//===================带有特质的对象，动态混入========================
trait Logger2 {
  def log(msg: String)
}

trait ConsoleLogger3 extends Logger2 {
  def log(msg: String) {
    println(msg)
  }
}

class Account2 {
  protected var balance = 0.0
}

abstract class SavingsAccount2 extends Account2 with Logger2 {
  def withdraw(amount: Double) {
    if (amount > balance) log("余额不足")
    else balance -= amount
  }
}

object Main extends App {
  val account = new SavingsAccount2 with ConsoleLogger3
  account.withdraw(100)
}


