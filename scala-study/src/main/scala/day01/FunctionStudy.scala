package day01

object FunctionStudy {
  def main(args: Array[String]): Unit = {
   //函数的定义
    def play(content:String ) :Unit={

    }
    val result=play("hahhahah")
    println(result+result.getClass.getTypeName)

    //变种1
    def play1 (a1:String ,a2:String):Unit={

    }

    //变种2 变量指定值
    def play2(a2:String ,a1:String ="hh"):Unit={

    }

    //变种3 可变参数
    def play3(a1:String ,a2:String* ):Unit={

    }

    //变种4 省略返回值类型
    def play4(a1:String )={

    }

    //变种5
    def play5(a1:String ):Int ={
      10
    }

    //变种6
    def play6(a1:String ) :String ={
      " "
    }

    def play66(a1:String )  ={
      " "
    }

    //变种7 匿名函数
    val f7=(a1:String ) =>println(a1.length)



  }
}
