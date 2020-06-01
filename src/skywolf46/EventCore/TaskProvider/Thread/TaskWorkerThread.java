package skywolf46.EventCore.TaskProvider.Thread;

import skywolf46.EventCore.Data.TaskGroup;
import skywolf46.EventCore.Data.TaskPool;

import java.util.ArrayList;
import java.util.List;
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
//            List<Runnable> tasks = new ArrayList<>();
//            tg.fill(tasks);
//            for (Runnable r : tasks)
//                r.run();
            Runnable r = tg.getNextTask();
            if (r == null) {
                System.out.println("Caution : Task drop");

            } else
                while (r != null) {
                    r.run();
                    r = tg.getNextTask();
                }

//            if(r != null)
//                r.run();
//                r = tg.getNextTask();
//            }
//            else
//                System.out.println("Null, continue");
            tp.returnTaskGroup(tg);
        }
    }
}
