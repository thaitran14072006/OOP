import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AuthorList implements I_Management<Author> {
    private final Map<String, Author> authors = new TreeMap<>();

    private void TranstoMap(ArrayList<Author> load){
        authors.clear();
        for(Author k: load){
            authors.put(k.getId(), k);
        }
    }
    
    public AuthorList() {
        TranstoMap(FileHandler.loadAuthors());
    }
    
    @Override
    public void add(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        
        for (Author existingAuthor : authors.values()) {
            if (existingAuthor.getEmail().equalsIgnoreCase(author.getEmail())) {
                throw new IllegalArgumentException("Author with email " + author.getEmail() + " already exists");
            }
        }
        
        authors.put(author.getId(), author);
        System.out.println("Author added successfully: " + author.getName());
    }
    
    @Override
    public void remove(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
        
        if (authors.containsKey(id)) {
            Author removedAuthor = authors.remove(id);
            System.out.println("Author removed successfully: " + removedAuthor.getName());
        } else {
            System.out.println("Author not found!");
        }
    }
    
    @Override
    public Author search(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
        
        if (authors.containsKey(id)) {
            return authors.get(id);
        } else {
            System.out.println("Author not found!");
            return null;
        }
    }
    
    public ArrayList<Author> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        ArrayList<Author> result = new ArrayList<>();
        for (Author author : authors.values()) {
            if (author.getName().equalsIgnoreCase(name)) {
                result.add(author);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("No authors found with name: " + name);
            return null;
        }
        return result;
    }
    
    @Override
    public void editProfile(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Author ID cannot be null or empty");
            return;
        }
        
        if (!authors.containsKey(id)) {
            System.out.println("Author not found!");
            return;
        }
        
        Scanner sc = new Scanner(System.in);
        Author author = authors.get(id);
        int choice;
        
        do {
            System.out.println("\n--- Editing Author: " + author.getName() + " ---");
            System.out.println("1. Edit name");
            System.out.println("2. Edit birth date");
            System.out.println("3. Edit phone number");
            System.out.println("4. Edit email");
            System.out.println("5. Edit pen name");
            System.out.println("6. Show current information");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter new name: ");
                        String newName = sc.nextLine();
                        author.setName(newName);
                        System.out.println("Name updated successfully.");
                    }
                    case 2 -> {
                        System.out.println("Enter new birth date:");
                        System.out.print("Year: ");
                        int year = sc.nextInt();
                        System.out.print("Month: ");
                        int month = sc.nextInt();
                        System.out.print("Day: ");
                        int day = sc.nextInt();
                        author.setDate(year, month, day);
                        System.out.println("Birth date updated successfully.");
                    }
                    case 3 -> {
                        System.out.print("Enter new phone number: ");
                        String phone = sc.nextLine();
                        author.setPhoneNumber(phone);
                        System.out.println("Phone number updated successfully.");
                    }
                    case 4 -> {
                        System.out.print("Enter new email: ");
                        String email = sc.nextLine();
                        author.setEmail(email);
                        System.out.println("Email updated successfully.");
                    }
                    case 5 -> {
                        System.out.print("Enter new pen name: ");
                        String penName = sc.nextLine();
                        author.setPenName(penName);
                        System.out.println("Pen name updated successfully.");
                    }
                    case 6 -> author.showInfo();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }
    
    @Override
    public void show(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
        
        if (authors.containsKey(id)) {
            authors.get(id).showInfo();
        } else {
            System.out.println("Author not found!");
        }
    }
    
    @Override
    public void showAll() {
        if (authors.isEmpty()) {
            System.out.println("No authors available.");
            return;
        }
        
        System.out.println("\n--- ALL AUTHORS ---");
        for (Author author : authors.values()) {
            author.showInfo();
        }
    }
    
    public int getTotalAuthors() {
        return authors.size();
    }
    
    public TreeMap<String, Author> getAuthorsMap() {
        return new TreeMap<>(authors);
    }

    public void save() {
    String filePath = System.getProperty("user.dir") + "/Library_Management_System/authors.txt";

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
        for (Author author : authors.values()) {
            writer.println(author.getId() + "|" 
                    + author.getName() + "|" 
                    + author.getDate() + "|" 
                    + author.getPenName() + "|" 
                    + author.getEmail() + "|" 
                    + author.getPhoneNumber());
        }
        System.out.println("Authors saved successfully to " + filePath);
    } catch (IOException e) {
        System.out.println("Error saving authors: " + e.getMessage());
    }
}

}