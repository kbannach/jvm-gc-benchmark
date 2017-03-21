#!/bin/bash

# TODO
# 1. java args from script execution erguments
# 2. loop [DONE]

view_usage() {
    sh -c 'java main.Main'
    exit
}

create_temp() {
    # create temporary files
    cp -r ../java/* .
    # build
    javac main/Main.java
}

cleanup() {
    rm -r benchmark/ main/
}

exec_java() {
# $1 algorithm; $2 heap size; $3 allocations; $4 arrSize; $5 threads

    printf 'executing: java '$2' -XX:+Use'$1'GC main.Main '$3' '$4' '$5'\n'
    sh -c 'java '$2' -XX:+Use'$1'GC main.Main '$3' '$4' '$5'\n' >> results.txt
}

# script
create_temp

# parse arguments
allocations=''
arrSize=''
threads=''
if [ $# -eq 0 ]
then
    view_usage
else
    args=( "$@" )
    for a in ${args[*]}; do
	case $a in
	    -allocations* ) allocations='-allocations='$a
		;;
	    -arrSize* ) arrSize='-arrSize='$a
		;;
	    -threads* ) threads='-threads='$a
		;;
	    *) echo "Unknown option: "$a
		;;
	esac
    done
#    exit # TODO
fi

# create results file
rm results.txt
touch results.txt
# TODO heap size in loop
printf "heap size: 128 MB\n" >> results.txt
printf "GC algorithm\t1 thd, fixed size\t1 thd, random size\tn thds, fixed size\tn thds, random size\t" >> results.txt

# run benchmark
algorithms=(ParallelOld) # CMS G1)
heap_sizes=(128) # 256 512)
for hs in ${heap_sizes[*]}; do
    heap_size="-Xms"$hs"m -Xmx"$hs"m"
    for alg in ${algorithms[*]}; do
	# run commands
	# $1 algorithm; $2 heap size; $3 allocations; $4 arrSize; $5 threads
	exec_java $alg $heap_size $allocations $arrSize '' ;
	exec_java $alg $heap_size $allocations '-arrSize=-1' '' ;
	exec_java $alg $heap_size $allocations $arrSize $threads ;
	exec_java $alg $heap_size $allocations '-arrSize=-1' $threads
    done
done

# garbage collection
cleanup
