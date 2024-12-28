import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manager {
    private ParcelMap parcelMap;
    private QueueOfCustomers queueOfCustomers;

    public Manager() {
        parcelMap = new ParcelMap();
        queueOfCustomers = new QueueOfCustomers();

        // Load data from provided files
        loadParcelsFromCSV("Parcels.csv");
        loadCustomersFromCSV("Custs.csv");
    }

    public void displayGUI() {
        JFrame frame = new JFrame("Depot Parcel Processing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns

        JTextArea customerQueueArea = new JTextArea();
        customerQueueArea.setEditable(false);
        updateCustomerQueue(customerQueueArea);
        JScrollPane customerQueueScrollPane = new JScrollPane(customerQueueArea);
        customerQueueScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));
        mainPanel.add(customerQueueScrollPane);

        JTextArea parcelListArea = new JTextArea();
        parcelListArea.setEditable(false);
        updateParcelList(parcelListArea);
        JScrollPane parcelListScrollPane = new JScrollPane(parcelListArea);
        parcelListScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));
        mainPanel.add(parcelListScrollPane);

        JTextArea parcelDetailsArea = new JTextArea();
        parcelDetailsArea.setEditable(false);
        JScrollPane parcelDetailsScrollPane = new JScrollPane(parcelDetailsArea);
        parcelDetailsScrollPane.setBorder(BorderFactory.createTitledBorder("Parcel Details"));
        mainPanel.add(parcelDetailsScrollPane);

        JPanel buttonPanel = new JPanel();

        JButton processButton = new JButton("Process Next Customer");
        processButton.addActionListener(e -> {
            Worker worker = new Worker(parcelMap, queueOfCustomers);
            Customer customer = queueOfCustomers.peekCustomer(); // Peek at the next customer

            if (customer != null) {
                Parcel parcel = parcelMap.getParcel(customer.getParcelId());
                if (parcel != null) {
                    worker.processNextCustomer();
                    parcelDetailsArea.setText("Customer Name: " + customer.getName() +
                            "\nParcel ID: " + parcel.getId() +
                            "\nDimensions: " + parcel.getLength() + "x" + parcel.getWidth() + "x" + parcel.getHeight() +
                            "\nWeight: " + parcel.getWeight() + "kg" +
                            "\nDays in Depot: " + parcel.getDaysInDepot() +
                            "\nStatus: " + parcel.getStatus());
                } else {
                    parcelDetailsArea.setText("Customer Name: " + customer.getName() +
                            "\nNo parcel found for Parcel ID: " + customer.getParcelId());
                }
            } else {
                parcelDetailsArea.setText("No more customers in the queue.");
                Log.getInstance().saveLogToFile("log.txt");
                JOptionPane.showMessageDialog(frame, "All customers processed. Log saved to log.txt.");
            }

            updateCustomerQueue(customerQueueArea);
            updateParcelList(parcelListArea);
        });
        buttonPanel.add(processButton);

        JButton saveLogButton = new JButton("Save Log");
        saveLogButton.addActionListener(e -> {
            Log.getInstance().saveLogToFile("log.txt");
            JOptionPane.showMessageDialog(frame, "Log saved to log.txt.");
        });
        buttonPanel.add(saveLogButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void updateParcelList(JTextArea parcelListArea) {
        StringBuilder sb = new StringBuilder("Parcels:\n");
        for (Parcel parcel : parcelMap.getAllParcels()) {
            sb.append(parcel)
                    .append(", Status=")
                    .append(parcel.getStatus())
                    .append("\n");
        }
        parcelListArea.setText(sb.toString());
    }

    private void updateCustomerQueue(JTextArea customerQueueArea) {
        StringBuilder sb = new StringBuilder("Customer Queue:\n");
        for (Customer customer : queueOfCustomers.getAllCustomers()) {
            sb.append(customer).append("\n");
        }
        customerQueueArea.setText(sb.toString());
    }

    private void loadParcelsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.err.println("Skipping invalid line in parcels file: " + line);
                    continue;
                }
                try {
                    String id = data[0];
                    int daysInDepot = Integer.parseInt(data[1]);
                    double weight = Double.parseDouble(data[2]);
                    double length = Double.parseDouble(data[3]);
                    double width = Double.parseDouble(data[4]);
                    double height = Double.parseDouble(data[5]);

                    Parcel parcel = new Parcel(id, length, width, height, weight, daysInDepot);
                    parcel.setStatus("Waiting");
                    parcelMap.addParcel(parcel);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Skipping invalid line due to parsing error: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading parcel file: " + e.getMessage());
        }
    }

    private void loadCustomersFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int sequenceNumber = 1;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",");
                if (data.length < 2) {
                    System.err.println("Skipping invalid line in customers file: " + line);
                    continue;
                }
                String name = data[0];
                String parcelId = data[1];
                Customer customer = new Customer(sequenceNumber++, name, parcelId);
                queueOfCustomers.addCustomer(customer);
            }
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Manager manager = new Manager();
            manager.displayGUI();
        });
    }
}
