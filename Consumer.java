public class Consumer extends Thread {
    private Buffer buffer;

    public Consumer(Buffer bufferQueue) {
        this.buffer = bufferQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (buffer) {
                    while (buffer.getCarParts().isEmpty()) {
                        System.out.println("The buffer is empty wait the producer");
                        buffer.wait();
                    }
                    CarPart carPart = buffer.removeCarPart();
                    installCarPart(carPart);
                }
            } catch (InterruptedException e) {
                // Handle the exception
            }
        }
    }
    private void installCarPart(CarPart carPart) {
        try {
            Thread.sleep(500);
            System.out.println("\t" + Thread.currentThread().getName() + " installing Type=> " + carPart.getType() + " with Color=> " + carPart.getColor());
            buffer.notifyAll();
        } catch (InterruptedException e) {
            // Handle the exception
        }

    }
}