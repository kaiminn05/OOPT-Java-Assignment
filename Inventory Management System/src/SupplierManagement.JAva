import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupplierManagement {
    private static Scanner scan = new Scanner(System.in);
    private static List<Item> items = new ArrayList<>();

    public static void showSupMenu(String[] args) {
        loadItemsData();
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
        private static void addSupplier() {
            System.out.println("Add Supplier...");
        }
    
        private static void updateSupplierInformation() {
            System.out.println("Update Supplier Information...");
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
    }
