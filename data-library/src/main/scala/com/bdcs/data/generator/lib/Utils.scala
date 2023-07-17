package com.bdcs.data.generator.lib

object Utils {

  def roundValue(input: Double): Double = {
    BigDecimal(input)
      .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
  }


}
