package benchmark;

import java.util.Random;

public class BenchmarkThread implements Runnable {

   @SuppressWarnings("serial")
   public static class NotFinishedYetException extends Throwable {

      public NotFinishedYetException() {
         super();
      }

   }

   public static final int MAX_ARRAY_SIZE = 1000;
   private final int       instancesNo;
   private final int       arraySize;
   private long            time           = -1l;
   private final Random    rand           = new Random();

   public BenchmarkThread(int instancesNo, int arraySize) {
      this.instancesNo = instancesNo;
      this.arraySize = arraySize;
   }

   @Override
   public void run() {
      long start = System.currentTimeMillis();
      Integer[] tmp;
      for (int i = 0; i < this.instancesNo; i++) {
         tmp = new Integer[getArraySize()];
      }
      this.time = System.currentTimeMillis() - start;
   }

   private int getArraySize() {
      return this.arraySize > 0 ? this.arraySize : this.rand.nextInt(MAX_ARRAY_SIZE);
   }

   public long getTimeInMilisec() throws NotFinishedYetException {
      if (this.time < 0l) {
         throw new NotFinishedYetException();
      }
      return this.time;
   }
}
