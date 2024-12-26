import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class QueueOfCustomers {
    private Queue<Customer> customerQueue;

    public QueueOfCustomers() {
        customerQueue = new LinkedList<>();
    }

    // Adding Customers To the Queue
    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    // remove and return customers to the queue
    public Customer serveCustomer() {
        return customerQueue.poll();
    }

    // check what size the queue is
    public int getQueueSize() {
        return customerQueue.size();
    }

    // print all the customers in the queue
    public void printQueue() {
        for (Customer customer : customerQueue) {
            System.out.println(customer);
        }
    }

    //
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerQueue);
    }
}
