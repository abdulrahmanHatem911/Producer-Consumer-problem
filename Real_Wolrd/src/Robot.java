class Robot extends Thread {
    private AssemblyLine assemblyLine;
    private int size;

    public Robot(AssemblyLine assemblyLine, int maxSize) {
        this.assemblyLine = assemblyLine;
        this.size = maxSize;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (assemblyLine) {
                    // Wait until the assembly line is not full
                    while (assemblyLine.getCarParts().size() == size) {
                        System.out.println("The assembly line is full waiting the Worker");
                        assemblyLine.wait();
                    }
                    // Produce a car part
                    Thread.sleep(500);
                    CarPart carPart = produceCarPart();
                    // Add the car part to the assembly line
                    assemblyLine.addCarPart(carPart);
                    System.out.println(Thread.currentThread().getName() + " add new value: Color==> " + carPart.getColor() + " Type=> " + carPart.getType());
                    assemblyLine.notifyAll();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception @ Producer Code");
                e.printStackTrace();
            }
        }
    }

    // Produce a car part
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