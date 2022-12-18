public class Main {
    public static void main(String[] args) {
        int MAX_SIZE = 5;
        AssemblyLine assemblyLine = new AssemblyLine(MAX_SIZE);
        for (int i = 0; i < 3; i++) {
            Robot robot = new Robot(assemblyLine, MAX_SIZE);
            Thread robotThread = new Thread(robot, "robot_producer_" + (i + 1));
            robotThread.start();
        }

        for (int i = 0; i < 3; i++) {
            Worker worker = new Worker(assemblyLine);
            Thread workerThread = new Thread(worker, "worker_consumer_" + (i + 1));
            workerThread.start();
        }
    }
}
