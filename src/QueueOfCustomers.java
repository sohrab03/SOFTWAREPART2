import java.util.LinkedList;
import java.util.Queue;

public class QueueOfCustomers {
    private Queue<Customer> customerQueue;

    public QueueOfCustomers() {
        this.customerQueue = new LinkedList<>();
    }

    // Add a customer to the queue
    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    // Serve (remove and return) the next customer in the queue
    public Customer serveCustomer() {
        return customerQueue.poll(); // Removes the first customer
    }

    // Peek at the next customer in the queue without removing them
    public Customer peekCustomer() {
        return customerQueue.peek(); // Returns the first customer without removing
    }

    // Get the size of the queue
    public int getQueueSize() {
        return customerQueue.size();
    }

    // Get all customers in the queue
    public Iterable<Customer> getAllCustomers() {
        return customerQueue;
    }
}
