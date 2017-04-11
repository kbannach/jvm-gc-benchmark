package main;

import benchmark.Benchmark;

public class Main {

   public static final int    MAX_ARRAY_SIZE       = 1000;
   public static final String THREADS_ARG_NAME     = "threads";
   public static final String ALLOCATIONS_ARG_NAME = "allocations";
   public static final String SIZE_ARG_NAME        = "arrSize";

   public static void main(String[] args) {
      if (args.length == 0) {
         displayUsage();
      } else {
         runBenchmark(args);
      }
   }

   private static void displayUsage() {
      String usage = "Usage:\n";
      usage += "\trun_benchmark.sh [arguments]\n";
      usage += "argument list:\n";
      usage += "\t" + THREADS_ARG_NAME + " - how many threads to use, default is 1\n";
      usage += "\t" + ALLOCATIONS_ARG_NAME + " - how many objects to allocate\n";
      usage += "\t" + SIZE_ARG_NAME + " - size of allocated arrays (if negative then this number is generated randomly), default is -1, maximum is " + MAX_ARRAY_SIZE + "\n";
      usage += "\n";
      usage += "Examples:\n";
      usage += "\t- performs 1000000 Integer array allocations with size of 1024 using 4 threads:\n";
      usage += "\t\trun_benchmark.sh -" + THREADS_ARG_NAME + "=4 -" + ALLOCATIONS_ARG_NAME + "=1000000 -" + SIZE_ARG_NAME + "=1024\n\n";
      usage += "\t- performs 500000 Integer array allocations with random size using 1 thread:\n";
      usage += "\t\trun_benchmark.sh -" + ALLOCATIONS_ARG_NAME + "=500000 -" + SIZE_ARG_NAME + "=-1\n";
      System.out.println(usage);
   }

   private static void runBenchmark(String[] args) {
      int threads = Math.min(8, getArgValue(args, THREADS_ARG_NAME, 1));
      checkPositive(threads);
      int allocations = getArgValue(args, ALLOCATIONS_ARG_NAME, null);
      checkPositive(allocations);
      int arrSize = getArgValue(args, SIZE_ARG_NAME, -1);
      // allocate and measure
      long start = System.currentTimeMillis();
      new Benchmark(threads, allocations, arrSize).run();
      long totalTime = System.currentTimeMillis() - start;
      System.out.print(totalTime);
   }

   private static int getArgValue(String[] args, String name, Integer defaultValue) throws ArgumentParsingException {
      String arg = findArg(args, name);
      if (arg == null) {
         return checkDefault(defaultValue, name);
      } else {
         if (arg.split("=").length < 2) {
            // example: "-threads="
            return checkDefault(defaultValue, name);
         }
         arg = arg.split("=")[1];
         try {
            return Integer.valueOf(arg);
         } catch (NumberFormatException e) {
            throw new ArgumentParsingException("Passed invalid value with \"" + name + "\" parameter (" + arg + ").");
         }
      }
   }

   private static int checkDefault(Integer defaultValue, String name) throws ArgumentParsingException {
      if (defaultValue != null) {
         return defaultValue;
      } else {
         throw new ArgumentParsingException("Parameter " + name + " missing.");
      }
   }

   private static String findArg(String[] args, String toFind) {
      for (String arg : args) {
         if (arg.contains(toFind)) {
            return arg;
         }
      }
      return null;
   }

   private static void checkPositive(int val) throws ArgumentParsingException {
      if (val <= 0) {
         throw new ArgumentParsingException("Arguments " + THREADS_ARG_NAME + " and " + ALLOCATIONS_ARG_NAME + " values have to be positive numbers.");
      }
   }

}
