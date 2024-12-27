public class Worker {
    private ParcelMap parcelMap;
    private QueueOfCustomers queueOfCustomers;
    private Log log;

    public Worker(ParcelMap parcelMap, QueueOfCustomers queueOfCustomers) {
        this.parcelMap = parcelMap;
        this.queueOfCustomers = queueOfCustomers;
        this.log = Log.getInstance(); // Get the singleton instance of Log
    }

    // Process the next customer in the queue
    public void processNextCustomer() {
        Customer customer = queueOfCustomers.serveCustomer();
        if (customer == null) {
            System.out.println("No customers in the queue.");
            log.addEvent("No customers in the queue.");
            return;
        }

        Parcel parcel = parcelMap.getParcel(customer.getParcelId());
        if (parcel == null) {
            System.out.println("Parcel not found for customer: " + customer.getName());
            log.addEvent("Parcel not found for customer: " + customer.getName());
            return;
        }

        // Calculate the collection fee
        double fee = parcel.calculateFee();
        System.out.println("Processing customer: " + customer.getName());
        System.out.println("Parcel ID: " + parcel.getId() + ", Fee: £" + fee);

        // Update parcel status
        parcel.setStatus("Collected");

        // Log the event
        log.addEvent("Processed customer: " + customer.getName() +
                ", Parcel ID: " + parcel.getId() + ", Fee: £" + fee);

        // Keep parcel in map but mark as collected
        log.addEvent("Parcel " + parcel.getId() + " status updated to Collected.");
    }
}
