import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("5. Delete Supplier");
            System.out.println("6. Exit");
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
                    deleteSupplier();
                    sleepUtil.sleep(2000);
                    break;
                case 6:
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

    // Add Supplier/Use previous Supplier
    private static void addSupplier() {
        readSuppliersFromFile();

        System.out.print("Enter Supplier Name: ");
        String supplierName = scan.nextLine();

        if (isSupplierExists(supplierName)) {
            System.out.println("Supplier \"" + supplierName + "\" already exists. Reading back from file...");
            readSupplierDetails(supplierName); 
            return;
        }

        System.out.print("Enter Supplier Contact: ");
        String supplierContact = scan.nextLine();
        System.out.print("Enter Supplier Address: ");
        String supplierAddress = scan.nextLine();

        Supplier newSupplier = new Supplier(supplierName, supplierContact, supplierAddress);
        suppliers.add(newSupplier);
        saveSupplierToFile(newSupplier);

        System.out.println("New Supplier \"" + supplierName + "\" added successfully.");
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

    }

    private static void trackSupplierPerformance() {

    }

    private static void addSupplierPerformance() {

    }

    private static void deleteSupplier() {

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
}
