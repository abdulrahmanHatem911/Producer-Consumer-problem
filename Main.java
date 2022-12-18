import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Random;

class Buffer {
    private final BlockingQueue<Integer> queue;

    public Buffer(int size) {
        queue = new ArrayBlockingQueue<>(size);
    }

    public void add(int item) throws InterruptedException {
        while (true) {
            if (queue.offer(item)) {
                break;
            }
            Thread.sleep(1);
        }
    }

    public int remove() throws InterruptedException {
        while (true) {
            if (queue.size() > 0) {
                return queue.poll();
            }
            Thread.sleep(1);
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
