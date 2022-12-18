import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

class Buffer {
    private final BlockingQueue<Integer> queue;
    private final Semaphore semaphore;

    public Buffer(int size) {
        queue = new ArrayBlockingQueue<>(size);
        semaphore = new Semaphore(1);
    }

    public void add(int item) throws InterruptedException {
        semaphore.acquire();
        queue.put(item);
        semaphore.release();
    }

    public int remove() throws InterruptedException {
        semaphore.acquire();
        int item = queue.take();
        semaphore.release();
        return item;
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
                Thread.sleep(2000);
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