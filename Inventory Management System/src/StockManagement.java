import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockManagement {
    private static Scanner scan = new Scanner(System.in);
    private static List<Stock> stockList = new ArrayList<>();
    

    public static void showStockMenu() {
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("Stock Management");
            System.out.println("======================");
            System.out.println("1. View Stock Levels");
            System.out.println("2. View Stock With Low Levels");
            System.out.println("3. Add New Stock");
            System.out.println("4. Update Stock Levels");
            System.out.println("5. Delete Stock");
            System.out.print("6. Exit >");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    ClearScreenUtil.clearScreen();
                    displayStockItems();
                    scan.nextLine();
                    break;
                case 2:
                    ClearScreenUtil.clearScreen();
                    viewLowStock();
                    break;
                case 3:
                    ClearScreenUtil.clearScreen();                    
                    addStock();
                    break;
                case 4:
                    ClearScreenUtil.clearScreen();                    
                    updateStock();
                    break;
                case 5:
                    ClearScreenUtil.clearScreen();                    
                    deleteStock();
                    break;
                case 6:
                    InventorySystem.showMainMenu();
                default:
                    System.out.println("Invalid Choice. Please Try Again");
                    sleepUtil.sleep(2000);
                    break;
            }
        }
    }

    public static void loadStockFromFile() {
    stockList.clear(); // Clear the existing list

    // First, try to load the file from the root folder
    File rootFile = new File("Inventory Management System/resources/inventory.txt");
    if (rootFile.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rootFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String id = parts[0];
                    String name = parts[1];
                    String desc = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    String unit = parts[4];
                    String supplier = parts[5];
                    int quantity = Integer.parseInt(parts[6]);
                    String expiryDate = parts[7]; // Assuming date is in String format for now

                    stockList.add(new Stock(id, name, desc, price, unit, supplier, quantity, expiryDate));
                }
            }
            System.out.println("Loaded stock from root directory.");
            return; // Exit the method if loaded successfully
        } catch (IOException e) {
            System.out.println("An error occurred while reading the stock file from root directory: " + e.getMessage());
        }
    } else {
        System.out.println("inventory.txt not found in root directory, checking resources...");
    }

    // If not found in root, try to load from the resources folder
    InputStream fileStream = InventoryManagement.class.getClassLoader().getResourceAsStream("Inventory Management System/resources/inventory.txt");
    
    if (fileStream == null) {
        System.out.println("File not found in resources.");
        return;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 8) {
                String id = parts[0];
                String name = parts[1];
                String desc = parts[2];
                double price = Double.parseDouble(parts[3]);
                String unit = parts[4];
                String supplier = parts[5];
                int quantity = Integer.parseInt(parts[6]);
                String expiryDate = parts[7]; // Assuming date is in String format for now

                stockList.add(new Stock(id, name, desc, price, unit, supplier, quantity, expiryDate));
            }
        }
        System.out.println("Loaded stock from resources.");
    } catch (IOException e) {
        System.out.println("An error occurred while reading the stock file from resources: " + e.getMessage());
    }
}

    public static void displayStockItems() {
        loadStockFromFile();
        System.out.println("Stock Items:");
        System.out.println("=================================================================================================================");
        System.out.printf("%-10s %-20s %-15s %-10s %-10s %-15s %-10s %-12s%n", 
                          "Item ID", "Name", "Description", "Price", "Unit", "Supplier", "Quantity", "Expiry Date");
        System.out.println("=================================================================================================================");
        
        for (Stock stock : stockList) {
            System.out.printf("%-10s %-20s %-15s %-10.2f %-10s %-15s %-10d %-12s%n", 
                              stock.getId(), 
                              stock.getName(), 
                              stock.getDesc(), 
                              stock.getPrice(), 
                              stock.getUnit(), 
                              stock.getSupplier(), 
                              stock.getQty(), 
                              stock.getExpiryDate());
        }
        
        System.out.println("=================================================================================================================");
    }
    
    public static void viewLowStock() {
        loadStockFromFile();
        System.out.println("Low Stock Items:");
        System.out.println("=================================================================================================================");
        System.out.printf("%-10s %-20s %-15s %-10s %-10s %-15s %-10s %-12s%n", 
                          "Item ID", "Name", "Description", "Price", "Unit", "Supplier", "Quantity", "Expiry Date");
        System.out.println("=================================================================================================================");
        
        boolean foundLowStock = false;
    
        for (Stock stock : stockList) {
            if (stock.lowStock()) {
                System.out.printf("%-10s %-20s %-15s %-10.2f %-10s %-15s %-10d %-12s%n", 
                                  stock.getId(), 
                                  stock.getName(), 
                                  stock.getDesc(), 
                                  stock.getPrice(), 
                                  stock.getUnit(), 
                                  stock.getSupplier(), 
                                  stock.getQty(), 
                                  stock.getExpiryDate());
                foundLowStock = true;
            }
        }
    
        if (!foundLowStock) {
            System.out.println("No items with low stock levels.");
        }
    
        System.out.println("=================================================================================================================");
        scan.nextLine(); // Assuming this waits for the user to press Enter before proceeding
    }
    
    public static void addStock() {
        loadStockFromFile(); // Load the stock items from the file
        
        if (stockList.isEmpty()) {
            System.out.println("No items found in inventory to update.");
            return;
        }
    
        // Display all the stock items for the user to choose from
        displayStockItems();
        
        System.out.print("Enter the Item ID of the stock you want to add more quantity to: ");
        String id = scan.nextLine();
        
        Stock stockToUpdate = null;
    
        // Find the stock item by ID
        for (Stock stock : stockList) {
            if (stock.getId().equals(id)) {
                stockToUpdate = stock;
                break;
            }
        }
    
        if (stockToUpdate != null) {
            System.out.println("Selected Item: " + stockToUpdate.getName());
            
            // Input for the quantity to add
            int quantityToAdd = 0;
            while (true) {
                System.out.print("Enter the quantity to add: ");
                String input = scan.next();
                try {
                    quantityToAdd = Integer.parseInt(input);
                    if (quantityToAdd <= 0) {
                        throw new Exception("Quantity should be more than 0");
                    }
                    break;
                } catch (Exception ex) {
                    System.out.println("Invalid quantity. Please enter a valid quantity.");
                }
            }
            scan.nextLine(); // Consume newline
            
            // Update the stock quantity
            stockToUpdate.setQty(stockToUpdate.getQty() + quantityToAdd);
            System.out.println("Stock quantity updated successfully.");
            
            // Save the updated stock to the file
            updateStockToFile();
        } else {
            System.out.println("Stock item with ID " + id + " not found.");
        }
        
        sleepUtil.sleep(2000); // Pause execution for 2 seconds
    }
    
    
    public static void updateStock() {
        displayStockItems();
        System.out.print("Enter the Item ID of the stock to update: ");
        String id = scan.nextLine();
        
        Stock stockToUpdate = null;
    
        // Find the stock item by ID
        for (Stock stock : stockList) {
            if (stock.getId().equals(id)) {
                stockToUpdate = stock;
                break;
            }
        }
    
        if (stockToUpdate != null) {
            System.out.println("Updating Stock Details for Item ID: " + id);
    
            // Update Item Name
            while (true) {
                System.out.print("Enter new Item Name (leave blank to keep current): ");
                String name = scan.nextLine();
                if (!name.isEmpty()) {
                    if (name.matches("[a-zA-Z\\s]+")) {
                        stockToUpdate.setName(name);
                        break;
                    } else {
                        System.out.println("Invalid item name. Please enter alphabetic characters only.");
                    }
                } else {
                    break;
                }
            }
    
            // Update Item Description
            while (true) {
                System.out.print("Enter new Item Description (leave blank to keep current): ");
                String desc = scan.nextLine();
                if (!desc.isEmpty()) {
                    stockToUpdate.setDesc(desc);
                }
                break;
            }
    
            // Update Price
            while (true) {
                System.out.print("Enter new Price (enter -1 to keep current): ");
                String input = scan.next();
                try {
                    double price = Double.parseDouble(input);
                    if (price == -1) break;
                    if (price < 0) throw new Exception("Price less than 0");
                    stockToUpdate.setPrice(price);
                    break;
                } catch (Exception ex) {
                    System.out.println("Invalid price. Please enter a valid price.");
                }
            }
    
            scan.nextLine(); // Consume newline
    
            // Update Unit
            while (true) {
                System.out.print("Enter new Unit (leave blank to keep current): ");
                String unit = scan.nextLine();
                if (!unit.isEmpty()) {
                    stockToUpdate.setUnit(unit);
                }
                break;
            }
    
            // Update Supplier
            while (true) {
                System.out.print("Enter new Supplier (leave blank to keep current): ");
                String supplier = scan.nextLine();
                if (!supplier.isEmpty()) {
                    if (supplier.matches("[a-zA-Z\\s]+")) {
                        stockToUpdate.setSupplier(supplier);
                        break;
                    } else {
                        System.out.println("Invalid item name. Please enter alphabetic characters only.");
                    }
                } else {
                    break;
                }
            }
    
            // Update Quantity
            while (true) {
                System.out.print("Enter new Quantity (enter -1 to keep current): ");
                String input = scan.next();
                try {
                    int quantity = Integer.parseInt(input);
                    if (quantity == -1) break;
                    if (quantity < 0) throw new Exception("Quantity less than 0");
                    stockToUpdate.setQty(quantity);
                    break;
                } catch (Exception ex) {
                    System.out.println("Invalid quantity. Please enter a valid quantity.");
                }
            }
    
            scan.nextLine(); // Consume newline
    
            // Update Expiry Date
            while (true) {
                System.out.print("Enter new Expiry Date (DD/MM/YYYY) (leave blank to keep current): ");
                String expiryDate = scan.nextLine();
                if (!expiryDate.isEmpty()) {
                    if (expiryDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        stockToUpdate.setExpiryDate(expiryDate);
                    } else {
                        System.out.println("Invalid date format. Please enter in the format DD/MM/YYYY.");
                        continue;
                    }
                }
                break;
            }
    
            // Save updates to file
            updateStockToFile();
    
        sleepUtil.sleep(2000);
        }
    }
    
    public static void deleteStock() {
        displayStockItems(); // Show current stock items
    
        System.out.print("Enter the Item ID of the stock to delete: ");
        String id = scan.nextLine();
    
        // Find and remove the stock item
        boolean itemFound = false;
        for (int i = 0; i < stockList.size(); i++) {
            if (stockList.get(i).getId().equals(id)) {
                stockList.remove(i);
                itemFound = true;
                break;
            }
        }
    
        if (itemFound) {
            // Define the path where you want to save the updated inventory file (e.g., user's home directory)
            String filePath = "Inventory Management System/resources/inventory.txt";
    
            // Save updated list to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write each stock item from the updated list back to the file
                for (Stock stock : stockList) {
                    writer.write(stock.toFileFormat());
                    writer.newLine();
                }
                System.out.println("Stock item with ID " + id + " deleted successfully and file updated at: " + filePath);
            } catch (IOException e) {
                System.out.println("An error occurred while updating the file: " + e.getMessage());
            }
        } else {
            System.out.println("Stock item with ID " + id + " not found.");
        }
    
        // Pause execution for 2 seconds to allow the user to see the result
        sleepUtil.sleep(2000);
    }
       
    public static String generateNewItemID() {
        int highestNumber = 0;
        
        // Check the existing item IDs to find the highest number
        for (Stock stock : stockList) {
            String currentID = stock.getId();
            if (currentID.startsWith("A")) {
                try {
                    int number = Integer.parseInt(currentID.substring(1));
                    if (number > highestNumber) {
                        highestNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the ID is not properly formatted (e.g., not starting with 'A' or non-numeric)
                }
            }
        }
    
        // Generate the new ID by incrementing the highest found number
        int newNumber = highestNumber + 1;
        return String.format("A%03d", newNumber); // Format the number with leading zeros (e.g., A001, A002)
    }

    public static void saveStockToFile(Stock newStock) {
        // Define the writable path (e.g., a folder named 'output' in the project root)
        String filePath = "Inventory Management System/resources/inventory.txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newStock.toFileFormat());
            writer.newLine();
            System.out.println("Stock added successfully and saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the stock: " + e.getMessage());
        }
    }    
    
    public static void updateStockToFile() {
        // Define the path where you want to save the updated inventory file (e.g., user's home directory)
        String filePath = "Inventory Management System/resources/inventory.txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate over the stockList and write each stock item to the file
            for (Stock stock : stockList) {
                writer.write(stock.toFileFormat());
                writer.newLine();
            }
            System.out.println("Stock updated successfully and saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the stock: " + e.getMessage());
        }
    }
    
    public static class Stock extends InventoryManagement.Item{
       private static int maxQuantity = 100;
    
        // Parameterized constructor
        public Stock(String id, String name, String desc, double price, String unit, String supplier, int quantity, String expiryDate) {
            super(id, name, desc, price, unit, supplier, quantity, expiryDate);
        }
    
        // Getters and Setters
        public int getmaxQuantity(){ return maxQuantity;}
        public void getmaxQuantity(int maxQuantity){ this.maxQuantity = maxQuantity; }

    
        // Methods
        public boolean lowStock() {
            // Consider low stock as less than 10% of a fixed max quantity, which could be configurable if needed
            return getQty() < 0.1 * getmaxQuantity();
        }
    
        // Method to convert Stock object to a file format string
        public String toFileFormat() {
            return getId() + "," + getName() + "," + getDesc() + "," + getPrice() + "," + getUnit() + "," +
                    getSupplier() + "," + getQty() + "," + getExpiryDate();
        }
    
        @Override
        public String toString() {
            return "Stock{ " +
                    "id='" + getId() + '\'' +
                    ", name='" + getName() + '\'' +
                    ", desc='" + getDesc() + '\'' +
                    ", price=" + getPrice() +
                    ", unit='" + getUnit() + '\'' +
                    ", supplier='" + getSupplier() + '\'' +
                    ", quantity=" + getQty() +
                    ", expiryDate='" + getExpiryDate() + '\'' +
                    ", maxQuantity=" + getmaxQuantity() +
                    '}';
        }
    } 

}
