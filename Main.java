public class Main {
    public static void main(String[] args) {
        int MAX_SIZE = 5;
        Buffer buffer = new Buffer(MAX_SIZE);
        for (int i = 0; i < 3; i++) {
            Producer robot = new Producer(buffer, MAX_SIZE);
            Thread robotThread = new Thread(robot, "producer_" + (i + 1));
            robotThread.start();
        }

        for (int i = 0; i < 3; i++) {
            Consumer worker = new Consumer(buffer);
            Thread workerThread = new Thread(worker, "consumer_" + (i + 1));
            workerThread.start();
        }
    }
}