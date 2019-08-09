package day03

import java.io.File

import scala.io.{Source, StdIn}


class RichFile(val from: File) {
  def read = Source.fromFile(from.getPath).mkString
}

object RichFile extends App {
  implicit def file2RichFile(from: File) = new RichFile(from)

  val contents = new File("D:\\scala笔记.txt").read
  println(contents)

  def foo(msg:String)=println(msg+msg.getClass.getTypeName)
  implicit def  int2String(x:Int)=x.toString
  foo(10)


  import java.io.{File, PrintWriter}
  val writer = new PrintWriter(new File("嘿嘿嘿.txt"))
  for (i <- 1 to 100)
    writer.println(i)
  writer.close()


  //控制台交互--老API
  print("请输入内容:")
  val consoleLine1 = Console.readLine()
  println("刚才输入的内容是:" + consoleLine1)

  //控制台交互--新API
  print("请输入内容(新API):")
  val consoleLine2 = StdIn.readLine()
  println("刚才输入的内容是:" + consoleLine2)


}


