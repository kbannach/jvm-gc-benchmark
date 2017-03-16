package benchmark;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkThread implements Runnable {

   @SuppressWarnings("serial")
   public static class NotFinishedYetException extends Throwable {
   }
   private final int instancesNo;
   private final int arraySize;
   private long      time = -1l;

   public BenchmarkThread(int instancesNo, int arraySize) {
      this.instancesNo = instancesNo;
      this.arraySize = arraySize;
   }

   @Override
   public void run() {
      long start = System.currentTimeMillis();
      List<Integer[]> coll = new ArrayList<>();
      for (int i = 0; i < this.instancesNo; i++) {
         coll.add(new Integer[this.arraySize]);
      }
      this.time = System.currentTimeMillis() - start;
   }

   public long getTimeInMilisec() throws NotFinishedYetException {
      if (this.time < 0l) {
         throw new NotFinishedYetException();
      }
      return this.time;
   }
}
