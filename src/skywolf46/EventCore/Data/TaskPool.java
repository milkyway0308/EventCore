package skywolf46.EventCore.Data;

import skywolf46.EventCore.TaskProvider.Thread.TaskWorkerThread;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;

public class TaskPool {
    private HashMap<String, TaskGroup> group = new HashMap<>();
    private Queue<TaskGroup> free = new LinkedBlockingQueue<>();
    private final Object waiter = new Object();
    private final Object taskGetter = new Object();


    public TaskPool(int threadAmount) {
        for (int i = 0; i < threadAmount; i++) {
            TaskWorkerThread twt = new TaskWorkerThread(this);
            twt.start();
        }
    }

    public void registerTaskPool(String task) {
        synchronized (taskGetter) {
            TaskGroup rg = new TaskGroup();
            this.group.put(task, rg);
        }
    }

    public void addTask(String group, Runnable task) {
        if (!this.group.containsKey(group))
            throw new IllegalStateException("Task group " + group + " not registered");
        TaskGroup tg = this.group.get(group);
        tg.addTask(task);
        if (tg.size() == 1) {
            synchronized (taskGetter) {
                free.add(tg);
            }
            synchronized (waiter) {
                waiter.notify();
            }
        }
    }

    public void returnTaskGroup(TaskGroup tr) {
        if (tr.hasNextTask()) {
            synchronized (taskGetter) {
                free.add(tr);
            }
        }
    }

    public TaskGroup getNextTask() {
        TaskGroup nextGroup;
        synchronized (taskGetter) {
            nextGroup = free.poll();
        }
        while (true) {
            synchronized (taskGetter){
                if (nextGroup != null && nextGroup.hasNextTask()) {
                    break;
                }
            }
            synchronized (waiter) {
                try {
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (taskGetter) {
                nextGroup = free.poll();
            }
        }
        return nextGroup;
    }

}
