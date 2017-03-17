#!/bin/sh
# create temporary files
cp -r ../java/* .
# build
javac main.Main

# create results file
touch results.txt
printf "heap size: 128 MB\n" >> results.txt
printf "GC algorithm\t1 thd, fixed size\t1 thd, random size\tn thds, fixed obj size\tn thds, random obj size\t" >> results.txt

# run benchmark
printf "ParrallelOld\t"
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=1
printf "CMS\t"
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=1
printf "G1\t"
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=1


# garbage collection
rm -r benchmark/ main/
