import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SupplierManagement {
    private static Scanner scan = new Scanner(System.in);
    private static List<Supplier> suppliers = new ArrayList<>();

    public static void showSupMenu() {
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("Supplier Management");
            System.out.println("======================");
            System.out.println("1. Add Supplier");
            System.out.println("2. Update Supplier Information");
            System.out.println("3. Track Supplier Performance");
            System.out.println("4. Add Supplier Performance");
            System.out.println("5. Display All Supplier and Performance");
            System.out.println("6. Delete Supplier");
            System.out.println("7. Exit");
            System.out.print("Enter your choice > ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    ClearScreenUtil.clearScreen();
                    addSupplier();
                    scan.nextLine();
                    break;
                case 2:
                    updateSupplierInformation();
                    sleepUtil.sleep(2000);
                    break;
                case 3:
                    trackSupplierPerformance();
                    sleepUtil.sleep(2000);
                    break;
                case 4:
                    addSupplierPerformance();
                    sleepUtil.sleep(2000);
                    break;
                case 5:
                    displayAllSupplierAndPerformancel();
                    sleepUtil.sleep(2000);
                    break;
                case 6:
                    deleteSupplier();
                    sleepUtil.sleep(2000);
                    break;
                case 7:
                    System.out.print("Press any key to continue...");
                    scan.nextLine();
                    return;
                default:
                    System.out.println("Invalid Choice. Please Try Again");
                    sleepUtil.sleep(2000);
                    break;
            }
        }
    }

    //Add Supplier/Use previous Supplier
    private static void addSupplier() {
        readSuppliersFromFile();

    // Provide options to add a new supplier or select an existing one
    System.out.println("Select an option:");
    System.out.println("1. Add New Supplier");
    System.out.println("2. Select Previous Supplier");
    System.out.print("Enter your choice (1 or 2): ");
    int option = getValidInteger(1, 2);

    if (option == 1) {
        // Add New Supplier
        System.out.print("Enter Supplier Name: ");
        String supplierName = scan.nextLine();
        
        // Check whether the name already exists
        if (isSupplierExists(supplierName)) {
            System.out.println("Error: Supplier \"" + supplierName + "\" already exists.");
            return;  // Exit if the supplier already exists
        }

        System.out.print("Enter Supplier Contact: ");
        String supplierContact = scan.nextLine();
        System.out.print("Enter Supplier Address: ");
        String supplierAddress = scan.nextLine();

        Supplier newSupplier = new Supplier(supplierName, supplierContact, supplierAddress);
        suppliers.add(newSupplier);
        saveSupplierToFile(newSupplier);

        System.out.println("New Supplier \"" + supplierName + "\" added successfully.");
    } else if (option == 2) {
        // Select Previous Supplier
        displayAllSuppliers();

        System.out.print("Enter the name of the supplier to select: ");
        String supplierName = scan.nextLine();

        if (isSupplierExists(supplierName)) {
            readSupplierDetails(supplierName);
        } else {
            System.out.println("Supplier not found.");
        }
    }
}

//Find the previous supplier
    private static void displayAllSuppliers() {
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available.");
            return;
        }    
        System.out.println("Available Suppliers:");
        for (Supplier supplier : suppliers) {
            System.out.println(supplier.getName());
        }
    }

    private static void readSuppliersFromFile() {
        File file = new File("suppliers.txt");
        if (!file.exists()) {
            return;
        }
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] details = line.split(",");
                    if (details.length == 3) {
                        Supplier supplier = new Supplier(details[0], details[1], details[2]);
                        suppliers.add(supplier);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading suppliers from the file.");
            e.printStackTrace();
        }
    }

    //Do validation
    private static boolean isSupplierExists(String supplierName) {
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                return true;
            }
        }
        return false;
    }

    private static void readSupplierDetails(String supplierName) {
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                System.out.println("Supplier Details: " + supplier.toString());
                return;
            }
        }
        System.out.println("Supplier not found.");
    }

    private static void saveSupplierToFile(Supplier supplier) {
        try (FileWriter writer = new FileWriter("suppliers.txt", true)) {
            writer.write(supplier.getName() + "," + supplier.getContact() + "," + supplier.getAddress() + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the supplier to the file.");
            e.printStackTrace();
        }
    }


    private static void updateSupplierInformation() {
        readSuppliersFromFile();

    System.out.print("Enter the name of the supplier you want to update: ");
    String supplierName = scan.nextLine();

    for (Supplier supplier : suppliers) {
        if (supplier.getName().equalsIgnoreCase(supplierName)) {
            System.out.println("Current Supplier Details: " + supplier.toString());
            System.out.print("Enter new Supplier Contact (or press Enter to keep current): ");
            String newContact = scan.nextLine();
            if (!newContact.trim().isEmpty()) {
                supplier.setContact(newContact);
            }
            System.out.print("Enter new Supplier Address (or press Enter to keep current): ");
            String newAddress = scan.nextLine();
            if (!newAddress.trim().isEmpty()) {
                supplier.setAddress(newAddress);
            }
            saveAllSuppliersToFile();
            System.out.println("Supplier information updated successfully.");
            return;
        }
    }
    System.out.println("Supplier not found.");
    }

    //Make sure all changes are recorded
    private static void saveAllSuppliersToFile() {
        try (FileWriter writer = new FileWriter("suppliers.txt")) {
            for (Supplier supplier : suppliers) {
                writer.write(supplier.getName() + "," + supplier.getContact() + "," + supplier.getAddress() + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving suppliers to the file.");
            e.printStackTrace();
        }
    }
    
    //Track the supplier performance
    private static void trackSupplierPerformance() {
        List<SupplierPerformance> performances = readPerformancesFromFile();
            if (performances.isEmpty()) {
                System.out.println("No performance data available.");
                return;
            }
    
            System.out.println("Supplier Performance:");
            for (SupplierPerformance performance : performances) {
                System.out.println("Supplier: " + performance.getSupplierName());
                System.out.println("Overall Performance Score: " + performance.getOverallPerformanceScore() + "/100");
                System.out.println("-------------------------------");
            }
        }
    
        private static List<SupplierPerformance> readPerformancesFromFile() {
            List<SupplierPerformance> performances = new ArrayList<>();
            try (Scanner fileScanner = new Scanner(new File("supplier_performance.txt"))) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        performances.add(new SupplierPerformance(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Performance file not found.");
            }
            return performances;
    }

    //Adding the supplier performance
    private static void addSupplierPerformance() {
        System.out.println("Add Supplier Performance...");
        System.out.print("Enter supplier name: ");
        String name = scan.nextLine();
        System.out.print("Enter on-time delivery rate (%): ");
        int onTimeRate = getValidInteger(0, 100);
        System.out.print("Enter quality score (1-100): ");
        int qualityScore = getValidInteger(1, 100);
        System.out.print("Enter price competitiveness score (1-100): ");
        int priceCompetitiveness = getValidInteger(1, 100);

        SupplierPerformance performance = new SupplierPerformance(name, onTimeRate, qualityScore, priceCompetitiveness);

        try (FileWriter writer = new FileWriter("supplier_performance.txt", true)) {
            writer.write(performance.toString() + System.lineSeparator());
            System.out.println("Supplier performance data added successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void displayAllSupplierAndPerformancel(){
        
        
    }

    //Delete Supplier
    private static void deleteSupplier() {
        System.out.print("Enter the name of the supplier to delete: ");
        String supplierName = scan.nextLine();
        
        Supplier supplierToDelete = null;
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                supplierToDelete = supplier;
                break;
            }
        }
        
        if (supplierToDelete != null) {
            // Remove supplier from the list
            suppliers.remove(supplierToDelete);
            saveAllSuppliersToFile();
            
            // Remove supplier's performance data
            deleteSupplierPerformance(supplierName);
            
            System.out.println("Supplier and associated performance data deleted successfully.");
        } else {
            System.out.println("Supplier not found.");
        }
    }
    
    // Method to delete supplier's performance data from file
    private static void deleteSupplierPerformance(String supplierName) {
        List<SupplierPerformance> performances = readPerformancesFromFile();
        
        // Filter out the performance data for the supplier to be deleted
        performances.removeIf(performance -> performance.getSupplierName().equalsIgnoreCase(supplierName));
        
        // Save the updated performance list back to the file
        try (FileWriter writer = new FileWriter("supplier_performance.txt")) {
            for (SupplierPerformance performance : performances) {
                writer.write(performance.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving updated performance data.");
            e.printStackTrace();
        }
    }

//Supplier Class
    public static class Supplier {
        private String name;
        private String contact;
        private String address;


        public Supplier(String name, String contact, String address) {
            this.name = name;
            this.contact = contact;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "Supplier [Name=" + name + ", Contact=" + contact + ", Address=" + address + "]";
        }
    }

//The Supplier Performance rate
    public static class SupplierPerformance {
        private String supplierName;
        private int onTimeDeliveryRate;         // in %
        private int qualityScore;              // 1-100
        private int priceCompetitiveness;     // 1-100

        public SupplierPerformance(String supplierName, int onTimeDeliveryRate, int qualityScore, int priceCompetitiveness) {
            this.supplierName = supplierName;
            this.onTimeDeliveryRate = onTimeDeliveryRate;
            this.qualityScore = qualityScore;
            this.priceCompetitiveness = priceCompetitiveness;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public int getOverallPerformanceScore() {
            // Weighted score example: 50% on-time, 30% quality, 20% price
            return (int) ((onTimeDeliveryRate * 0.5) + (qualityScore * 0.3) + (priceCompetitiveness * 0.2));
        }

        @Override
        public String toString() {
            return supplierName + "," + onTimeDeliveryRate + "," + qualityScore + "," + priceCompetitiveness;
        }
    }
    private static int getValidInteger(int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scan.nextLine());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
