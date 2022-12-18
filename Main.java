import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Buffer {
    private final BlockingQueue<Integer> queue;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public Buffer(int size) {
        queue = new ArrayBlockingQueue<>(size);
    }

    public void add(int item) throws InterruptedException {
        synchronized (lock1) {
            while (queue.size() == queue.remainingCapacity()) {
                lock1.wait();
            }
            queue.put(item);
            lock2.notifyAll();
        }
    }

    public int remove() throws InterruptedException {
        synchronized (lock2) {
            while (queue.size() == 0) {
                lock2.wait();
            }
            int item = queue.take();
            lock1.notifyAll();
            return item;
        }
    }
}

class Producer implements Runnable {
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int item = generateItem();
                buffer.add(item);
                System.out.println("Produced " + item);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    private int generateItem() {
        // generate a random integer between 0 and 100
        return new Random().nextInt(101);
    }
}

class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int item = buffer.remove();
                System.out.println("Consumed " + item);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(10);
        Thread producerThread = new Thread(new Producer(buffer));
        Thread consumerThread = new Thread(new Consumer(buffer));
        producerThread.start();
        consumerThread.start();
    }
}
