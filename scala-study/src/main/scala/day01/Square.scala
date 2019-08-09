package day01

object Square {
  def unapply(z: Double): Option[Double] ={
    Some(math.sqrt(z))
  }

}
