package com.assemblyrobot.utils;

public abstract class RandomUtils {

  public static boolean resultIsWithinVariance(int result, int mean, int variance) {
    return result >= mean - variance && result <= mean + variance;
  }

  public static boolean resultIsWithinVariance(long result, int mean, int variance) {
    return result >= mean - variance && result <= mean + variance;
  }

  public static boolean resultIsWithinVariance(double result, int mean, int variance) {
    return result >= mean - variance && result <= mean + variance;
  }
}
