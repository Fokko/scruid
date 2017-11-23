package ing.wbaa.druid.utils

object StringUtils {
  def decapitalize(input: String) = {
    val chars = input.toCharArray
    chars(0) = Character.toLowerCase(chars(0))
    chars.mkString
  }
}
