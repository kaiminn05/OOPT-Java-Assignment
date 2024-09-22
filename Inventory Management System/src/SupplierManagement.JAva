import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupplierManagement {
    private static Scanner scan = new Scanner(System.in);
    private static List<Supplier> suppliers = new ArrayList<>();

    public static void main(String[] args) {
        showSupMenu();
    }

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
                    ClearScreenUtil.clearScreen();
                    displayAllSupplierAndPerformancel();
                    System.out.print("Press any key to continue...");
                    scan.nextLine();
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

    // Add Supplier
    private static void addSupplier() {
        System.out.println("Add Supplier...");
        System.out.print("Enter supplier name: ");
        String name = scan.nextLine();
        System.out.print("Enter supplier address: ");
        String address = scan.nextLine();
        System.out.print("Enter supplier contact number: ");
        String contactNum = scan.nextLine();
    
        // Format the supplier details with null placeholders for rating and deliveries
        String supplierDetails = name + "," + contactNum + "," + address + ",0,0";
    
        try (FileWriter writer = new FileWriter("Inventory Management System/resources/supplier.txt", true)) {
            writer.write(supplierDetails + System.lineSeparator());
            System.out.println(ColorUtil.GREEN_BOLD + "Supplier added successfully!" + ColorUtil.RESET);
        } catch (IOException e) {
            System.out.println(ColorUtil.RED_BOLD + "An error occurred while writing to the file." + ColorUtil.RESET);
            e.printStackTrace();
        }
    }
    

    private static void updateSupplierInformation() {
    System.out.print("Enter the name of the supplier you want to update: ");
    String supplierName = scan.nextLine();
    boolean found = false;
    List<String> supplierList = new ArrayList<>();

    try (Scanner fileScanner = new Scanner(new File("Inventory Management System/resources/supplier.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] details = line.split(",");

            if (details.length == 5) {
                String name = details[0];
                String contact = details[1];
                String address = details[2];
                int rating = Integer.parseInt(details[3]);
                int deliveries = Integer.parseInt(details[4]);

                if (name.equalsIgnoreCase(supplierName)) {
                    System.out.println("Supplier found: " + name);

                    System.out.print("Enter new name (leave blank to keep current): ");
                    String newName = scan.nextLine();
                    if (!newName.trim().isEmpty()) {
                        name = newName;
                    }

                    System.out.print("Enter new contact (leave blank to keep current): ");
                    String newContact = scan.nextLine();
                    if (!newContact.trim().isEmpty()) {
                        contact = newContact;
                    }

                    System.out.print("Enter new address (leave blank to keep current): ");
                    String newAddress = scan.nextLine();
                    if (!newAddress.trim().isEmpty()) {
                        address = newAddress;
                    }

                    found = true;
                }
                supplierList.add(name + "," + contact + "," + address + "," + rating + "," + deliveries);
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println(ColorUtil.RED_BOLD + "Supplier file not found." + ColorUtil.RESET);
        e.printStackTrace();
        return;
    }

    if (!found) {
        System.out.println(ColorUtil.RED_BOLD + "Supplier not found." + ColorUtil.RESET);
        return;
    }

    try (FileWriter writer = new FileWriter("Inventory Management System/resources/supplier.txt")) {
        for (String supplier : supplierList) {
            writer.write(supplier + System.lineSeparator());
        }
        System.out.println(ColorUtil.BLUE_BOLD + "Supplier information updated successfully!" + ColorUtil.RESET);
    } catch (IOException e) {
        System.out.println(ColorUtil.RED_BOLD + "An error occurred while writing to the file." + ColorUtil.RESET);
        e.printStackTrace();
    }
}


    // Track the supplier performance
    private static void trackSupplierPerformance() {
        System.out.print("Enter the name of the supplier you want to track: ");
        String supplierName = scan.nextLine();
        boolean found = false;
    
        try (Scanner fileScanner = new Scanner(new File("Inventory Management System/resources/supplier.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
    
                if (details.length == 5) {
                    String name = details[0];
                    String contact = details[1];
                    String address = details[2];
                    int rating = Integer.parseInt(details[3]);
                    int deliveries = Integer.parseInt(details[4]);
    
                    if (name.equalsIgnoreCase(supplierName)) {
                        found = true;
                        System.out.println("Supplier performance:");
                        System.out.println("Name: " + name);
                        System.out.println("Contact: " + contact);
                        System.out.println("Address: " + address);
                        System.out.println("Rating: " + rating);
                        System.out.println("Deliveries: " + deliveries);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier file not found." + ColorUtil.RESET);
            e.printStackTrace();
        }
    
        if (!found) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier not found." + ColorUtil.RESET);
        }
    }
    

    // Adding the supplier performance
    private static void addSupplierPerformance() {
        System.out.print("Enter the name of the supplier to update performance: ");
        String supplierName = scan.nextLine();
        boolean found = false;
        List<String> supplierList = new ArrayList<>();
    
        try (Scanner fileScanner = new Scanner(new File("Inventory Management System/resources/supplier.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
    
                if (details.length == 5) {
                    String name = details[0];
                    String contact = details[1];
                    String address = details[2];
                    int rating = Integer.parseInt(details[3]);
                    int deliveries = Integer.parseInt(details[4]);
    
                    if (name.equalsIgnoreCase(supplierName)) {
                        System.out.print("Enter new rating (current: " + rating + "): ");
                        int newRating = scan.nextInt();
                        System.out.print("Enter new deliveries count (current: " + deliveries + "): ");
                        int newDeliveries = scan.nextInt();
                        scan.nextLine();
    
                        rating = newRating;
                        deliveries = newDeliveries;
                        found = true;
                    }
                    supplierList.add(name + "," + contact + "," + address + "," + rating + "," + deliveries);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier file not found." + ColorUtil.RESET);
            e.printStackTrace();
            return;
        }
    
        if (!found) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier not found." + ColorUtil.RESET);
            return;
        }
    
        try (FileWriter writer = new FileWriter("supplier.txt")) {
            for (String supplier : supplierList) {
                writer.write(supplier + System.lineSeparator());
            }
            System.out.println(ColorUtil.GREEN_BOLD + "Supplier performance updated successfully!" + ColorUtil.RESET);
        } catch (IOException e) {
            System.out.println(ColorUtil.RED_BOLD + "An error occurred while writing to the file." + ColorUtil.RESET);
            e.printStackTrace();
        }
    }
    

    // Display all the supplier information
    private static void displayAllSupplierAndPerformancel() {
        List<Supplier> supplierList = new ArrayList<>();
    
        // Read suppliers from the file
        try {
            File supplierFile = new File("supplier.txt"); // Try to read from the root directory
            if (!supplierFile.exists()) {
                // If the file doesn't exist in the root directory, check the resources folder
                supplierFile = new File("Inventory Management System/resources/supplier.txt");
            }
        
            // Now try to read from the determined file
            try (Scanner fileScanner = new Scanner(supplierFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] details = line.split(",");
        
                    if (details.length == 5) {  // Ensure the line contains all expected fields
                        String name = details[0];
                        String contact = details[1];
                        String address = details[2];
                        int rating = Integer.parseInt(details[3]);
                        int deliveries = Integer.parseInt(details[4]);
        
                        // Create a new Supplier object and add it to the list
                        Supplier supplier = new Supplier(name, contact, address);
                        supplier.setRating(rating);
                        supplier.setDeliveries(deliveries);
                        supplierList.add(supplier);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(ColorUtil.RED_BOLD + "Supplier file not found." + ColorUtil.RESET);
                e.printStackTrace();
                return;
            }
        } catch (Exception e) {
            System.out.println(ColorUtil.RED_BOLD + "Error accessing supplier file." + ColorUtil.RESET);
            e.printStackTrace();
        }
    
        // Display all suppliers
        System.out.println("All Suppliers and Performance:");
        System.out.println("===============================================================================================================");
        System.out.printf("%-30s %-15s %-30s %-10s %-15s%n", "Name", "Contact", "Address", "Rating", "Deliveries");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (Supplier supplier : supplierList) {
            System.out.println(supplier);
        }
        System.out.println("===============================================================================================================");

    }
    

    // Delete Supplier
    private static void deleteSupplier() {
        System.out.print("Enter the name of the supplier to delete: ");
        String supplierName = scan.nextLine();
        boolean found = false;
        List<String> supplierList = new ArrayList<>();
    
        try (Scanner fileScanner = new Scanner(new File("Inventory Management System/resources/supplier.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
    
                if (details.length == 5) {
                    String name = details[0];
    
                    if (!name.equalsIgnoreCase(supplierName)) {
                        supplierList.add(line);  // Only keep non-deleted suppliers
                    } else {
                        found = true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier file not found." + ColorUtil.RESET);
            e.printStackTrace();
            return;
        }
    
        if (!found) {
            System.out.println(ColorUtil.RED_BOLD + "Supplier not found." + ColorUtil.RESET);
            return;
        }
    
        try (FileWriter writer = new FileWriter("supplier.txt")) {
            for (String supplier : supplierList) {
                writer.write(supplier + System.lineSeparator());
            }
            System.out.println(ColorUtil.GREEN_BOLD + "Supplier deleted successfully!" + ColorUtil.RESET);
        } catch (IOException e) {
            System.out.println(ColorUtil.RED_BOLD + "An error occurred while writing to the file." + ColorUtil.RESET);
            e.printStackTrace();
        }
    }
    

    public static void loadSupplier() {
        File inventoryFile = new File("Inventory Management System\\resources\\inventory.txt");
        File supplierFile = new File("Inventory Management System\\resources\\supplier.txt");

        if (!inventoryFile.exists()) {
            System.out.println(ColorUtil.RED_BOLD + "File inventory.txt does not exist" + ColorUtil.RESET);
            return;
        }

        // load existing supplier from supplier.txt prevent duplicate
        List<String> existingSuppliers = new ArrayList<>();
        if (supplierFile.exists()) {
            try (Scanner supplierScanner = new Scanner(supplierFile)) {
                while (supplierScanner.hasNextLine()) {
                    String line = supplierScanner.nextLine();
                    String[] columns = line.split(",");
                    if (columns.length > 0) {
                        existingSuppliers.add(columns[0]); // supplier name in first column
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try (Scanner fileScanner = new Scanner(inventoryFile);
                FileWriter supplierWriter = new FileWriter(supplierFile, true)) {

            while (fileScanner.hasNextLine()) {
                String data = fileScanner.nextLine();
                String[] columns = data.split(","); // split the line by ,

                if (columns.length > 5) { // ensure atleast 6 col
                    String supplierName = columns[5];

                    // write to file if the supplier is not repeat
                    if (!existingSuppliers.contains(supplierName)) {
                        String supplierDetails = supplierName + ",N/A,N/A,0,0\n";
                        supplierWriter.write(supplierDetails);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Supplier Class
    public static class Supplier {
        private String name;
        private String contact;
        private String address;
        private int rating; // Rating out of 10 or percentage
        private int deliveries; // Number of successful deliveries

        public Supplier(String name, String contact, String address) {
            this.name = name;
            this.contact = contact;
            this.address = address;
            this.rating = 0;
            this.deliveries = 0;
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

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getDeliveries() {
            return deliveries;
        }

        public void setDeliveries(int deliveries) {
            this.deliveries = deliveries;
        }

        @Override
        public String toString() {
            return String.format("%-30s %-15s %-30s %-10s %-15s",
                    "Name: " + name,
                    "Contact: " + contact,
                    "Address: " + address,
                    "Rating: " + rating,
                    "Deliveries: " + deliveries)
                    ;}

    }
}
