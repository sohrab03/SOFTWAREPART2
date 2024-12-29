import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manager {
    private ParcelMap parcelMap;
    private QueueOfCustomers queueOfCustomers;
    private Worker worker;

    public Manager() {
        parcelMap = new ParcelMap();
        queueOfCustomers = new QueueOfCustomers();
        worker = new Worker(parcelMap, queueOfCustomers);

        // Load data from provided files
        loadParcelsFromCSV("Parcels.csv");
        loadCustomersFromCSV("Custs.csv");
    }

    public void displayGUI() {
        JFrame frame = new JFrame("Depot Parcel Processing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        // Parcel List Panel
        JTextArea parcelListArea = new JTextArea();
        parcelListArea.setEditable(false);
        updateParcelList(parcelListArea);
        mainPanel.add(new JScrollPane(parcelListArea));

        // Customer Queue Panel
        JTextArea customerQueueArea = new JTextArea();
        customerQueueArea.setEditable(false);
        updateCustomerQueue(customerQueueArea);
        mainPanel.add(new JScrollPane(customerQueueArea));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();

        // Process Button
        JButton processButton = new JButton("Process Next Customer");
        processButton.addActionListener(e -> {
            worker.processNextCustomer();
            updateParcelList(parcelListArea);
            updateCustomerQueue(customerQueueArea);
        });
        buttonPanel.add(processButton);

        // Save Log Button
        JButton saveLogButton = new JButton("Save Log");
        saveLogButton.addActionListener(e -> {
            Log.getInstance().saveLogToFile("log.txt");
            JOptionPane.showMessageDialog(frame, "Log saved to log.txt");
        });
        buttonPanel.add(saveLogButton);

        mainPanel.add(buttonPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void updateParcelList(JTextArea parcelListArea) {
        StringBuilder sb = new StringBuilder("Parcels in Depot:\n");
        for (Parcel parcel : parcelMap.getAllParcels()) {
            sb.append(parcel).append("\n");
        }
        parcelListArea.setText(sb.toString());
    }

    private void updateCustomerQueue(JTextArea customerQueueArea) {
        StringBuilder sb = new StringBuilder("Customers in Queue:\n");
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
                    continue; // Skip empty lines
                }
                String[] data = line.split(",");
                if (data.length < 6) { // Ensure the line has at least six columns
                    System.err.println("Skipping invalid line in parcels file: " + line);
                    continue;
                }
                try {
                    String id = data[0]; // Parcel ID
                    int daysInDepot = Integer.parseInt(data[1]); // Days in Depot
                    double weight = Double.parseDouble(data[2]); // Weight
                    // Combine the last three columns into the dimensions format "length x width x height"
                    String dimensions = data[3] + "x" + data[4] + "x" + data[5];

                    // Parse dimensions to validate them
                    String[] dimParts = dimensions.split("x");
                    double length = Double.parseDouble(dimParts[0]);
                    double width = Double.parseDouble(dimParts[1]);
                    double height = Double.parseDouble(dimParts[2]);

                    Parcel parcel = new Parcel(id, length, width, height, weight, daysInDepot);
                    parcelMap.addParcel(parcel);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Skipping line due to parsing error in parcels file: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading parcel file: " + e.getMessage());
        }
    }

    private void loadCustomersFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int sequenceNumber = 1; // Start sequence numbers from 1
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
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
