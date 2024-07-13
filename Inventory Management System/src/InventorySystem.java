import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class InventorySystem {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
            case 3:
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void login() {
        List<User> users = retrieveUserData();
        while (true) {
            System.out.print("Username: ");
            String username = scan.nextLine();
            User currentUser = null;
            for(User user : users ){
                if(user.getUsername().equals(username)){
                    currentUser = user;
                    break;
                }
            }
            if(currentUser == null){
                System.out.println("Username not found. Please try again.");
                continue;
            }
            System.out.print("Password: ");
            String password = scan.nextLine();
            if(currentUser.getPassword().equals(password)){
                System.out.println("Login Successful!");

                manageAccess(currentUser.getRole());
                break;
            }else{
                System.out.println("Invalid password. Please try again.");
            }
        }   
    }

    private static void manageAccess(String role){
        switch(role){
            case "Admin":
                System.out.println("Welcome, Admin!");
                break;
            case "Inventory Manager":
                System.out.println("Welcome, Inventory Manager!");
                break;
            case "Supplier Manager":
                System.out.println("Welcome, Supplier Manager!");
                break;
            case "Stock Clerk":
                System.out.println("Welcome, Stock Clerk!");
                break;
            default:
                System.out.println("Uknown role. Contact admin for access.");
                break;
            
        }
    }

    private static void register() {
        List <User> users = retrieveUserData();
        while(true){
            System.out.print("Enter username: ");
            String username = scan.nextLine();
            boolean usernameExists = false;
            for(User user : users){
                if(user.getUsername().equals(username)){
                    System.out.println("Username \"" + username + "\" already exists.");
                    usernameExists = true;
                    break;
                }
            }

            if(usernameExists){
                continue;
            }
        
            System.out.print("Enter password(at least 6 characters): ");
            String password = scan.nextLine();
            if(password.length() < 6){
                System.out.println("Password must be at least 6 character.");
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
            System.out.print("Your choice -> ");
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

            User newUser = new User(username,password,age,role);
            users.add(newUser);
            boolean success = saveUserData(users);

            if (success) {
                System.out.println("Registration successful! You can now login.");
                showLoginMenu();
                break;
            } else {
                System.out.println("Registration failed. Username may exists.");
            }
        }
    }

    private static List<User> retrieveUserData(){
        List<User> users = new ArrayList<>();
        try{
            File read = new File("user.txt");
            Scanner retrieve = new Scanner(read);
            while(retrieve.hasNextLine()){
                String data = retrieve.nextLine(); 

                String[] userDetails = data.split(",");

                if(userDetails.length >= 4){
                    String username = userDetails[0];
                    String password = userDetails[1];
                    int age = Integer.parseInt(userDetails[2]);
                    String role = userDetails[3];
                    User user = new User(username,password,age,role);
                    users.add(user);
                }
            }
            retrieve.close();
        }catch(FileNotFoundException e){
            System.out.println("An error occured.");
            e.printStackTrace();
        }
        return users;
    }

    private static boolean saveUserData(List<User> users) {
        try (FileWriter writer = new FileWriter("user.txt")) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getAge() + "," + user.getRole() + "\n");
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

        // Constructor for User class
        public User(String username, String password, int age, String role) {
            this.username = username;
            this.password = password;
            this.age = age;
            this.role = role;
        }

        // Getter methods for user attributes
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
