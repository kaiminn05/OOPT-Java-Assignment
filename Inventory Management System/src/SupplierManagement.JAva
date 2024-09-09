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

    //Add Supplier
    private static void addSupplier() {
        System.out.println("Add Supplier...");
        System.out.print("Enter supplier name: ");
        String name = scan.nextLine();
        System.out.print("Enter supplier address: ");
        String address = scan.nextLine();
        System.out.print("Enter supplier contact number: ");
        String contactNum = scan.nextLine();

        Supplier supplier = new Supplier(name, address, contactNum);

        try (FileWriter writer = new FileWriter("suppliers.txt", true)) {
            writer.write(supplier.toString() + System.lineSeparator());
            System.out.println("Supplier added successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }


    private static void updateSupplierInformation() {
        System.out.print("Enter the name of the supplier you want to update: ");
    String supplierName = scan.nextLine();
    boolean found = false;
    List<Supplier> supplierList = new ArrayList<>();
    try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] details = line.replace("Supplier [", "").replace("]", "").split(", ");
            
            String name = details[0].split("=")[1];
            String contact = details[1].split("=")[1];
            String address = details[2].split("=")[1];

            Supplier supplier = new Supplier(name, contact, address);
            supplierList.add(supplier);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Supplier file not found.");
        e.printStackTrace();
        return;
    }

    // Search for the supplier to update
    for (Supplier supplier : supplierList) {
        if (supplier.getName().equalsIgnoreCase(supplierName)) {
            System.out.println("Supplier found: " + supplier);
            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scan.nextLine();
            if (!newName.trim().isEmpty()) {
                supplier.setName(newName);
            }

            System.out.print("Enter new contact (leave blank to keep current): ");
            String newContact = scan.nextLine();
            if (!newContact.trim().isEmpty()) {
                supplier.setContact(newContact);
            }

            System.out.print("Enter new address (leave blank to keep current): ");
            String newAddress = scan.nextLine();
            if (!newAddress.trim().isEmpty()) {
                supplier.setAddress(newAddress);
            }
            found = true;
            break;
        }
    }
    if (!found) {
        System.out.println("Supplier not found.");
        return;
    }
    try (FileWriter writer = new FileWriter("suppliers.txt")) {
        for (Supplier supplier : supplierList) {
            writer.write(supplier.toString() + System.lineSeparator());
        }
        System.out.println("Supplier information updated successfully!");
    } catch (IOException e) {
        System.out.println("An error occurred while writing to the file.");
        e.printStackTrace();
    }
    }

    
    //Track the supplier performance
    private static void trackSupplierPerformance() {
        System.out.print("Enter the name of the supplier you want to track: ");
    String supplierName = scan.nextLine();
    boolean found = false;

    try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] details = line.replace("Supplier [", "").replace("]", "").split(", ");

            String name = details[0].split("=")[1];
            String contact = details[1].split("=")[1];
            String address = details[2].split("=")[1];
            int rating = Integer.parseInt(details[3].split("=")[1]);
            int deliveries = Integer.parseInt(details[4].split("=")[1]);

            Supplier supplier = new Supplier(name, contact, address);
            supplier.setRating(rating);
            supplier.setDeliveries(deliveries);

            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                found = true;
                System.out.println("Supplier performance: ");
                System.out.println(supplier);
                break;
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Supplier file not found.");
        e.printStackTrace();
    }

    if (!found) {
        System.out.println("Supplier not found.");
    }
        
    }

    //Adding the supplier performance
    private static void addSupplierPerformance() {
        System.out.print("Enter the name of the supplier to update performance: ");
        String supplierName = scan.nextLine();
        boolean found = false;
        List<Supplier> supplierList = new ArrayList<>();
    
        try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.replace("Supplier [", "").replace("]", "").split(", ");
    
                String name = details[0].split("=")[1];
                String contact = details[1].split("=")[1];
                String address = details[2].split("=")[1];
                int rating = Integer.parseInt(details[3].split("=")[1]);
                int deliveries = Integer.parseInt(details[4].split("=")[1]);
    
                Supplier supplier = new Supplier(name, contact, address);
                supplier.setRating(rating);
                supplier.setDeliveries(deliveries);
                supplierList.add(supplier);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Supplier file not found.");
            e.printStackTrace();
            return;
        }
    
        for (Supplier supplier : supplierList) {
            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                System.out.print("Enter new rating (current: " + supplier.getRating() + "): ");
                int newRating = scan.nextInt();
                System.out.print("Enter new deliveries count (current: " + supplier.getDeliveries() + "): ");
                int newDeliveries = scan.nextInt();
                scan.nextLine();
    
                supplier.setRating(newRating);
                supplier.setDeliveries(newDeliveries);
    
                found = true;
                break;
            }
        }
    
        if (!found) {
            System.out.println("Supplier not found.");
            return;
        }
    
        try (FileWriter writer = new FileWriter("suppliers.txt")) {
            for (Supplier supplier : supplierList) {
                writer.write(supplier.toString() + System.lineSeparator());
            }
            System.out.println("Supplier performance updated successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    //Display all the supplier information
    private static void displayAllSupplierAndPerformancel(){
        List<Supplier> supplierList = new ArrayList<>();

    // Read suppliers from the file
    try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] details = line.replace("Supplier [", "").replace("]", "").split(", ");

            String name = details[0].split("=")[1];
            String contact = details[1].split("=")[1];
            String address = details[2].split("=")[1];
            int rating = Integer.parseInt(details[3].split("=")[1]);
            int deliveries = Integer.parseInt(details[4].split("=")[1]);

            // Add each supplier to the list
            Supplier supplier = new Supplier(name, contact, address);
            supplier.setRating(rating);
            supplier.setDeliveries(deliveries);
            supplierList.add(supplier);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Supplier file not found.");
        e.printStackTrace();
        return;
    }

    // Display all suppliers
    System.out.println("All Suppliers and Performance:");
    System.out.println("=============================");
    for (Supplier supplier : supplierList) {
        System.out.println(supplier); // Supplier's toString method will format the output
    }
    }

    //Delete Supplier
    private static void deleteSupplier() { System.out.print("Enter the name of the supplier to delete: ");
    String supplierName = scan.nextLine();
    boolean found = false;
    List<Supplier> supplierList = new ArrayList<>();

    // Read suppliers from the file
    try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] details = line.replace("Supplier [", "").replace("]", "").split(", ");

            String name = details[0].split("=")[1];
            String contact = details[1].split("=")[1];
            String address = details[2].split("=")[1];
            int rating = Integer.parseInt(details[3].split("=")[1]);
            int deliveries = Integer.parseInt(details[4].split("=")[1]);

            Supplier supplier = new Supplier(name, contact, address);
            supplier.setRating(rating);
            supplier.setDeliveries(deliveries);

            // Only add suppliers that are NOT being deleted
            if (!name.equalsIgnoreCase(supplierName)) {
                supplierList.add(supplier);
            } else {
                found = true;
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Supplier file not found.");
        e.printStackTrace();
        return;
    }

    // Check if the supplier was found
    if (!found) {
        System.out.println("Supplier not found.");
        return;
    }

    // Write the updated list of suppliers back to the file
    try (FileWriter writer = new FileWriter("suppliers.txt")) {
        for (Supplier supplier : supplierList) {
            writer.write(supplier.toString() + System.lineSeparator());
        }
        System.out.println("Supplier deleted successfully!");
    } catch (IOException e) {
        System.out.println("An error occurred while writing to the file.");
        e.printStackTrace();
    }
    }

//Supplier Class
    public static class Supplier {
        private String name;
    private String contact;
    private String address;
    private int rating; //Rating out of 10 or percentage
    private int deliveries; //Number of successful deliveries

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
        return "Supplier [Name=" + name + ", Contact=" + contact + ", Address=" + address + 
               ", Rating=" + rating + ", Deliveries=" + deliveries + "]";
    }
    }

    
}
