import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventorySystem {
    private static Scanner scan = new Scanner(System.in);
    private static User loggedInUser;

    public static void main(String[] args) {
        showLoginMenu();
    }
    //display login menu
    public static void showLoginMenu() {
        SupplierManagement.loadSupplier();// load the supplier
        InventoryManagement.loadItemsData();
        ClearScreenUtil.clearScreen();
        System.out.println("Welcome to F&B Inventory Management System");
        System.out.println("==========================================");
        System.out.println("1. Login");
        System.out.println("2. Register");// huh
        System.out.println("3. Exit");
        System.out.print("Enter your choice > ");
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
            case 3:
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    //display main menu
    public static void showMainMenu() {
        while (true) {
            ClearScreenUtil.clearScreen();
            showLoading(); //show loading screen
            sleepUtil.sleep(2000);
            ClearScreenUtil.clearScreen();
            // show the notification
            InventoryManagement.showExpiryNotifications();

            System.out.println("Press Enter to continue to the main menu...");
            scan.nextLine();

            ClearScreenUtil.clearScreen();
            System.out.println("\nF&B Inventory Management System");
            System.out.println("1. Inventory Management");
            System.out.println("2. Stock In & Restock");
            System.out.println("3. Supplier Management");
            System.out.println("4. Logout");
            System.out.print("Enter your choice > "); //let user enter choice
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) { //seperate which module can access by role like "admin,inventory manager,stock clerk and supplier manager"
                case 1:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Inventory Manager")) {
                        System.out.println("Accessing Inventory Management...");
                        sleepUtil.sleep(2000);
                        InventoryManagement.showInvMenu();
                    } else {
                        System.out.println(ColorUtil.RED_BOLD
                                + "Access denied. You do not have permission to access Inventory Management."
                                + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 2:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Stock Clerk")) {
                        System.out.println("Accessing Stock In & Restock...");
                        sleepUtil.sleep(2000);
                        StockManagement.showStockMenu();
                    } else {
                        System.out.println(ColorUtil.RED_BOLD
                                + "Access denied. You do not have permission to access Stock In & Restock."
                                + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 3:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Supplier Manager")) {
                        System.out.println("Accessing Supplier Management...");
                        sleepUtil.sleep(2000);
                        SupplierManagement.showSupMenu();

                    } else {
                        System.out.println(ColorUtil.RED_BOLD
                                + "Access denied. You do not have permission to access Supplier Management."
                                + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 4:
                    loggedInUser = null;
                    System.out.println(ColorUtil.GREEN_BOLD + "Logged out successfully." + ColorUtil.RESET);
                    sleepUtil.sleep(2000);
                    showLoginMenu();
                    return; // return to login menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    //login part
    private static void login() { 
        List<User> users = retrieveUserData();
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("LOGIN");
            System.out.println("===========");
            System.out.print("Username: ");
            String username = scan.nextLine();
            User currentUser = null;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    currentUser = user;
                    break;
                }
            }
            if (currentUser == null) {
                System.out.println(ColorUtil.RED_BOLD + "Username not found. Please try again." + ColorUtil.RESET);
                sleepUtil.sleep(1500);
                ClearScreenUtil.clearScreen();
                continue;
            }
            System.out.print("Password: ");
            String password = scan.nextLine();
            if (currentUser.getPassword().equals(password)) {
                ClearScreenUtil.clearScreen();
                System.out.println(ColorUtil.GREEN_BOLD + "Login Successful!" + ColorUtil.RESET);
                sleepUtil.sleep(1000);
                System.out.println("Logged in as " + ColorUtil.GREEN + username + ColorUtil.RESET);
                loggedInUser = currentUser; // set the logged in user
                sleepUtil.sleep(2000);
                showMainMenu();
                break;
            } else {
                System.out.println(ColorUtil.RED_BOLD + "Invalid password. Please try again." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
            }
        }
    }
    //register part
    private static void register() {
        List<User> users = retrieveUserData();
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("REGISTER");
            System.out.println("===========");
            System.out.print("Enter username: ");
            String username = scan.nextLine();
            boolean usernameExists = false;
            //check whether the username is exist
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    System.out.println(
                            "Username \"" + ColorUtil.CYAN_BOLD + username + ColorUtil.RESET + "\" already exists."); //print error message if exist
                    sleepUtil.sleep(2000);
                    ClearScreenUtil.clearScreen();
                    usernameExists = true;
                    break;
                }
            }

            if (usernameExists) {
                continue;
            }
            //let user enter password
            String password;
            System.out.print("Enter password (6-10 characters): ");
            //validation for password
            while (true) {
                if (scan.hasNextLine()) {
                    password = scan.nextLine();
                    if (password.length() < 6) {
                        System.out.println(
                                ColorUtil.RED_BOLD + "Password must be at least 6 characters." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        System.out.print("Enter password (6-10 characters): ");
                    } else if (password.length() > 10) {
                        System.out.println(
                                ColorUtil.RED_BOLD + "Password must be no more than 10 characters." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        System.out.print("Enter password (6-10 characters): ");
                    } else {
                        break; // Password is valid, break the loop
                    }
                }
            }
            //let user enter age
            int age;
            System.out.print("Enter your age: ");
            //validation for age
            while (true) {
                if (scan.hasNextInt()) {
                    age = scan.nextInt();
                    if (age >= 18 && age <= 100) {
                        break;
                    } else {
                        System.out.println(
                                ColorUtil.RED_BOLD + "Age Invalid. Age must be between 18 - 100" + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                        ClearScreenUtil.clearPreviousLine();
                        ClearScreenUtil.clearPreviousLine();
                        System.out.print("Enter your age: ");
                    }
                } else {
                    System.out.println(
                            ColorUtil.RED_BOLD + "Invalid input. Please enter a valid number." + ColorUtil.RESET);
                    scan.next();
                    sleepUtil.sleep(2000);
                    ClearScreenUtil.clearPreviousLine();
                    ClearScreenUtil.clearPreviousLine();
                    System.out.print("Enter your age: ");
                }
                scan.nextLine();
            }
            //let user to choose role
            System.out.println("Choose your role: ");
            System.out.println("1. Admin");
            System.out.println("2. Inventory Manager");
            System.out.println("3. Stock Clerk");
            System.out.println("4. Supplier Manager");
            System.out.print("Your choice > ");
            int choice = scan.nextInt();
            scan.nextLine();
            String role = "";
            switch (choice) {
                case 1:
                    role = "Admin";
                    break;
                case 2:
                    role = "Inventory Manager";
                    break;
                case 3:
                    role = "Stock Clerk";
                    break;
                case 4:
                    role = "Supplier Manager";
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return;
            }

            User newUser = new User(username, password, age, role);
            users.add(newUser);
            boolean success = saveUserData(users);

            if (success) {
                System.out.println(
                        ColorUtil.GREEN_BOLD + "Registration successful! You can now login." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
                showLoginMenu();
                break;
            } else {
                System.out.println(ColorUtil.RED_BOLD + "Registration failed. Username may exists." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
            }
        }
    }
    //method for retrieving user data
    private static List<User> retrieveUserData() {
        List<User> users = new ArrayList<>();
        try {
            File read = new File("Inventory Management System/resources/user.txt");
            Scanner retrieve = new Scanner(read);
            while (retrieve.hasNextLine()) {
                String data = retrieve.nextLine();

                String[] userDetails = data.split(",");

                if (userDetails.length >= 4) {
                    String username = userDetails[0];
                    String password = userDetails[1];
                    int age = Integer.parseInt(userDetails[2]);
                    String role = userDetails[3];
                    User user = new User(username, password, age, role);
                    users.add(user);
                }
            }
            retrieve.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
        return users;
    }
    //method for saving user data
    private static boolean saveUserData(List<User> users) {
        try (FileWriter writer = new FileWriter("Inventory Management System/resources/user.txt")) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getAge() + "," + user.getRole()
                        + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user data.");
            e.printStackTrace();
            return false;
        }
    }
    //method for showing loading screen
    private static void showLoading() {
        ClearScreenUtil.clearScreen();
        System.out.print(ColorUtil.YELLOW_BOLD + "Loading Data" + ColorUtil.RESET);
        for (int i = 0; i < 5; i++) { // print the dot
            System.out.print(ColorUtil.YELLOW_BOLD + "." + ColorUtil.RESET);
            sleepUtil.sleep(1000);
        }
    }

    private static class User {
        private String username;
        private String password;
        private int age;
        private String role;

        // constrctur for user
        public User(String username, String password, int age, String role) {
            this.username = username;
            this.password = password;
            this.age = age;
            this.role = role;
        }

        // getter & setter
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public int getAge() {
            return age;
        }

        public String getRole() {
            return role;
        }
    }
}
