#!/bin/sh

# TODO
# 1. java args from script execution erguments
# 2. loop

# create temporary files
cp -r ../java/* .
# build
javac main.Main

# create results file
touch results.txt
printf "heap size: 128 MB\n" >> results.txt
printf "GC algorithm\t1 thd, fixed size\t1 thd, random size\tn thds, fixed size\tn thds, random size\t" >> results.txt

# run benchmark
printf "ParrallelOld\t"
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=300
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=-1
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=300 -threads=4
java -Xms128m -Xmx128m -XX:+UseParallelOldGC main.Main -allocations=10000000 -arrSize=-1 -threads=4
#printf "CMS\t"
#java -Xms128m -Xmx128m -XX:+UseConcMarkSweepGC main.Main -allocations=10000000 -arrSize=1
#printf "G1\t"
#java -Xms128m -Xmx128m -XX:+UseG1GC main.Main -allocations=10000000 -arrSize=1


# garbage collection
rm -r benchmark/ main/
