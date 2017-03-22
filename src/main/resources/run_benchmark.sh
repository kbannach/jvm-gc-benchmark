#!/bin/bash

create_temp() {
    # create temporary files
    cp -r ../java/* .
    # build
    javac main/Main.java
}

cleanup() {
    rm -r benchmark/ main/
}

view_usage() {
    sh -c 'java main.Main'
    cleanup
    exit
}

exec_java() {
    # $1 algorithm; $2 heap size; $3 allocations; $4 arrSize; $5 threads

    printf "executing: java "$2" -XX:+Use"$1"GC main.Main "$3" "$4" "$5" \n"
    sh -c "java "$2" -XX:+Use"$1"GC main.Main "$3" "$4" "$5" \n" >> results.txt
}

# begin script
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
	    -allocations* ) allocations=$a
		;;
	    -arrSize* ) arrSize=$a
		;;
	    -threads* ) threads=$a
		;;
	    *) echo "Unknown option: "$a
		;;
	esac
    done
fi

# create results file
rm results.txt
touch results.txt

# run benchmark
algorithms=(ParallelOld ConcMarkSweep G1)
heap_sizes=(128 256 512)
for hs in ${heap_sizes[*]}; do
    printf "heap size: "$hs" MB\n" >> results.txt
    printf "GC algorithm\t1 thd, fixed size\t1 thd, random size\tn thds, fixed size\tn thds, random size\n" >> results.txt

    heap_size='-Xms'$hs'm -Xmx'$hs'm'
    for alg in ${algorithms[*]}; do
	# print algorithm name with whitespace
	p=$alg"\t\t"
	t="\t"
	p="${p//G1/G1$t}"
	printf $p >> results.txt

	# run commands
	# $1 algorithm; $2 heap size; $3 allocations; $4 arrSize; $5 threads
	exec_java $alg $heap_size $allocations $arrSize '' ;
	printf "\t\t\t" >> results.txt ;
	exec_java $alg $heap_size $allocations '-arrSize=-1' '' ;
	printf "\t\t\t" >> results.txt ;
	exec_java $alg $heap_size $allocations $arrSize $threads ;
	printf "\t\t\t" >> results.txt ;
	exec_java $alg $heap_size $allocations '-arrSize=-1' $threads ;
	printf "\n" >> results.txt ;
    done

    printf "\n\n" >> results.txt
done

# garbage collection
cleanup
