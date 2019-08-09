package day01

object Names {
  def unapplySeq(names: String ): Option[Array[String ]] = {
    if(names.contains(","))Some(names.split(",")) else None
  }
}
