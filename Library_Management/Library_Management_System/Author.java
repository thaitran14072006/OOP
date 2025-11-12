import java.util.ArrayList;
import java.util.regex.Pattern;

public final class Author extends Person {
    private String penName;
    private static int count = 0;
    
    private static final Pattern PEN_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]{2,30}$");
    
    public Author(String name, int year, int month, int dayOfMonth, String penName, String email, String phoneNumber) {
        super(name, year, month, dayOfMonth, phoneNumber, email);
        count++;
        this.id = "A" + String.format("%03d", count);
        setPenName(penName);
    }
    
    public Author(String id, String name, int year, int month, int dayOfMonth, String penName, String email, String phoneNumber) {
        super(id, name, year, month, dayOfMonth, phoneNumber, email);
        setPenName(penName);
    }
    
    private void validatePenName(String penName) {
        if (penName == null || penName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pen name cannot be null or empty");
        }
        if (!PEN_NAME_PATTERN.matcher(penName).matches()) {
            throw new IllegalArgumentException("Pen name must be 2-30 characters long and contain only letters, numbers and spaces");
        }
    }
    
    public void setPenName(String penName) {
        validatePenName(penName);
        this.penName = penName.trim();
    }
    
    public String getPenName() {
        return penName;
    }
    public static int getTotal()
    {
        return count;
    }
    public static void updateCountFromFile(ArrayList<Author> authors) {
        int max = 0;
        for (Author au : authors) {
            try {
                String idNum = au.getId().substring(1);
                int num = Integer.parseInt(idNum);
                if (num > max) max = num;
            } catch (NumberFormatException e) {
                System.out.println("Warning: Invalid author ID format: " + au.getId());
            }
        }
        count = max;
    }
    
    @Override
    public void showInfo() {
        System.out.println("Author ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Birth Date: " + getDate());
        System.out.println("Pen Name: " + getPenName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhoneNumber());
        System.out.println("---");
    }
}