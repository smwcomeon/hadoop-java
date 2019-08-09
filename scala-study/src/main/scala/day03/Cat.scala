package day03

class Cat {
    private var name:String=_
    def changeName(name:String):Unit={
      this.name=name
    }
  val hair = Cat.growHair

  override def toString: String = {
    "name：-》"+name+"hair"+hair
  }
}

object Cat{
  private var hari:Int = 0

  def apply(): Cat = new Cat()

  private def growHair={
    hari +=1
    hari
  }
}
