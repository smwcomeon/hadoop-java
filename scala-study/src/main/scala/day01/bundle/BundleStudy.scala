package day01.bundle

object BundleStudy {
  def main(args: Array[String]): Unit = {

    //嵌套执行
    val sale = Bundle("愚人节大甩卖系列", 10,
                    Article("《九阴真经》", 40),
                    Bundle("从出门一条狗到装备全发光的修炼之路系列", 20,
                      Article("《如何快速捡起地上的装备》", 80),
                      Article("《名字起得太长躲在树后容易被地方发现》",30)))

    val result1 = sale match {
      case Bundle(_, _, Article(descr, _), _*) => descr
    }
    println(result1)


    val result2 = sale match {
      case Bundle(_, _, art @ Article(_, _), rest @ _*) => ("art="+art, rest)
    }
    println(result2)


    val result3 = sale match {
      case Bundle(_, _, art @ Article(_, _), rest) => (art, rest)
    }
    println(result3)

    //计算价格
    def price(it: Item): Double = {
      it match {
        case Article(_, p) => p
        case Bundle(_, disc, its@_*) => its.map(price _ ).sum - disc
      }
    }

    println(price(sale))

    def price1(it:Item):Double={
      it match {
        case Article(_,price)=>price
        case Bundle(_,discount,its@_*)=>its.map(price1).sum-discount
      }
    }

    //偏函数 只有符合函数case相对应的情况才能调用偏函数
    //判断参数符不符合偏函数的case用isDefiendAt 如果符合返回true 不符合返回false
    //偏函数如果不用isdefinedAt判断 当出现不合符的情况会出错
    val f: PartialFunction[Char, Int] = {
      case '+' => 1
      case '-' => -1
    }
    println(f('-'))
    println(f.isDefinedAt('0'))
    println(f('+'))
    //    println(f('0'))

    val f1 = new PartialFunction[Any, Int] {
      def apply(any: Any) = any.asInstanceOf[Int] + 1

      def isDefinedAt(any: Any) = if (any.isInstanceOf[Int]) true else false
    }
    val rf1 = List(1, 3, 5, "seven") collect f1
    println(rf1)



  }

}
