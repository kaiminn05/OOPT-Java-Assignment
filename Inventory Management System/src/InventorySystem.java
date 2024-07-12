import java.util.Scanner;

public class InventorySystem {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        showLoginMenu();
    }

    public static void showLoginMenu() {
        System.out.println("Welcome to F&B Inventory Management System");
        System.out.println("==========================================");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        int choice = scan.nextInt();
        scan.nextLine();
        System.out.println();
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void login() {
        while (true) {
            System.out.println("Username: ");
            String username = scan.nextLine();
            System.out.println("Password: ");
            String password = scan.nextLine();
        }
    }

    private static void register() {
        System.out.println("Enter username: ");
        String username = scan.nextLine();
        System.out.println("Enter password: ");
        String password = scan.nextLine();
        System.out.println("Choose your role: ");
        System.out.println("1. Inventory Manager");
        System.out.println("2. Stock Clerk");
        System.out.println("3. Supplier Manager");
        int choice = scan.nextInt();
        scan.nextLine();
        String role;
        switch (choice) {
            case 1:
                role = "Inventory Manager";
                break;
            case 2:
                role = "Stock Clerk";
                break;
            case 3:
                role = "Supplier Manager";
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        boolean success;

        if (success) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username may exists.");
        }

    }
}
