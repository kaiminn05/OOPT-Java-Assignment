import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
                    deleteInventoryItem();
                    sleepUtil.sleep(2000);
                    break;
                case 5:
                    searchInventory();
                    sleepUtil.sleep(2000);
                    break;
                case 6:
                    setExpiryDate();
                    sleepUtil.sleep(2000);
                    break;
                case 7:
                    checkExpiryAlerts();
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
        while (name.length() > 20 || name.isEmpty()) {
            if (name.isEmpty()) {
                System.out.println(ColorUtil.RED_BOLD + "The name field cannot be empty." + ColorUtil.RESET);
                sleepUtil.sleep(1500);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
            } else {
                System.out.println(ColorUtil.RED_BOLD + "The maximum name for item is 20 character" + ColorUtil.RESET);
                sleepUtil.sleep(2000);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
            }
            System.out.print("Enter item name: ");
            name = scan.nextLine();
        }
        System.out.print("Enter item description: ");
        String desc = scan.nextLine();
        while (desc.length() > 20 || desc.isEmpty()) {
            if (desc.isEmpty()) {
                desc = "N/A";
            } else {
                System.out.println("The max item description is 20 characters.");
                sleepUtil.sleep(2000);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
                System.out.print("Enter Description: ");
                desc = scan.nextLine();
            }
        }

        double price;
        System.out.print("Enter item price: ");
        while (true) {
            if (scan.hasNextDouble()) {
                price = scan.nextDouble();
                if (price < 0) {
                    System.out.println(ColorUtil.RED + "The item price should be positive." + ColorUtil.RESET);
                    sleepUtil.sleep(1500);
                    ClearScreenUtil.clearPreviousLine();
                    ClearScreenUtil.clearPreviousLine();
                    System.out.print("Enter item price: ");
                } else {
                    break;
                }
            } else {
                System.out
                        .println(ColorUtil.RED_BOLD + "Invalid input. Please enter a valid number." + ColorUtil.RESET);
                sleepUtil.sleep(1500);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
                scan.next();
                System.out.print("Enter item price: ");
            }
        }

        scan.nextLine();
        System.out.print("Enter item unit: ");
        String unit = scan.nextLine();
        while (unit.length() > 10 || unit.isEmpty()) {
            if (unit.isEmpty()) {
                System.out.println(ColorUtil.RED_BOLD + "Item unit field cannot be empty." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
            } else {
                System.out.print(ColorUtil.RED_BOLD + "The maximum character for item unit is 10." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
                ClearScreenUtil.clearPreviousLine();
                ClearScreenUtil.clearPreviousLine();
            }
            System.out.print("Enter item unit: ");
            unit = scan.nextLine();
        }

        String supplier = chooseOrAddSupplier();

        String id = "A" + String.format("%03d", items.size() + 1);// generate unique ID for each items
        int quantity = 0;
        String expiryDate = "N/A";
        Item newItem = new Item(id, name, desc, price, unit, supplier, quantity, expiryDate);
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

    private static void displayInventory() { // display inventory item(can be use for other partt)
        System.out.println(
                "======================================================================================================================================");
        System.out.printf("%-10s %-20s %-20s %-10s %-10s %-35s %-10s %-15s%n",
                "ID", "Name", "Description", "Price", "Unit", "Supplier", "Quantity", "Expiry Date");
        System.out.println(
                "=====================================================================================================================================");

        for (Item item : items) {
            String expiryDate = item.getExpiryDate();
            // check if the expiry date is close to current date
            if (isCloseToExpiry(expiryDate)) {
                // if close then color will become red
                System.out.printf("%-10s %-20s %-20s %-10.2f %-10s %-35s %-10d %-10s%n",
                        item.getId(), item.getName(), item.getDesc(), item.getPrice(),
                        item.getUnit(), item.getSupplier(), item.getQty(),
                        ColorUtil.RED_BOLD + expiryDate + ColorUtil.RESET);
            } else {
                // else just print like normal(with green color)
                System.out.printf("%-10s %-20s %-20s %-10.2f %-10s %-35s %-10d %-10s%n",
                        item.getId(), item.getName(), item.getDesc(), item.getPrice(),
                        item.getUnit(), item.getSupplier(), item.getQty(),
                        ColorUtil.GREEN_BOLD + expiryDate + ColorUtil.RESET);
            }
        }

        System.out.println(
                "=====================================================================================================================================");
    }

    private static void updateInventory() { // update inventory function
        ClearScreenUtil.clearScreen();
        System.out.println("UPDATE INVENTORY ITEMS");
        System.out.println("=======================");
        displayInventory();
        System.out.print("Enter item ID to choose > A");
        String itemId = scan.nextLine();

        Item itemToUpdate = null;
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase("A" + itemId)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate == null) {
            System.out.println("Item ID not found.");
            return;
        }
        ClearScreenUtil.clearScreen();
        System.out.println("Selected Item: " + ColorUtil.YELLOW_BOLD + itemToUpdate.getName() + ColorUtil.RESET);
        System.out.println("1. Update Name");
        System.out.println("2. Update Description");
        System.out.println("3. Update Price");
        System.out.println("4. Update Unit");
        System.out.println("5. Update Supplier");
        System.out.println("6. Update Quantity");
        System.out.print("Enter field number to update > ");
        int updateChoice = scan.nextInt();
        scan.nextLine();

        while (updateChoice < 1 || updateChoice > 6) {
            System.out.println(ColorUtil.RED_BOLD + "Try again. Please choose a valid option." + ColorUtil.RESET);
            sleepUtil.sleep(2000);
            ClearScreenUtil.clearPreviousLine();
            ClearScreenUtil.clearPreviousLine();
            System.out.print("Enter field number to update > ");
            updateChoice = scan.nextInt();
            scan.nextLine();
        }
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new name: ");
                String newname = scan.nextLine();
                while(newname.length() > 20 || newname.isEmpty() || newname.equals(itemToUpdate.getName())){
                    if(newname.isEmpty()){
                        System.out.println(ColorUtil.RED_BOLD + "Name cannot be empty." + ColorUtil.RESET);
                    }else if(newname.equals(itemToUpdate.getName())){
                        System.out.println(ColorUtil.RED_BOLD + "Same name cannot be updated." + ColorUtil.RESET);
                    }else{
                        System.out.println(ColorUtil.RED_BOLD + "Maximum character for name is 20." +  ColorUtil.RESET);
                    }
                    sleepUtil.sleep(2000);
                    ClearScreenUtil.clearPreviousLine();
                    ClearScreenUtil.clearPreviousLine();
                    System.out.print("Enter new name: ");
                    newname = scan.nextLine();
                }
                itemToUpdate.setName(newname);
                break;
            case 2:
                System.out.print("Enter new description: ");
                String desc = scan.nextLine();
                while (desc.length() > 20 || desc.isEmpty()) {
                    if (desc.isEmpty()) {
                        System.out.println(ColorUtil.RED_BOLD + "New description cannot be empty." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        System.out.print("Enter new description: ");
                        desc = scan.nextLine();
                    } else {
                        System.out.println("The max item description is 20 characters.");
                        sleepUtil.sleep(2000);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        System.out.print("Enter new description: ");
                        desc = scan.nextLine();
                    }
                }
                itemToUpdate.setDesc(desc);
                break;
            case 3:
                double price;
                System.out.print("Enter new price: ");
                while (true) {
                    if (scan.hasNextDouble()) {
                        price = scan.nextDouble();
                        if (price < 0) {
                            System.out.println(ColorUtil.RED + "The item price should be positive." + ColorUtil.RESET);
                            sleepUtil.sleep(1500);
                            ClearScreenUtil.clearPreviousLine();
                            ClearScreenUtil.clearPreviousLine();
                            System.out.print("Enter new price: ");
                        } else {
                            break;
                        }
                    } else {
                        System.out
                                .println(ColorUtil.RED_BOLD + "Invalid input. Please enter a valid number." + ColorUtil.RESET);
                        sleepUtil.sleep(1500);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        scan.next();
                        System.out.print("Enter new price: ");
                    }
                }
                scan.nextLine();
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

        saveUpdatedData(); // save all data after update
        System.out.println(ColorUtil.GREEN_BOLD + "Item updated successfully." + ColorUtil.RESET);
    }

    private static void deleteInventoryItem() {
        ClearScreenUtil.clearScreen();
        System.out.println("DELETE INVENTORY ITEMS");
        System.out.println("=======================");
        displayInventory();
        System.out.print("Enter item ID to delete > ");
        String itemId = scan.nextLine();

        Item itemToDelete = null;
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                itemToDelete = item;
                break;
            }
        }

        if (itemToDelete == null) {
            System.out.println(ColorUtil.RED_BOLD + "Item ID not found." + ColorUtil.RESET);
            return;
        }

        items.remove(itemToDelete); // remove item from the list
        saveUpdatedData(); // save updated data

        System.out.println(ColorUtil.GREEN_BOLD + "Item deleted successfully." + ColorUtil.RESET);
        sleepUtil.sleep(2000);
    }

    private static void searchInventory() {
        ClearScreenUtil.clearScreen();
        System.out.println("SEARCH INVENTORY");
        System.out.println("=======================");
        System.out.print("Enter keyword to search (name, description, or supplier): ");
        String keyword = scan.nextLine().toLowerCase();

        List<Item> matchingItems = new ArrayList<>();

        // find through items based on the keyword
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(keyword) ||
                    item.getDesc().toLowerCase().contains(keyword) ||
                    item.getSupplier().toLowerCase().contains(keyword)) {
                matchingItems.add(item);
            }
        }

        // display search result
        if (matchingItems.isEmpty()) {
            System.out.println(ColorUtil.RED_BOLD + "No items found matching the search criteria." + ColorUtil.RESET);
        } else {
            System.out.println("Matching Inventory Items:");
            System.out.println(
                    "========================================================================================================");
            System.out.printf("%-10s %-20s %-20s %-10s %-10s %-20s %-10s%n",
                    "ID", "Name", "Description", "Price", "Unit", "Supplier", "Quantity");
            System.out.println(
                    "========================================================================================================");

            for (Item item : matchingItems) {
                System.out.printf("%-10s %-20s %-20s %-10.2f %-10s %-20s %-10d%n",
                        item.getId(), item.getName(), item.getDesc(), item.getPrice(),
                        item.getUnit(), item.getSupplier(), item.getQty());
            }
            System.out.println(
                    "========================================================================================================");
        }

        System.out.print("Press any key to continue....");
        scan.nextLine();
    }

    private static String chooseOrAddSupplier() {
        List<String> suppliers = loadSuppliersFromFile();
    
        System.out.println("1. Choose from previous suppliers");
        System.out.println("2. Add a new supplier");
    
        int option;
        while (true) {
            System.out.print("Enter your choice (1 or 2): ");
            if (scan.hasNextInt()) {
                option = scan.nextInt();
                scan.nextLine();  
                if (option == 1 || option == 2) {
                    break;
                } else {
                    System.out.println("Invalid input. Please choose either 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                scan.next();  
            }
        }
    
        if (option == 1) {  // choose from previous suppliers
            if (suppliers.isEmpty()) {
                System.out.println("No existing suppliers found.");
            } else {
                System.out.println("Choose a supplier from the list:");
                for (int i = 0; i < suppliers.size(); i++) {
                    System.out.println((i + 1) + ". " + suppliers.get(i));
                }
            }
    
            int choice;
            while (true) {
                System.out.print("Enter your choice: ");
                if (scan.hasNextInt()) {
                    choice = scan.nextInt();
                    scan.nextLine();  
                    if (choice > 0 && choice <= suppliers.size()) {
                        return suppliers.get(choice - 1);
                    } else {
                        System.out.println("Invalid input. Please choose a valid supplier number.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scan.next();  
                }
            }
        } else if (option == 2) {  // option for add new supplier
            System.out.print("Enter new supplier name: ");
            String newSupplier = scan.nextLine();
            appendSupplierToFile(newSupplier);
            return newSupplier;
        }
    
        return null;  
    }
    
    private static void setExpiryDate() {
        ClearScreenUtil.clearScreen();
        System.out.println("SET EXPIRY DATE");
        System.out.println("=======================");
        displayInventory();
        System.out.print("Enter item ID to set expiry date > ");
        String itemId = scan.nextLine();

        Item itemToSetExpiry = null;
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) { // ignore the upper lowerr case
                itemToSetExpiry = item;
                break;
            }
        }

        if (itemToSetExpiry == null) {
            System.out.println(ColorUtil.RED_BOLD + "Item ID not found." + ColorUtil.RESET);
            return;
        }

        String expiryDate;
        while (true) {
            System.out.print("Enter expiry date (dd/MM/yyyy): ");
            expiryDate = scan.nextLine();

            // validate date
            if (isValidExpiryDate(expiryDate)) {
                break;
            } else {
                System.out.println(ColorUtil.RED_BOLD
                        + "Invalid expiry date format or date in the past. Please try again." + ColorUtil.RESET);
            }
        }

        itemToSetExpiry.setExpiryDate(expiryDate);
        saveUpdatedData();

        System.out.println(ColorUtil.GREEN_BOLD + "Expiry date set successfully." + ColorUtil.RESET);
        sleepUtil.sleep(2000);
    }

    private static void checkExpiryAlerts() {
        ClearScreenUtil.clearScreen();
        System.out.println("CHECK EXPIRY ALERTS");
        System.out.println(
                "============================================================================================");
        System.out.printf("%-10s %-20s %-20s %-15s %-15s%n", "ID", "Name", "Expiry Date", "Days Remaining", "Alert");

        for (Item item : items) {
            String expiryDate = item.getExpiryDate();
            long daysRemaining = calculateDaysUntilExpiry(expiryDate);

            if (daysRemaining < 0) { // if date already passed
                System.out.printf("%-10s %-20s %-20s %-15s %s%n", item.getId(), item.getName(), expiryDate, "Expired",
                        ColorUtil.RED_BOLD + "Expired" + ColorUtil.RESET);
            } else if (isNearExpiry(expiryDate)) {
                // near expired(30dayas)
                System.out.printf("%-10s %-20s %-20s %s%-15d%s %s%n",
                        item.getId(), item.getName(), expiryDate,
                        ColorUtil.RED_BOLD, daysRemaining, ColorUtil.RESET,
                        ColorUtil.RED_BOLD + "Near Expiry" + ColorUtil.RESET);
            } else {
                // not expiered
                System.out.printf("%-10s %-20s %-20s %s%-15d%s %s%n",
                        item.getId(), item.getName(), expiryDate,
                        ColorUtil.GREEN_BOLD, daysRemaining, ColorUtil.RESET,
                        ColorUtil.GREEN_BOLD + "Safe" + ColorUtil.RESET);
            }
        }

        System.out.println(
                "============================================================================================");
        System.out.print("Press any key to continue....");
        scan.nextLine();
    }

    public static void showExpiryNotifications() {
        System.out.println("========== Expiry Date Alerts ==========");
        boolean hasAlerts = false;

        for (Item item : items) {
            String expiryDate = item.getExpiryDate();
            long daysRemaining = calculateDaysUntilExpiry(expiryDate);

            if (isNearExpiry(expiryDate)) {
                System.out.printf("%sWARNING: %s%s is close to expiry! Expiry Date: %s (%d days remaining)%s%n",
                        ColorUtil.RED_BOLD, item.getName(), ColorUtil.RESET, expiryDate, daysRemaining,
                        ColorUtil.RESET);
                hasAlerts = true;
            }
        }

        if (!hasAlerts) {
            System.out.println("No items are close to expiry.");
        }

        System.out.println("========================================");
    }

    private static boolean isValidExpiryDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            Date expiry = dateFormat.parse(date);
            Date today = new Date(); // get today date
            if (expiry.before(today)) {
                return false; // if expiry dat eis before today then return false
            }
            return true;
        } catch (ParseException e) {
            return false; // return false if date format wrong
        }
    }

    private static boolean isCloseToExpiry(String expiryDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date expiryDate = dateFormat.parse(expiryDateStr);
            Date today = new Date();

            // calc the diff
            long diffInMillies = Math.abs(expiryDate.getTime() - today.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // if difference below or equal to 30 day then return true
            return diffInDays <= 30 && expiryDate.after(today);
        } catch (ParseException e) {
            return false;
        }
    }

    private static long calculateDaysUntilExpiry(String expiryDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // make sure the format is correct
        try {
            Date expiryDate = dateFormat.parse(expiryDateStr);
            Date today = new Date(); // get today date
            long diffInMillies = expiryDate.getTime() - today.getTime(); // time differences

            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            
            return -1;
        }
    }

    private static boolean isNearExpiry(String expiryDateStr) {
        long daysRemaining = calculateDaysUntilExpiry(expiryDateStr);
        return daysRemaining > 0 && daysRemaining <= 30; // consider as near expiry if within 30 days
    }

    private static boolean saveItemData(Item item) { // this if use for add items with append TRUE so that data wont
                                                     // replace previous data
        try (FileWriter writer = new FileWriter(
                "Inventory Management System/resources/inventory.txt",
                true)) { // write in append mode so that exist items
            // wont replace new item
            writer.write(item.getId() + "," + item.getName() + "," + item.getDesc() + "," + item.getPrice() + ","
                    + item.getUnit() + "," + item.getSupplier() + "," + item.getQty() + "," + item.getExpiryDate()
                    + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving item data.");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean saveUpdatedData() { // this is use for update items information without append TRUE
        try (FileWriter writer = new FileWriter(
                "Inventory Management System/resources/inventory.txt")) {
            for (Item item : items) {
                writer.write(item.getId() + "," + item.getName() + "," + item.getDesc() + "," + item.getPrice() + ","
                        + item.getUnit() + "," + item.getSupplier() + "," + item.getQty() + "," + item.getExpiryDate()
                        + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving item data.");
            e.printStackTrace();
            return false;
        }
    }

    private static List<String> loadSuppliersFromFile() {
        List<String> suppliers = new ArrayList<>();
        File supplierFile = new File("Inventory Management System/resources/supplier.txt");
        if (supplierFile.exists()) {
            try (Scanner fileScanner = new Scanner(supplierFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] details = line.split(",");
                    if (details.length > 0) {
                        suppliers.add(details[0]);  // first col for supplier name
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return suppliers;
    }

    private static void appendSupplierToFile(String supplier) {
        try (FileWriter writer = new FileWriter("Inventory Management System/resources/supplier.txt", true)) {
            writer.write(supplier + ",N/A,N/A,0,0\n"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadItemsData() {
        items.clear(); // clear existing list
        File file = new File(
                "Inventory Management System/resources/inventory.txt");

        if (!file.exists()) {
            System.out.println("The file inventory.txt does not exist.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String data = fileScanner.nextLine();

                String[] itemDetails = data.split(",");
                if (itemDetails.length == 8) {
                    String id = itemDetails[0];
                    String name = itemDetails[1];
                    String desc = itemDetails[2];
                    double price = Double.parseDouble(itemDetails[3]);
                    String unit = itemDetails[4];
                    String supplier = itemDetails[5];
                    int quantity = Integer.parseInt(itemDetails[6]);
                    String expiryDate = itemDetails[7];

                    Item item = new Item(id, name, desc, price, unit, supplier, quantity, expiryDate);
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
        private String expiryDate;

        public Item(String id, String name, String desc, double price, String unit, String supplier, int quantity,
                String expiryDate) {
            this.id = id;
            this.name = name;
            this.desc = desc;
            this.price = price;
            this.unit = unit;
            this.supplier = supplier;
            this.quantity = quantity;
            this.expiryDate = expiryDate;
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

        public String getExpiryDate() {
            return expiryDate;
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

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }
    }
}
