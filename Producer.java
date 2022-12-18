public class Producer extends Thread {
    private Buffer buffer;
    private int size;

    public Producer(Buffer bufferQueue, int maxSize) {
        this.buffer = bufferQueue;
        this.size = maxSize;
    }
    @Override
    public void run() {
        while (true) {
            try {
                synchronized (buffer) {
                    // Wait until the assembly line is not full
                    while (buffer.getCarParts().size() == size) {
                        System.out.println("The buffer is full waiting the Consumer");
                        buffer.wait();
                    }
                    // Produce a car part
                    Thread.sleep(1000);
                    CarPart carPart = produceCarPart();
                    // Add the car part to the assembly line
                    buffer.addCarPart(carPart);
                    System.out.println(Thread.currentThread().getName() + " add new value: Color==> " + carPart.getColor() + " Type=> " + carPart.getType());
                    buffer.notifyAll();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception @ Producer Code");
                e.printStackTrace();
            }
        }
    }

    private CarPart produceCarPart() {
        // Create a new car part
        CarPart carPart = new CarPart();
        // Set the properties of the car part
        carPart.setType("Door");
        carPart.setColor("Red");
        // Return the car part
        return carPart;
    }
}