package FPUv2

object TestArgs {
  val expWidth = 5
  val precision = 11
  val len: Int = expWidth + precision
  def toUInt[T <: AnyVal](f: T): String = {
    if(f.getClass == classOf[java.lang.Float]) {
      "h" + java.lang.Float.floatToIntBits(f.asInstanceOf[Float]).toHexString
    }
    else if(f.getClass == classOf[java.lang.Integer]) {
      "h" + java.lang.Integer.toHexString(f.asInstanceOf[Int])
    } else {
      "h0"
    }
  }
}
