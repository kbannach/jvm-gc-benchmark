#!/bin/bash

view_usage() {
    sh -c 'java main.Main'
}

# TODO
# 1. java args from script execution erguments
# 2. loop [DONE]

# create temporary files
cp -r ../java/* .
# build
javac main/Main.java

# parse arguments
if [ $# -eq 0 ]
then
    view_usage
else
    allocations='-allocations='$1
    arrSize='-arrSize='$2
    threads='-threads='$3
    echo $allocations' '$arrSize' '$threads
    exit
fi

# create results file
touch results.txt
printf "heap size: 128 MB\n" >> results.txt
printf "GC algorithm\t1 thd, fixed size\t1 thd, random size\tn thds, fixed size\tn thds, random size\t" >> results.txt

# run benchmark
algorithms=(ParrallelOld CMS G1)

for alg in  ${algorithms[*]}; do
    printf 'executing: java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=300'
    sh -c 'java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=300' >> results.txt

    printf 'executing: java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=-1'
    sh -c 'java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=-1' >> results.txt

    printf 'executing: java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=300 -threads=4'
    sh -c 'java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=300 -threads=4' >> results.txt

    printf 'executing: java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=-1 -threads=4'
    sh -c 'java -Xms128m -Xmx128m -XX:+Use'$alg'GC main.Main -allocations=10000000 -arrSize=-1 -threads=4' >> results.txt
done


# garbage collection
rm -r benchmark/ main/
