package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import benchmark.BenchmarkThread.NotFinishedYetException;

public class Benchmark {

   // 100 KB = 800 000 bits
   // 800 000 bits / 32 bits (int) = 25 000 int elements in 1MB array
   public static final int MAX_ARRAY_SIZE = 25000;
   private final int       threadsNo;
   private final int       allocationsNo;
   private final int       arraySize;

   public Benchmark(int threadsNo, int allocationsNo, int arraySize) {
      this.threadsNo = threadsNo;
      this.allocationsNo = allocationsNo;
      this.arraySize = arraySize > 0 ? arraySize : new Random().nextInt(MAX_ARRAY_SIZE);
   }

   /**
    * @return sum of times of executions
    */
   public long run() {
      int instPerThread = this.allocationsNo / this.threadsNo;
      BenchmarkThread[] tab = new BenchmarkThread[this.threadsNo];
      for (int i = 0; i < this.threadsNo; i++) {
         tab[i] = new BenchmarkThread(instPerThread, this.arraySize);
      }
      for (BenchmarkThread bt : tab) {
         bt.run();
      }
      // collect results
      List<Long> results = new ArrayList<>(tab.length);
      for (BenchmarkThread bt : tab) {
         try {
            results.add(bt.getTimeInMilisec());
         } catch (NotFinishedYetException e) {
            try {
               wait(10000); //10 sec
            } catch (InterruptedException e1) {
               // empty
            }
         }
      }
      long res = 0l;
      for (Long l : results) {
         res += l;
      }
      return res;
   }
}
