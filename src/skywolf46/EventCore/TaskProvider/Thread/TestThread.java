package skywolf46.EventCore.TaskProvider.Thread;

import skywolf46.EventCore.Data.TaskPool;
import skywolf46.EventCore.EventCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestThread extends Thread {
    public TaskPool tp;
    private static int data = 0;
    private int dat;
    private long total = 0;
    private int next = 0;
    private int minTask;
    private int maxTask;
    private long maxWait;
    private long minWait = Long.MAX_VALUE;
    private long max = 30000;
    private int cur;
    private static Random r = new Random();
    private HashMap<Long, Integer> vals = new HashMap<>();
    private List<Integer> val = new ArrayList<>();

    public TestThread(TaskPool tp) {
        this.tp = tp;
        this.dat = data++;
        this.tp.registerTaskPool("TestTask-" + dat);
    }

    @Override
    public void run() {
        System.out.println("Thread " + dat + ": Warming up...");
        for (int i = 0; i < 5000; i++) {

        }
        System.out.println("Thread " + dat + ": Ready, starting task.");
        for (int i = 0; i < max; i++)
            val.add(i);
//        System.out.println("start");
        for (int i = 0; i < 3000; i++) {
//            System.out.println("Adding task in " + dat);
            final long current = System.nanoTime();
            for (int x = 0; x < 10; x++) {
                int cache = cur++;
                tp.addTask("TestTask-" + dat, () -> {
                    long val = (System.nanoTime() - current);
                    if (val > maxWait) {
                        maxWait = val;
                        maxTask = next;
                    }
                    if (minWait > val) {
                        minWait = val;
                        minTask = next;
                    }
//                    TestThread.this.val.remove((Integer) cur);
//                    System.out.println("Task in thread " + dat + " running / Elapsed " + val + "ns");
                    total += val;
                    next++;
//                    val = val - val % 10000;
//                    vals.put(val, vals.getOrDefault(val, 0) + 1);
//                    next++;
                });
            }
            try {
                Thread.sleep(r.nextInt(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Thread " + dat + " complete.");
        long current = System.nanoTime();
        tp.addTask("TestTask-" + dat, () -> {
//            System.out.println(vals);
            System.out.println("Test complete; Task group " + dat + " average wait: " + (total / max) + "ns / Result wait: " + (System.nanoTime() - current) + "ns / Tasks: " + next + "(" + ((double) next / (double) max * 100) + "%) / Max wait " + maxWait + "ns(On task " + maxTask + ") / Min wait " + minWait + "(On task " + minTask + ")");
//            if (val.size() > 0)
//                System.out.println("Los: " + val);
        });
    }

    public int getDrop() {
        return (int) (max - next);
    }
}
