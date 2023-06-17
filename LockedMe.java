import java.io.*;
import java.util.*;

public class LockedMe implements Serializable {
    // Root directory where files are stored
    private static final String ROOT_DIRECTORY = "files/";

    // Map to store user credentials (username and password)
    private static Map<String, String> userCredentials = new HashMap<>();

    public static void main(String[] args) {
        // Display the application header
        System.out.println(
                        "===================================\n" +
                        "        LockedMe.com\n" +
                        "   Developer: Mohamad Rafi\n" +
                        "===================================");

        // Load user credentials from the file
        loadUserCredentials();

        // Display registration or login menu
        boolean running = true;
        while (running) {
            displayRegistrationOrLoginMenu();
            int option = getUserOption();

            switch (option) {
                case 1:
                    // Register a new user
                    registerUser();
                    break;
                case 2:
                    // Authenticate the user
                    String username = authenticateUser();
                    if (username != null) {
                        // Display the main menu for the authenticated user
                        displayMainMenu(username);
                    }
                    break;
                case 3:
                    // Exit the application
                    System.out.println("Exiting LockedMe App. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        // Save user credentials to the file
        saveUserCredentials();
    }

    // Loads user credentials from the file
    private static void loadUserCredentials() {
        try (FileInputStream fileInputStream = new FileInputStream("credentials.ser");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            userCredentials = (Map<String, String>) objectInputStream.readObject();
            System.out.println("User credentials loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load user credentials: " + e.getMessage());
        }
    }

    // Saves user credentials to the file
    private static void saveUserCredentials() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("credentials.ser");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(userCredentials);
            System.out.println("User credentials saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save user credentials: " + e.getMessage());
        }
    }

    // Displays the registration or login menu
    private static void displayRegistrationOrLoginMenu() {
        System.out.println("\n-- Registration / Login Menu --");
        System.out.println("1. Register as a new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    // Gets the user's option from the menu
    private static int getUserOption() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        boolean validOption = false;
        while (!validOption) {
            System.out.print("\nEnter your choice: ");
            try {
                option = scanner.nextInt();
                validOption = true;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear the input buffer
                System.out.println("Invalid option. Please enter a number.");
            }
        }
        return option;
    }

    // Registers a new user
    private static void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a username: ");
        String username = scanner.nextLine();

        // Check if the username already exists
        if (userCredentials.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        // Add the user to the credentials map
        userCredentials.put(username, password);
        System.out.println("Registration successful. You can now log in.");
    }

    // Authenticates a user
    private static String authenticateUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Check if the username and password match a user in the credentials map
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return username;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return null;
        }
    }

    // Displays the main menu
    private static void displayMainMenu(String username) {
        boolean running = true;
        while (running) {
            System.out.println("\n-- Main Menu --");
            System.out.println("Welcome, " + username + "!");
            System.out.println("1. Display files in ascending order");
            System.out.println("2. Perform file operations");
            System.out.println("3. Logout");
            int option = getUserOption();

            switch (option) {
                case 1:
                    // Display files in ascending order
                    displayFilesInAscendingOrder();
                    break;
                case 2:
                    // Perform file operations
                    performFileOperations();
                    break;
                case 3:
                    // Logout and return to the previous menu
                    System.out.println("Logged out. Goodbye, " + username + "!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    // Displays files in ascending order
    private static void displayFilesInAscendingOrder() {
        File directory = new File(ROOT_DIRECTORY);
        File[] files = directory.listFiles();

        if (files != null && files.length > 0) {
            // Sort the files in ascending order.
            Arrays.sort(files);

            // Display the files.
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("\nNo files found.");
        }
    }

    // Performs file operations
    private static void performFileOperations() {
        boolean running = true;
        while (running) {
            displayFileOperationsMenu();
            int option = getUserOption();

            switch (option) {
                case 1:
                    // Add a new file
                    addFile();
                    break;
                case 2:
                    // Delete a file
                    deleteFile();
                    break;
                case 3:
                    // Search for a file
                    searchFile();
                    break;
                case 4:
                    // Edit a file
                    editFile();
                    break;
                case 5:
                    // Go back to the main menu
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    // Displays the file operations menu
    private static void displayFileOperationsMenu() {
        System.out.println("\n-- File Operations Menu --");
        System.out.println("1. Add a file");
        System.out.println("2. Delete a file");
        System.out.println("3. Search for a file");
        System.out.println("4. Edit a file");
        System.out.println("5. Go back to the main menu");
    }

    // Adds a new file
    private static void addFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the file name: ");
        String fileName = scanner.nextLine();
        String filePath = ROOT_DIRECTORY + fileName;

        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
                System.out.print("Enter content to add to the file: ");
                String content = scanner.nextLine();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
                System.out.println("Content added to the file.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Failed to create file. " + e.getMessage());
        }
    }

    // Checks if a file exists (case-sensitive)
    private static boolean isFileExists(String fileName) {
        File directory = new File(ROOT_DIRECTORY);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Searches for a file (case-sensitive)
    private static void searchFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the file name to search: ");
        String fileName = scanner.nextLine();

        if (isFileExists(fileName)) {
            File directory = new File(ROOT_DIRECTORY);
            File[] files = directory.listFiles();

            // Sort the files array in ascending order
            Arrays.sort(files);

            // Perform binary search to find the file
            int index = Arrays.binarySearch(files, new File(ROOT_DIRECTORY + fileName));

            if (index >= 0) {
                System.out.println("File found: " + files[index].getAbsolutePath());
            } else {
                System.out.println("File not found.");
            }
        } else {
            System.out.println("File not found.");
        }
    }

    // Deletes a file (case-sensitive)
    private static void deleteFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the file name: ");
        String fileName = scanner.nextLine();

        if (isFileExists(fileName)) {
            String filePath = ROOT_DIRECTORY + fileName;
            File file = new File(filePath);

            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete file.");
            }
        } else {
            System.out.println("File not found.");
        }
    }

    // Edits a file (case-sensitive)
    private static void editFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the file name: ");
        String fileName = scanner.nextLine();

        if (isFileExists(fileName)) {
            String filePath = ROOT_DIRECTORY + fileName;
            File file = new File(filePath);

            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                System.out.println("\nCurrent content of the file:");
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                bufferedReader.close();

                System.out.print("Do you want to add data to the file? (yes/no): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    FileWriter fileWriter = new FileWriter(file, true);
                    System.out.print("Enter content to append to the file: ");
                    String content = scanner.nextLine();
                    fileWriter.write("\n" + content);
                    fileWriter.close();
                    System.out.println("Content appended to the file.");
                } else {
                    System.out.println("No changes made to the file.");
                }
            } catch (IOException e) {
                System.out.println("Failed to edit file. " + e.getMessage());
            }
        } else {
            System.out.println("File not found.");
        }
    }
}
