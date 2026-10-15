#!/bin/bash

case $1 in
	"1" )	echo CoarseGrainedListIntSet
			OUTPUT="CoarseGrainedListIntSet"
	;;
	"2" )	echo HandOverHandListIntSet
			OUTPUT="HandOverHandListSet"
	;;
	"3" )	echo LazyLinkedListSortedSet
			OUTPUT="LazyLinkedListSortedSet"
	;;
	*)		echo "Specify algorithm"
			exit 0
esac

echo "Who I am: $OUTPUT on `uname -n`"
echo "started on" `date`


for i in 1 2 4 6 8 10 12
do
	for j in 0 10 25 50 75 100
	do
		for k in 100 1000 10000
		do
			for l in 'seq 1 5'
			do
				echo "→ $OUTPUT	$i	$j  $k  $l"
				java -cp bin contention.benchmark.Test -b linkedlists.lockbased.$OUTPUT -d 3000 -t $i -u $j -i $k -r 2048 -W 0 | grep Throughput
			#	echo "→ $OUTPUT	$i	$j	without -W 0"
			#	java -cp bin contention.benchmark.Test -b linkedlists.lockbased.$OUTPUT -d 3000 -t $i -u $j -i 1024 -r 2048 | grep Throughput
			done
		done
	done 
	# wait
done
echo "finished on" `date`
echo "DONE \o/"

