package com.assemblyrobot.utils;

import lombok.val;

public abstract class RandomUtils {
  public static boolean resultIsWithinVariance(int result, int mean, int variance) {
    val isAboveMin = result >= mean - variance;
    val isBelowMax = result <= mean + variance;

    System.out.printf(
        "Value %d %s above minimum and %s below maximum.%n",
        result, isAboveMin ? "IS" : "IS NOT", isBelowMax ? "IS" : "IS NOT");

    return result >= mean - variance && result <= mean + variance;
  }

  public static boolean resultIsWithinVariance(long result, int mean, int variance) {
    val isAboveMin = result >= mean - variance;
    val isBelowMax = result <= mean + variance;

    System.out.printf(
        "Value %d %s above minimum and %s below maximum.%n",
        result, isAboveMin ? "IS" : "IS NOT", isBelowMax ? "IS" : "IS NOT");

    return result >= mean - variance && result <= mean + variance;
  }

  public static boolean resultIsWithinVariance(double result, int mean, int variance) {
    val isAboveMin = result >= mean - variance;
    val isBelowMax = result <= mean + variance;

    System.out.printf(
        "Value %f %s above minimum and %s below maximum.%n",
        result, isAboveMin ? "IS" : "IS NOT", isBelowMax ? "IS" : "IS NOT");

    return result >= mean - variance && result <= mean + variance;
  }
}
