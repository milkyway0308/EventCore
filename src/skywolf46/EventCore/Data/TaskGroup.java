package skywolf46.EventCore.Data;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskGroup {
    private TransferQueue<Runnable> tasks = new LinkedTransferQueue<>();

    public void addTask(Runnable r) {
        tasks.add(r);
        //        this.tasks.add(r);
//        System.out.println("Task added / " + size());
    }

    public void addTasks(List<Runnable> r) {
        this.tasks.addAll(r);
    }

    public Runnable getNextTask() {
        return tasks.poll();
    }


    public boolean hasNextTask() {
        return tasks.size() > 0;
    }

    public int size() {
        return tasks.size();
    }
}
