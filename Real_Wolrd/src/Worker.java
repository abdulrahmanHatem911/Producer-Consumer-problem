class Worker extends Thread {
    private AssemblyLine assemblyLine;

    public Worker(AssemblyLine assemblyLine) {
        this.assemblyLine = assemblyLine;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (assemblyLine) {
                    while (assemblyLine.getCarParts().isEmpty()) {
                        System.out.println("The assembly line is empty wait the robot");
                        assemblyLine.wait();
                    }
                    CarPart carPart = assemblyLine.removeCarPart();
                    installCarPart(carPart);
                }
            } catch (InterruptedException e) {
                // Handle the exception
            }
        }
    }

    // Install a car part on the car
    private void installCarPart(CarPart carPart) {
        try {
            Thread.sleep(500);
            System.out.println("\t" + Thread.currentThread().getName() + " installing Type=> " + carPart.getType() + " with Color=> " + carPart.getColor());
            assemblyLine.notifyAll();
        } catch (InterruptedException e) {
            // Handle the exception
        }

    }
}
