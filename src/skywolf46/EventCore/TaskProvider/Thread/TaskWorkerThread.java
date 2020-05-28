package skywolf46.EventCore.TaskProvider.Thread;

import skywolf46.EventCore.Data.TaskGroup;
import skywolf46.EventCore.Data.TaskPool;

import java.util.concurrent.CountDownLatch;

public class TaskWorkerThread extends Thread {
    private TaskPool tp;

    public TaskWorkerThread(TaskPool tp) {
        this.tp = tp;
    }

    @Override
    public void run() {
        while (true) {
            TaskGroup tg = tp.getNextTask();
            Runnable r = tg.getNextTask();
            while (r != null) {
                r.run();
                r = tg.getNextTask();
            }
//                r = tg.getNextTask();
//            }
//            else
//                System.out.println("Null, continue");
            tp.returnTaskGroup(tg);
        }
    }
}
