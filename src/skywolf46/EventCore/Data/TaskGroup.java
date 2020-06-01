package skywolf46.EventCore.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskGroup {
    private final Object LOCK = new Object();
    // Causes deadlock, but why?
//    private Queue<Runnable> tasks = new ArrayDeque<>();
    private List<Runnable> tasks = new ArrayList<>();

    public void addTask(Runnable r) {
        synchronized (LOCK) {
            tasks.add(r);
        }
        //        this.tasks.add(r);
//        System.out.println("Task added / " + size());
    }

    public void addTasks(List<Runnable> r) {
        this.tasks.addAll(r);
    }

    public Runnable getNextTask() {
        synchronized (LOCK) {
            if (tasks.size() > 0)
                return tasks.remove(0);
            return null;
        }
    }


    public boolean hasNextTask() {
        return tasks.size() > 0;
    }

    public int size() {
        return tasks.size();
    }

    public void fill(List<Runnable> tasks) {
//        this.tasks.drainTo(tasks);
    }
}
