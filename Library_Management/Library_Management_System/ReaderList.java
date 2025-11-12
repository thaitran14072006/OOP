import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class ReaderList implements I_Management<Reader> {
    private final TreeMap<String, Reader> readers = new TreeMap<>();
    
    private void TranstoMap(ArrayList<Reader> load){
        readers.clear();
        for(Reader k: load){
            readers.put(k.getId(), k);
        }
    }
    
    public ReaderList() {
        TranstoMap(FileHandler.loadReaders());
    }
    
    @Override
    public void add(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        
        for (Reader existingReader : readers.values()) {
            if (existingReader.getEmail().equalsIgnoreCase(reader.getEmail())) {
                throw new IllegalArgumentException("Reader with email " + reader.getEmail() + " already exists");
            }
        }
        
        readers.put(reader.getId(), reader);
        System.out.println("Reader added successfully: " + reader.getName());
    }
    
    @Override
    public void remove(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Reader ID cannot be null or empty");
        }
        
        if (readers.containsKey(id)) {
            Reader removedReader = readers.remove(id);
            System.out.println("Reader removed successfully: " + removedReader.getName());
        } else {
            System.out.println("Reader not found!");
        }
    }
    
    @Override
    public Reader search(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Reader ID cannot be null or empty");
        }
        
        if (readers.containsKey(id)) {
            return readers.get(id);
        } else {
            System.out.println("Reader not found!");
            return null;
        }
    }
    
    public ArrayList<Reader> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        ArrayList<Reader> result = new ArrayList<>();
        for (Reader reader : readers.values()) {
            if (reader.getName().equalsIgnoreCase(name)) {
                result.add(reader);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("No readers found with name: " + name);
            return null;
        }
        return result;
    }
    
    @Override
    public void editProfile(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Reader ID cannot be null or empty");
            return;
        }
        
        if (!readers.containsKey(id)) {
            System.out.println("Reader not found!");
            return;
        }
        
        Scanner sc = new Scanner(System.in);
        Reader reader = readers.get(id);
        int choice;
        
        do {
            System.out.println("\n--- Editing Reader: " + reader.getName() + " ---");
            System.out.println("1. Edit name");
            System.out.println("2. Edit birth date");
            System.out.println("3. Edit phone number");
            System.out.println("4. Edit email");
            System.out.println("5. Edit address");
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
                        reader.setName(newName);
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
                        reader.setDate(year, month, day);
                        System.out.println("Birth date updated successfully.");
                    }
                    case 3 -> {
                        System.out.print("Enter new phone number: ");
                        String phone = sc.nextLine();
                        reader.setPhoneNumber(phone);
                        System.out.println("Phone number updated successfully.");
                    }
                    case 4 -> {
                        System.out.print("Enter new email: ");
                        String email = sc.nextLine();
                        reader.setEmail(email);
                        System.out.println("Email updated successfully.");
                    }
                    case 5 -> {
                        System.out.print("Enter new address: ");
                        String address = sc.nextLine();
                        reader.setAddress(address);
                        System.out.println("Address updated successfully.");
                    }
                    case 6 -> reader.showInfo();
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
            throw new IllegalArgumentException("Reader ID cannot be null or empty");
        }
        
        if (readers.containsKey(id)) {
            readers.get(id).showInfo();
        } else {
            System.out.println("Reader not found!");
        }
    }
    
    @Override
    public void showAll() {
        if (readers.isEmpty()) {
            System.out.println("No readers available.");
            return;
        }
        
        System.out.println("\n--- ALL READERS ---");
        for (Reader reader : readers.values()) {
            reader.showInfo();
        }
    }
    
    public int getTotalReaders() {
        return readers.size();
    }
    
    public TreeMap<String, Reader> getReadersMap() {
        return new TreeMap<>(readers);
    }
    @Override
   public void save() {
    String filePath = System.getProperty("user.dir") + "/Library_Management_System/readers.txt";

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
        for (Reader reader : readers.values()) {
            writer.println(reader.getId() + "|" 
                    + reader.getName() + "|" 
                    + reader.getDate() + "|" 
                    + reader.getPhoneNumber() + "|" 
                    + reader.getEmail() + "|" 
                    + reader.getAddress());
        }
        System.out.println("Readers saved successfully to " + filePath);
    } catch (IOException e) {
        System.out.println("Error saving readers: " + e.getMessage());
    }
}


    
}