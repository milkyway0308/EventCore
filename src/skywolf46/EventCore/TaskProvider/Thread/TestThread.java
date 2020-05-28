package skywolf46.EventCore.TaskProvider.Thread;

import skywolf46.EventCore.Data.TaskPool;
import skywolf46.EventCore.EventCore;

public class TestThread extends Thread {
    public TaskPool tp;
    private static int data = 0;
    private int dat;
    private long total = 0;
    private int next = 0;
    private long min;
    private long max = 40000;

    public TestThread(TaskPool tp) {
        this.tp = tp;
        this.dat = data++;
        this.tp.registerTaskPool("TestTask-" + dat);
    }

    @Override
    public void run() {
//        System.out.println("start");
        for (int i = 0; i < 4000; i++) {
//            System.out.println("Adding task in " + dat);
            final long current = System.nanoTime();
            for (int x = 0; x < 10; x++)
                tp.addTask("TestTask-" + dat, () -> {
                    long val = (System.nanoTime() - current);
//                    System.out.println("Task in thread " + dat + " running / Elapsed " + val + "ns");
                    total += val;
                    next++;
//                    next++;
                });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Thread " + dat + " complete.");
        long current = System.nanoTime();
        tp.addTask("TestTask-" + dat, () -> {
            System.out.println("Test complete; Task group " + dat + " average wait: " + (total / max) + "ns / Result wait: " + (System.nanoTime() - current) + "ns / Tasks: " + next + "(" + ((double) next / (double) max * 100) + "%)");
        });
    }
}
