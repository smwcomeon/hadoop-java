package day03

object ExtendTest {
  def main(args: Array[String]): Unit = {
    class Person  {
      var name = ""
    }

    class Employee extends Person{
      var salary = 0.0
      def description = "员工姓名：" + name + " 薪水：" + salary
    }

  }
}
