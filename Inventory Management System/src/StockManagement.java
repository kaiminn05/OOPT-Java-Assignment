import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            System.out.println("6. Track Batch Number");
            System.out.print("7. Exit >");
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
                    System.out.println("Tracking...");
                    sleepUtil.sleep(2000);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid Choice. Please Try Again");
                    sleepUtil.sleep(2000);
                    break;
            }
        }
    }

    public static void loadStockFromFile() {
        stockList.clear(); // Clear the existing list
        try (BufferedReader reader = new BufferedReader(new FileReader("stock.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String itemID = parts[0];
                    String itemName = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double unitPrice = Double.parseDouble(parts[3]);
                    String category = parts[4];
                    int maxQuantity = Integer.parseInt(parts[5]);
                    stockList.add(new Stock(itemID, itemName, quantity, unitPrice, category, maxQuantity));
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the stock file: " + e.getMessage());
        }
    }

    public static void displayStockItems() {
        loadStockFromFile();
        System.out.println("Stock Items:");
        System.out.println("=====================================================================");
        System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s%n", 
                          "Item ID", "Item Name", "Quantity", "Unit Price", "Category", "Max Quantity");
        System.out.println("=====================================================================");
        
        for (Stock stock : stockList) {
            System.out.printf("%-10s %-20s %-10d %-10.2f %-15s %-10d%n", 
                              stock.getItemID(), 
                              stock.getItemName(), 
                              stock.getQuantity(), 
                              stock.getUnitPrice(), 
                              stock.getCategory(), 
                              stock.getMaxQuantity());
        }
        
        System.out.println("=====================================================================");
    }

    public static void viewLowStock() {
        loadStockFromFile();
        System.out.println("Low Stock Items:");
        System.out.println("=====================================================================");
        System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s%n", 
                          "Item ID", "Item Name", "Quantity", "Unit Price", "Category", "Max Quantity");
        System.out.println("=====================================================================");
        
        boolean foundLowStock = false;

        for (Stock stock : stockList) {
            if (stock.lowStock()) {
                System.out.printf("%-10s %-20s %-10d %-10.2f %-15s %-10d%n", 
                                  stock.getItemID(), 
                                  stock.getItemName(), 
                                  stock.getQuantity(), 
                                  stock.getUnitPrice(), 
                                  stock.getCategory(), 
                                  stock.getMaxQuantity());
                foundLowStock = true;
            }
        }

        if (!foundLowStock) {
            System.out.println("No items with low stock levels.");
        }

        System.out.println("=====================================================================");
    }

    public static void addStock() {
        System.out.println("Add New Stock");
        System.out.println("==============");

        // Collect stock details from user
        System.out.print("Enter Item ID: ");
        String itemID = scan.nextLine();

        // Check if an item with the same ID already exists
        for (Stock stock : stockList) {
            if (stock.getItemID().equals(itemID)) {
                System.out.println("An item with ID " + itemID + " already exists. Please enter a unique Item ID.");
                sleepUtil.sleep(2000);
                return; // Exit the method without adding the stock
            }
        }

        System.out.print("Enter Item Name: ");
        String itemName = scan.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scan.nextInt();
        System.out.print("Enter Unit Price: ");
        double unitPrice = scan.nextDouble();
        scan.nextLine(); // Consume newline
        System.out.print("Enter Category: ");
        String category = scan.nextLine();
        System.out.print("Enter Max Quantity: ");
        int maxQuantity = scan.nextInt();

        // Create a new Stock object
        Stock newStock = new Stock(itemID, itemName, quantity, unitPrice, category, maxQuantity);

        // Add new stock to the list
        stockList.add(newStock);

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("stock.txt", true))) {
            writer.write(newStock.toFileFormat());
            writer.newLine();
            System.out.println("Stock added successfully and saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the stock: " + e.getMessage());
        }

        sleepUtil.sleep(2000);
    }

    public static void updateStock() {
        displayStockItems();
        System.out.print("Enter the Item ID of the stock to update: ");
        String itemID = scan.nextLine();
        
        Stock stockToUpdate = null;

        // Find the stock item by ID
        for (Stock stock : stockList) {
            if (stock.getItemID().equals(itemID)) {
                stockToUpdate = stock;
                break;
            }
        }

        if (stockToUpdate != null) {
            System.out.println("Updating Stock Details for Item ID: " + itemID);
            
            System.out.print("Enter new Item Name (leave blank to keep current): ");
            String itemName = scan.nextLine();
            if (!itemName.isEmpty()) {
                stockToUpdate.setItemName(itemName);
            }

            System.out.print("Enter new Quantity (enter -1 to keep current): ");
            int quantity = scan.nextInt();
            if (quantity != -1) {
                stockToUpdate.setQuantity(quantity);
            }

            System.out.print("Enter new Unit Price (enter -1 to keep current): ");
            double unitPrice = scan.nextDouble();
            if (unitPrice != -1) {
                stockToUpdate.setUnitPrice(unitPrice);
            }
            scan.nextLine(); // Consume newline

            System.out.print("Enter new Category (leave blank to keep current): ");
            String category = scan.nextLine();
            if (!category.isEmpty()) {
                stockToUpdate.setCategory(category);
            }

            System.out.print("Enter new Max Quantity (enter -1 to keep current): ");
            int maxQuantity = scan.nextInt();
            if (maxQuantity != -1) {
                stockToUpdate.setMaxQuantity(maxQuantity);
            }

            // Save updates to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("stock.txt"))) {
                for (Stock stock : stockList) {
                    writer.write(stock.toFileFormat());
                    writer.newLine();
                }
                System.out.println("Stock updated successfully and saved to file.");
            } catch (IOException e) {
                System.out.println("An error occurred while saving the stock: " + e.getMessage());
            }

        } else {
            System.out.println("Stock item with ID " + itemID + " not found.");
        }

        sleepUtil.sleep(2000);
    }

    public static void deleteStock() {
        displayStockItems(); // Show current stock items
    
        System.out.print("Enter the Item ID of the stock to delete: ");
        String itemID = scan.nextLine();
    
        // Find and remove the stock item
        boolean itemFound = false;
        for (int i = 0; i < stockList.size(); i++) {
            if (stockList.get(i).getItemID().equals(itemID)) {
                stockList.remove(i);
                itemFound = true;
                break;
            }
        }
    
        if (itemFound) {
            // Save updated list to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("stock.txt"))) {
                for (Stock stock : stockList) {
                    writer.write(stock.toFileFormat());
                    writer.newLine();
                }
                System.out.println("Stock item with ID " + itemID + " deleted successfully and file updated.");
            } catch (IOException e) {
                System.out.println("An error occurred while updating the file: " + e.getMessage());
            }
        } else {
            System.out.println("Stock item with ID " + itemID + " not found.");
        }
    
        sleepUtil.sleep(2000);
    }
    
    public static class Stock {
        private String itemID;
        private String itemName;
        private int quantity;
        private double unitPrice;
        private String category;
        private int maxQuantity;
    
        // Default constructor
        public Stock() {
            this.itemID = "";
            this.itemName = "";
            this.quantity = 0;
            this.unitPrice = 0.0;
            this.category = "";
            this.maxQuantity = 100; // Default maximum quantity
        }
    
        // Parameterized constructor
        public Stock(String itemID, String itemName, int quantity, double unitPrice, String category, int maxQuantity) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.category = category;
            this.maxQuantity = maxQuantity;
        }
    
        // Getters and Setters
        public String getItemID() { return itemID; }
        public void setItemID(String itemID) { this.itemID = itemID; }
    
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
    
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    
        public int getMaxQuantity() { return maxQuantity; }
        public void setMaxQuantity(int maxQuantity) { this.maxQuantity = maxQuantity; }
    
        // Methods   
        public boolean lowStock() {
            return this.quantity < 0.1 * this.maxQuantity;
        }

        // Method to convert Stock object to a file format string
        public String toFileFormat() {
            return itemID + "," + itemName + "," + quantity + "," + unitPrice + "," + category + "," + maxQuantity;
        }

        @Override
        public String toString() {
            return 
                "Stock{ " +
                "itemID ='" + itemID + '\'' +
                ", itemName ='" + itemName + '\'' +
                ", quantity =" + quantity +
                ", unitPrice =" + unitPrice +
                ", category ='" + category + '\'' +
                ", maxQuantity =" + maxQuantity +
                '}';
        }

        
    }

}
