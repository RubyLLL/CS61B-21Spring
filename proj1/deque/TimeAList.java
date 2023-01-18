package deque;

import edu.princeton.cs.algs4.Stopwatch;
public class TimeAList {
    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times, ArrayDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        ArrayDeque<Integer> Ns = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();

        Ns.addLast(1000);
        Ns.addLast(2000);
        Ns.addLast(4000);
        Ns.addLast(8000);
        Ns.addLast(16000);
        Ns.addLast(32000);
        Ns.addLast(64000);
        Ns.addLast(128000);

        for(int n = 0; n < Ns.size(); n++) {
            ArrayDeque<Integer> list = new ArrayDeque<>();
            int opCount = 0;
            for(int i = 0; i < Ns.get(n); i++) {
                list.addLast(n);
            }
            Stopwatch timer = new Stopwatch();
            for(int i = 0; i < Ns.get(n); i++){
                list.removeFirst();
                opCount++;
            }
            opCounts.addLast(opCount);
            times.addLast(timer.elapsedTime());
        }

        printTimingTable(Ns, times, opCounts);
    }
}
