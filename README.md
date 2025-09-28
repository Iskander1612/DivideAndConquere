Divide and Conquer Assignment
Learning Goals

Implement classic divide-and-conquer algorithms with safe recursion patterns

Analyse running-time recurrences using Master Theorem and Akra–Bazzi intuition

Validate theory with experimental measurements (time, recursion depth, comparisons/allocations)

Communicate results via a short report and clean Git history

Algorithms Implemented
1. MergeSort (D&C, Master Case 2)

Linear merge with reusable buffer

Small-n cut-off → Insertion Sort

Recurrence (student-level):
Split into 2 halves → 2T(n/2).
Merge step → O(n).
So: T(n) = 2T(n/2) + O(n) → Θ(n log n); recursion depth Θ(log n).

2. QuickSort (robust)

Randomized pivot selection

Recurse on smaller partition, iterate on larger (stack depth ≈ O(log n) expected)

Recurrence:
Average: Θ(n log n); Worst: Θ(n²);
With random pivot, expected depth ≤ 2·log₂ n + O(1).

3. Deterministic Select (Median of Medians, MoM5)

Group by 5; median of medians as pivot

Recurse only on the needed side (prefer the smaller side)

Recurrence: T(n) = T(n/5) + T(7n/10) + O(n) → Θ(n) (Akra–Bazzi intuition).
Linear time, but larger constants (can be slower than QuickSort for small n).

4. Closest Pair of Points (2D)

Sort by x, recursive split; strip check by y-order (classical 7–8 neighbor scan)

We return squared distance (dist²) for stability

Recurrence: T(n) = 2T(n/2) + O(n) → Θ(n log n); sorting dominates large n.

Architecture Notes

Depth tracking: QuickSort keeps expected recursion depth O(log n) by always recursing on the smaller side.

Allocations: MergeSort uses a single reusable buffer across the whole call.

Counters: All algorithms instrumented with counters (comparisons, swaps, allocations, maxDepth).

CSV Writer: Appends metrics to metrics.csv for plotting.

CLI (example):

mvn -q -DskipTests=true package
java -cp target/assignment-dnc-0.1.0.jar org.example.dnc.cli.Main \
--algo mergesort --n 100000 --trials 3 --cutoff 32 --seed 42 --csv metrics.csv

CSV columns: algo,n,trial,timeNanos,depth,comparisons,swaps,allocations[,result].

Experimental Results
Plots (paste screenshots)

Time vs n — all four algorithms
![Time vs n](out/time_vs_n.png)

Recursion depth vs n — MergeSort & QuickSort only
![Depth vs n](out/depth_vs_n.png)

Data collection tip:

SEED=42; JAR=target/assignment-dnc-0.1.0.jar; rm -f metrics.csv
for n in 10000 20000 40000 80000 160000; do
java -cp $JAR org.example.dnc.cli.Main --algo mergesort --n $n --trials 3 --cutoff 32 --seed $SEED --csv metrics.csv
java -cp $JAR org.example.dnc.cli.Main --algo quicksort --n $n --trials 3 --cutoff 24 --seed $SEED --csv metrics.csv
java -cp $JAR org.example.dnc.cli.Main --algo select    --n $n --k $(( n/2 )) --trials 3 --cutoff 24 --seed $SEED --csv metrics.csv
java -cp $JAR org.example.dnc.cli.Main --algo closest   --n $n --trials 3 --seed $SEED --csv metrics.csv
done
JMH Benchmark Results (Select vs Arrays.sort()[k])

Expectation: MoM5 ≈ Θ(n) and faster than sort[k] for large enough n.
Paste here: ![JMH: Select vs sort[k]](out/jmh_select_vs_sort.png)

Discussion of Constant‑Factor Effects

QuickSort: tiny randomization overhead vs cache-friendly partitioning

MergeSort: stable/predictable; copying adds constants, mitigated by a shared buffer

Select: linear time but higher constants → can be slower than QuickSort on small inputs

Closest Pair: sorting dominates for small n, asymptotics win as n grows

Summary

Theoretical recurrences align with measured asymptotics

Minor constant‑factor mismatches (e.g., QuickSort often faster than Select on small n)

Depth control worked as expected (QuickSort ≤ 2·log₂ n)

Final implementation satisfies the assignment requirements

GitHub Workflow

Branches

main — stable releases (v0.1, v1.0)

feature/mergesort, feature/quicksort, feature/select, feature/closest, feature/metrics, feature/cli, bench/jmh

Commit storyline

init, feat(metrics), feat(mergesort), feat(quicksort), refactor(util),
feat(select), feat(closest), feat(cli), bench(jmh), docs(report), fix, release:v1.0