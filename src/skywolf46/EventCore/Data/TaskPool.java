package skywolf46.EventCore.Data;

import skywolf46.EventCore.TaskProvider.Thread.TaskWorkerThread;

import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TaskPool {
    private HashMap<String, TaskGroup> group = new HashMap<>();
    private TransferQueue<TaskGroup> free = new LinkedTransferQueue<>();
    private final Object waiter = new Object();


    public TaskPool(int threadAmount) {
        for (int i = 0; i < threadAmount; i++) {
            TaskWorkerThread twt = new TaskWorkerThread(this);
            twt.start();
        }
    }

    public void registerTaskPool(String task) {
        TaskGroup rg = new TaskGroup();
        this.group.put(task, rg);
    }

    public void addTask(String group, Runnable task) {
        if (!this.group.containsKey(group))
            throw new IllegalStateException("Task group " + group + " not registered");
        TaskGroup tg = this.group.get(group);
        tg.addTask(task);
        if (tg.size() == 1) {
            free.add(tg);
            synchronized (waiter){
                waiter.notify();
            }
        }
    }

    public void returnTaskGroup(TaskGroup tr) {
        if (tr.hasNextTask()) {
            free.add(tr);
        }
    }

    public TaskGroup getNextTask() {
        TaskGroup nextGroup = free.poll();
        while (nextGroup == null || !nextGroup.hasNextTask()) {
            synchronized (waiter) {
                try {
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextGroup = free.poll();
        }
        return nextGroup;
    }

}
