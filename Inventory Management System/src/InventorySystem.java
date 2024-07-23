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

    public static void showLoginMenu() {
        ClearScreenUtil.clearScreen();
        System.out.println("Welcome to F&B Inventory Management System");
        System.out.println("==========================================");
        System.out.println("1. Login");
        System.out.println("2. Register");
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

    public static void showMainMenu() {
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("\nF&B Inventory Management System");
            System.out.println("1. Inventory Management");
            System.out.println("2. Stock In & Restock");
            System.out.println("3. Supplier Management");
            System.out.println("4. Check Expiry and Stock Alerts");
            System.out.println("5. Logout");
            System.out.print("Enter your choice > ");
            int choice = scan.nextInt();
            scan.nextLine(); 

            switch (choice) {
                case 1:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Inventory Manager")) {
                        System.out.println("Accessing Inventory Management...");
                        sleepUtil.sleep(2000);
                        InventoryManagement.showInvMenu();
                    } else {
                        System.out.println(ColorUtil.RED_BOLD + "Access denied. You do not have permission to access Inventory Management." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 2:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Stock Clerk")) {
                        System.out.println("Accessing Stock In & Restock...");
                        sleepUtil.sleep(2000);
                    } else {
                        System.out.println(ColorUtil.RED_BOLD + "Access denied. You do not have permission to access Stock In & Restock." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 3:
                    if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Supplier Manager")) {
                        System.out.println("Accessing Supplier Management...");
                        sleepUtil.sleep(2000);
                        SupplierManagement.showSupMenu();
                    } else {
                        System.out.println(ColorUtil.RED_BOLD + "Access denied. You do not have permission to access Supplier Management." + ColorUtil.RESET);
                        sleepUtil.sleep(2000);
                    }
                    break;
                case 4:
                    System.out.println("Checking Expiry and Stock Alerts...");
                    sleepUtil.sleep(2000);
                    break;
                case 5:
                    loggedInUser = null;
                    System.out.println(ColorUtil.GREEN_BOLD + "Logged out successfully." + ColorUtil.RESET);
                    sleepUtil.sleep(2000);
                    showLoginMenu();
                    return; //return to login menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

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
                System.out.println("Username not found. Please try again.");
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


    private static void register() {
        List<User> users = retrieveUserData();
        while (true) {
            ClearScreenUtil.clearScreen(); 
            System.out.println("REGISTER");
            System.out.println("===========");
            System.out.print("Enter username: ");
            String username = scan.nextLine();
            boolean usernameExists = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    System.out.println("Username \"" + ColorUtil.CYAN_BOLD + username + ColorUtil.RESET + "\" already exists.");
                    sleepUtil.sleep(2000);
                    ClearScreenUtil.clearScreen();
                    usernameExists = true;
                    break;
                }
            }

            if (usernameExists) {
                continue;
            }

            System.out.print("Enter password(at least 6 characters): ");
            String password = scan.nextLine();
            if (password.length() < 6) {
                System.out.println("Password must be at least 6 character.");
                sleepUtil.sleep(2000);
                continue;
            }
            System.out.print("Enter you age: ");
            int age = scan.nextInt();
            scan.nextLine();
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
                System.out.println(ColorUtil.GREEN_BOLD + "Registration successful! You can now login." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
                showLoginMenu();
                break;
            } else {
                System.out.println(ColorUtil.RED_BOLD + "Registration failed. Username may exists." + ColorUtil.RESET);
                sleepUtil.sleep(2000);
            }
        }
    }

    private static List<User> retrieveUserData() {
        List<User> users = new ArrayList<>();
        try {
            File read = new File("user.txt");
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

    private static boolean saveUserData(List<User> users) {
        try (FileWriter writer = new FileWriter("user.txt")) {
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

    private static class User {
        private String username;
        private String password;
        private int age;
        private String role;

        //constrctur for user
        public User(String username, String password, int age, String role) {
            this.username = username;
            this.password = password;
            this.age = age;
            this.role = role;
        }
        //get
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
