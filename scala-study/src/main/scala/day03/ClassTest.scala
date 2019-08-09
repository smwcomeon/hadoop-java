package day03

object ClassTest {
  def main(args: Array[String]): Unit = {
    val cat1=Cat()
    val cat2=Cat()

    cat1.changeName("hei猫")
    cat2.changeName("白猫")
    println(cat1)
    println(cat2)



    //Creater
    val ant=new Ant
    println(ant.range)
    println(ant.env.length)

    val ant2=new Ant2
    println(ant2.range)
    println(ant2.env.length)
  }
}
