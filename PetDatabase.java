// Carl Fredrickson
// CSC422 - Assignment 1
// Professor James Tucker II
// July 6, 2024

/*
 * This is a database program for managing information (name and age) about pets. 
 * The database allows the user to add pet information to the database, remove pet information, 
 * update pet information, and search for pets by name or by age.
 */

/* RELEASE 1
* Allow adding and displaying of pets.
*/

/* RELEASE 2 (coming soon)
* Allow searching of pets.
*/

/* RELEASE 3 (coming soon)
* Allow for updating and removing pets.
*/


import java.util.Scanner;

public class PetDatabase {

    // This class represents a pet. The information stored includes the pet's name and age.
    public static class Pet {
        private String name;
        private int age;

        public Pet(String thisName, int thisAge) {
            name = thisName;
            age = thisAge;
        }

        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public int getAge() {
            return age;
        }
    
        public void setAge(int age) {
            this.age = age;
        }
    }


    // Global variables for the program.
    static Pet[] pets = new Pet[100];
    static int petCount = 0;
    static Scanner s;


    // This main method uses a loop and a switch statement to allow the user to choose what they want to do.
    // Other methods carry out the actions chosen by the user. 
    public static void main(String[] args) throws InvalidArgumentException {
        printL("Pet Database Program");
        printL("********************");
        
        // Keep asking the user what to do until they choose to exit the program.
        int userChoice = -1;
        while (userChoice != 9) {
            // Find out what the user wants to do.
            userChoice = getUserChoice();

            // Take action based on the user's choice
            switch (userChoice) {
                case 1:
                    showAllPets();
                    break;

                case 2:
                    addPets();
                    break;

                case 3:
                    searchPetsByName();
                    break;

                case 4:
                    searchPetsByAge();
                    break;
                    
                // DEV TEST OPTION - adds pets to database to facilitate testing
                case 9:
                    populateSampleData();
                    break;

                default:
                    printL("Goodbye");
                    break;
            }
            
            // Reset the user choice if the user didn't choose to exit the program.
            if (userChoice != 0) {
                userChoice = -1;
            }
        }
    }

    // This method shows the user the main menu and returns the selected option.
    private static int getUserChoice() {
        // Show the menu
        printL("");
        printL("What would you like to do?");
        printL("1) View all pets");
        printL("2) Add new pets");
        printL("3) Search pets by name");
        printL("4) Search pets by age");
        //printL("9) *** Populate sample pet data (Used for testing)");
        printL("0) Exit program");
        
        // Get user input
        int userChoice = -1;
        s = new Scanner(System.in);
        while ((userChoice < 0 || userChoice > 4) && userChoice != 9) {
            print("Your choice: ");
            userChoice = s.nextInt();
            
            if ((userChoice < 0 || userChoice > 4) && userChoice != 9) {
                printL("*** INVALID CHOICE ***");
            }
        }
        
        return userChoice;
    }

    // This method displays a list of all pets in the pets array.
    private static void showAllPets() {
        printTableHeader();
        for (int i = 0; i < petCount; i++) {
            printTableRow(i, pets[i].getName(), pets[i].getAge());
        }
        printTableFooter(petCount);
    }

    // This method allows the user to add 1 or more pets to the database.
    private static void addPets() throws InvalidArgumentException {
        s = new Scanner(System.in);
        String line = "";
        
        printL("");
        printL("Enter name and age of pets to add. Enter 'done' when finished.");
        
        while (!line.equals("done")) {
            // Get the name and age of a new pet
            print("Add pet (name age): ");
            line = s.nextLine();
            
            // Exit the while statement if 'done' was entered
            if (!line.equals("done")) {
                String[] petInfo = parseArgument(line);
                addPet(petInfo[0], Integer.parseInt(petInfo[1]));
            }
        }
    }

    // This method parses the line that was entered and returns an array of 2 strings containing the name and age of the pet.
    // If the specified line does not contain 2 values, an InvalidArgumentException is thrown.
    private static String[] parseArgument(String line) throws InvalidArgumentException {
        String[] petInfo = line.split(" ");
        if (petInfo.length != 2) {
            throw new InvalidArgumentException("*** INVALID ARGUMENT EXCEPTION - '" + line + "' is not a valid input. The input must contain the pet's name and age, separated by a space.");
        }
        return petInfo;
    }

    // This method creates a new Pet object and adds it to the pets array.
    private static void addPet(String name, int age) {
        pets[petCount] = new Pet(name, age);
        petCount++;
    }

    private static void searchPetsByName() {
        // Get search information from the user.
        s = new Scanner(System.in);
        printL("");
        print("Enter search phrase: ");
        String searchPhrase = s.nextLine();

        // Create a new array with the results of the search.
        int resultCount = 0;
        Pet[] searchResults = new Pet[100];
        for (int i = 0; i < petCount; i++) {
            if (pets[i].getName().contains(searchPhrase)) {
                searchResults[resultCount] = new Pet(pets[i].getName(), pets[i].getAge());
                resultCount++;
            }
        }

        // Display the results.
        printL("Found these pets with '" + searchPhrase + "' in the name:");
        showSearchResults(searchResults, resultCount);
    }

    private static void searchPetsByAge() {
        // Get search information from the user.
        s = new Scanner(System.in);
        printL("");
        print("Enter search age: ");
        int searchAge = s.nextInt();

        // Create a new array with the results of the search.
        int resultCount = 0;
        Pet[] searchResults = new Pet[100];
        for (int i = 0; i < petCount; i++) {
            if (pets[i].getAge() == searchAge) {
                searchResults[resultCount] = new Pet(pets[i].getName(), pets[i].getAge());
                resultCount++;
            }
        }

        // Display the results.
        printL("Found these pets with age of " + searchAge + ":");
        showSearchResults(searchResults, resultCount);
    }

    // This method displays a list of all pets in the pets array.
    private static void showSearchResults(Pet[] results, int resultCount) {
        printTableHeader();
        for (int i = 0; i < resultCount; i++) {
            printTableRow(i, results[i].getName(), results[i].getAge());
        }
        printTableFooter(resultCount);
    }


    // EXCEPTION CLASSES

    // This custom exception is thrown when the user enters an invalid input for a pet's name and age.
    public static class InvalidArgumentException extends Exception {
        public InvalidArgumentException(String message) {
            super(message);
        }
    }


    // UTILITY METHODS

    // This method shortens the print statement.
    public static void print(String content) {
        System.out.print(content);
    }
    
    // This method shortens the print line statement.
    public static void printL(String content) {
        System.out.println(content);
    }

    // This method creates the header for displaying pet information.
    private static void printTableHeader() {
        printL("");
        printL("+-------------------------+");
        printL("| ID  | NAME       | AGE  |");
        printL("+-------------------------+");
    }
    
    // This method creates one row of pet information.
    private static void printTableRow(int id, String name, int age) {
        print("| ");
        System.out.printf("%3s", id);
        print(" | ");
        System.out.printf("%-10s", name);
        print(" | ");
        System.out.printf("%4s", age);
        print(" |\n");
    }
    
    // This method creates the footer for displaying pet information.
    private static void printTableFooter(int rowCount) {
        printL("+-------------------------+");
        printL("" + rowCount + " row(s) in set.");
    }

    // This is used to make testing and debugging easier.
    public static void populateSampleData() {
        addPet("Toby", 7);
        addPet("Norman", 1);
        addPet("Rover", 6);
        addPet("Princess", 7);
        addPet("Raja", 4);
        addPet("Lumen", 5);
        addPet("Jake", 9);
        printL("Added sample pets to database.");
    }
}