#!/bin/bash

case $1 in
	"1" )	echo CoarseGrainedListIntSet
			OUTPUT="CoarseGrainedListBasedSet"
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


for i in 12 10 8 6 4 1
do
	for j in 0 10 100
	do
		for k in 100 1000 10000
		do
			for l in 1 2 3 4 5
			do
				echo "→ $OUTPUT $i $j  $k  $l" >> output_$OUTPUT.txt
				java -cp bin contention.benchmark.Test -b linkedlists.lockbased.$OUTPUT -d 3000 -t $i -u $j -i $k -r $((2*$k)) -W 0 | grep Throughput >> output_$OUTPUT.txt
			done
		done
	done
	# wait
done
echo "finished on" `date`
echo "DONE \o/"

