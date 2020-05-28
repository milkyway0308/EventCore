package skywolf46.EventCore;

import skywolf46.EventCore.Data.TaskPool;
import skywolf46.EventCore.TaskProvider.Thread.TestThread;

public class EventCore {
    public static void main(String[] args) {
        TaskPool tp = new TaskPool(7);
        for (int i = 0; i < 10; i++){
//            System.out.println(i);
            new TestThread(tp).start();
        }
    }
}
