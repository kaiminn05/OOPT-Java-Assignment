import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManagement {
    private static Scanner scan = new Scanner(System.in);
    private static List<Item> items = new ArrayList<>();

    public static void showInvMenu() {

        while (true) {
            loadItemsData();
            ClearScreenUtil.clearScreen();
            System.out.println("Inventory Management");
            System.out.println("======================");
            System.out.println("1. View Inventory");
            System.out.println("2. Add New Inventory Items");
            System.out.println("3. Update Inventory Items");
            System.out.println("4. Delete Inventory Items");
            System.out.println("5. Search");
            System.out.println("6. Set Expiry Date");
            System.out.println("7. Check Expiry Alerts");
            System.out.println("8. Exit");
            System.out.print("Enter your choice >");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    ClearScreenUtil.clearScreen();
                    System.out.println("CURRENT INVENTORY ITEMS");
                    displayInventory();
                    System.out.print("Press any key to continue....");
                    scan.nextLine();
                    break;
                case 2:
                    addItem();
                    sleepUtil.sleep(2000);
                    break;
                case 3:
                    updateInventory();
                    sleepUtil.sleep(2000);
                    break;
                case 4:
                    System.out.println("Delete Inventory Items...");
                    sleepUtil.sleep(2000);
                    break;
                case 5:
                    System.out.println("Search...");
                    sleepUtil.sleep(2000);
                    break;
                case 6:
                    System.out.println("Set Expiry Dates");
                    sleepUtil.sleep(2000);
                    break;
                case 7:
                    System.out.println("Check Expiry Dates");
                    sleepUtil.sleep(2000);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid Choice. Please Try Again");
                    sleepUtil.sleep(2000);
                    break;
            }
        }
    }

    private static void addItem() {
        ClearScreenUtil.clearScreen();
        System.out.println("ADD NEW INVENTORY ITEMS");
        System.out.println("=========================");
        System.out.print("Enter item name: ");
        String name = scan.nextLine();
        System.out.print("Enter item description: ");
        String desc = scan.nextLine();
        System.out.print("Enter item price: ");
        double price = scan.nextDouble();
        scan.nextLine();
        System.out.print("Enter item unit: ");
        String unit = scan.nextLine();
        System.out.print("Enter item supplier: ");
        String supplier = scan.nextLine();
        System.out.print("Enter item quantity: ");
        int quantity = scan.nextInt();
        scan.nextLine();

        String id = "A" + String.format("%03d", items.size() + 1);// generate unique ID for each items
        Item newItem = new Item(id, name, desc, price, unit, supplier, quantity);
        items.add(newItem);
        boolean success = saveItemData(newItem);

        if (success) {
            System.out.println(ColorUtil.GREEN_BOLD + "Item added successfully!" + ColorUtil.RESET);
            sleepUtil.sleep(2000);
        } else {
            System.out.println(ColorUtil.RED_BOLD + "Failed to add item." + ColorUtil.RESET);
            sleepUtil.sleep(2000);
        }
    }

    private static void displayInventory() {
        System.out.println(
                "========================================================================================================");
        System.out.printf("%-10s %-20s %-20s %-10s %-10s %-20s %-10s%n",
                "ID", "Name", "Description", "Price", "Unit", "Supplier", "Quantity");
        System.out.println(
                "========================================================================================================");

        for (Item item : items) {
            System.out.printf("%-10s %-20s %-20s %-10.2f %-10s %-20s %-10d%n",
                    item.getId(), item.getName(), item.getDesc(), item.getPrice(),
                    item.getUnit(), item.getSupplier(), item.getQty());
        }
        System.out.println(
                "========================================================================================================");
    }

    private static void updateInventory() {
        ClearScreenUtil.clearScreen();
        System.out.println("UPDATE INVENTORY ITEMS");
        System.out.println("=======================");
        displayInventory();
        System.out.print("Enter item ID to choose > ");
        String itemId = scan.nextLine();

        Item itemToUpdate = null;
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate == null) {
            System.out.println("Item ID not found.");
            return;
        }

        System.out.println("Selected Item: " + itemToUpdate.getName());
        System.out.println("1. Update Name");
        System.out.println("2. Update Description");
        System.out.println("3. Update Price");
        System.out.println("4. Update Unit");
        System.out.println("5. Update Supplier");
        System.out.println("6. Update Quantity");
        System.out.print("Enter field number to update > ");
        int updateChoice = scan.nextInt();
        scan.nextLine(); 

        switch (updateChoice) {
            case 1:
                System.out.print("Enter new name: ");
                itemToUpdate.setName(scan.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                itemToUpdate.setDesc(scan.nextLine());
                break;
            case 3:
                System.out.print("Enter new price: ");
                itemToUpdate.setPrice(scan.nextDouble());
                scan.nextLine(); // Consume newline
                break;
            case 4:
                System.out.print("Enter new unit: ");
                itemToUpdate.setUnit(scan.nextLine());
                break;
            case 5:
                System.out.print("Enter new supplier: ");
                itemToUpdate.setSupplier(scan.nextLine());
                break;
            case 6:
                System.out.print("Enter new quantity: ");
                itemToUpdate.setQty(scan.nextInt());
                scan.nextLine(); 
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        saveUpdatedData(); //save all data after update
        System.out.println(ColorUtil.GREEN_BOLD + "Item updated successfully." + ColorUtil.RESET);
    }

    private static boolean saveItemData(Item item) {
        try (FileWriter writer = new FileWriter(
                "C:\\Users\\Acer\\OneDrive\\Desktop\\OOPT Java Assignment\\Inventory Management System\\resources\\inventory.txt",
                true)) { // write in append mode so that exist items
            // wont replace new item
            writer.write(item.getId() + "," + item.getName() + "," + item.getDesc() + "," + item.getPrice() + ","
                    + item.getUnit() + "," + item.getSupplier() + "," + item.getQty() + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving item data.");
            e.printStackTrace();
            return false;
        }
    }
    private static boolean saveUpdatedData() {
        try (FileWriter writer = new FileWriter(
                "C:\\Users\\Acer\\OneDrive\\Desktop\\OOPT Java Assignment\\Inventory Management System\\resources\\inventory.txt")) {
            for (Item item : items) {
                writer.write(item.getId() + "," + item.getName() + "," + item.getDesc() + "," + item.getPrice() + ","
                        + item.getUnit() + "," + item.getSupplier() + "," + item.getQty() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving item data.");
            e.printStackTrace();
            return false;
        }
    }
    private static void loadItemsData() {
        items.clear(); // clear existing list
        File file = new File(
                "C:\\Users\\Acer\\OneDrive\\Desktop\\OOPT Java Assignment\\Inventory Management System\\resources\\inventory.txt");

        if (!file.exists()) {
            System.out.println("The file inventory.txt does not exist.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String data = fileScanner.nextLine();

                String[] itemDetails = data.split(",");
                if (itemDetails.length == 7) {
                    String id = itemDetails[0];
                    String name = itemDetails[1];
                    String desc = itemDetails[2];
                    double price = Double.parseDouble(itemDetails[3]);
                    String unit = itemDetails[4];
                    String supplier = itemDetails[5];
                    int quantity = Integer.parseInt(itemDetails[6]);

                    Item item = new Item(id, name, desc, price, unit, supplier, quantity);
                    items.add(item);

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while loading items data.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    private static class Item {
        private String id;
        private String name;
        private String desc;
        private double price;
        private String unit;
        private String supplier;
        private int quantity;

        public Item(String id, String name, String desc, double price, String unit, String supplier, int quantity) {
            this.id = id;
            this.name = name;
            this.desc = desc;
            this.price = price;
            this.unit = unit;
            this.supplier = supplier;
            this.quantity = quantity;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public double getPrice() {
            return price;
        }

        public String getUnit() {
            return unit;
        }

        public String getSupplier() {
            return supplier;
        }

        public int getQty() {
            return quantity;
        }  

        public void setName(String name) {
            this.name = name;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public void setQty(int quantity) {
            this.quantity = quantity;
        }
    }
}
