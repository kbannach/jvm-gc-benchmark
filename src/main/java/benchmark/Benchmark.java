package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    * @return times of execution of each thread
    */
   public List<Long> run() {
      int instPerThread = this.allocationsNo / this.threadsNo;
      BenchmarkThread[] tab = new BenchmarkThread[this.threadsNo];
      for (int i = 0; i < this.threadsNo; i++) {
         tab[i] = new BenchmarkThread(instPerThread, this.arraySize);
      }
      // execute threads
      ExecutorService service = Executors.newFixedThreadPool(this.threadsNo);
      for (BenchmarkThread bt : tab) {
         service.execute(bt);
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
      service.shutdown();
      return results;
   }
}
