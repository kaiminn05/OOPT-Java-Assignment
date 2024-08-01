import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupplierManagement {
    private static Scanner scan = new Scanner(System.in);

    public static void showSupMenu() {
        while (true) {
            clearScreen();
            System.out.println("Supplier Management");
            System.out.println("======================");
            System.out.println("1. Add Supplier");
            System.out.println("2. Update Supplier Information");
            System.out.println("3. Track Supplier Performance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = getValidChoice(1, 4);
            switch (choice) {
                case 1:
                    addSupplier();
                    sleep(2000);
                    break;
                case 2:
                    updateSupplierInformation();
                    sleep(2000);
                    break;
                case 3:
                    trackSupplierPerformance();
                    sleep(2000);
                    break;
                case 4:
                    System.out.print("Press any key to continue...");
                    scan.nextLine();
                    return;
                default:
                    System.out.println("Invalid Choice. Please Try Again");
                    sleep(2000);
                    break;
            }
        }
    }

    // Supplier Class
    static class Supplier {
        private String name;
        private String address;
        private String contactNum;

        public Supplier(String name, String address, String contactNum) {
            this.name = name;
            this.address = address;
            this.contactNum = contactNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactNum() {
            return contactNum;
        }

        public void setContactNum(String contactNum) {
            this.contactNum = contactNum;
        }

        @Override
        public String toString() {
            return name + "," + address + "," + contactNum;
        }
    }

    // The Supplier Object
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
        System.out.println("Update Supplier Information...");

        List<Supplier> suppliers = readSuppliersFromFile();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available to update.");
            return;
        }

        System.out.println("Select a supplier to update:");
        for (int i = 0; i < suppliers.size(); i++) {
            System.out.println((i + 1) + ". " + suppliers.get(i).getName());
        }
        int choice = getValidChoice(1, suppliers.size()) - 1;
        Supplier supplierToUpdate = suppliers.get(choice);

        System.out.print("Enter new name (or press Enter to keep current: " + supplierToUpdate.getName() + "): ");
        String newName = scan.nextLine();
        if (!newName.isEmpty()) {
            supplierToUpdate.setName(newName);
        }

        System.out.print("Enter new address (or press Enter to keep current: " + supplierToUpdate.getAddress() + "): ");
        String newAddress = scan.nextLine();
        if (!newAddress.isEmpty()) {
            supplierToUpdate.setAddress(newAddress);
        }

        System.out.print("Enter new contact number (or press Enter to keep current: " + supplierToUpdate.getContactNum() + "): ");
        String newContactNum = scan.nextLine();
        if (!newContactNum.isEmpty()) {
            supplierToUpdate.setContactNum(newContactNum);
        }

        writeSuppliersToFile(suppliers);
        System.out.println("Supplier information updated successfully!");
    }

    private static List<Supplier> readSuppliersFromFile() {
        List<Supplier> suppliers = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File("suppliers.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    suppliers.add(new Supplier(parts[0], parts[1], parts[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Supplier file not found.");
        }
        return suppliers;
    }

    private static void writeSuppliersToFile(List<Supplier> suppliers) {
        try (FileWriter writer = new FileWriter("suppliers.txt", false)) {
            for (Supplier supplier : suppliers) {
                writer.write(supplier.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void trackSupplierPerformance() {
        System.out.println("Track Supplier Performance...");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int getValidChoice(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            if (scan.hasNextInt()) {
                choice = scan.nextInt();
                scan.nextLine();
            } else {
                scan.nextLine();
            }
            if (choice < min || choice > max) {
                System.out.print("Invalid choice. Please try again: ");
            }
        }
        return choice;
    }

    public static void main(String[] args) {
        showSupMenu();
    }
}
