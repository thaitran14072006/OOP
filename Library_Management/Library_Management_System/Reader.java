import java.time.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Reader extends Person {
    private String address;
    private static int count = 0;
    
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,-]{5,100}$");
    
    public Reader(String name, int year, int month, int dayOfMonth, String phoneNumber, String email, String address) {
        super(name, year, month, dayOfMonth, phoneNumber, email);
        setAddress(address);
        count++;
        this.id = "R" + String.format("%03d", count);
    }
    
    public Reader(String id, String name, int year, int month, int dayOfMonth, String phoneNumber, String email, String address) {
        super(id, name, year, month, dayOfMonth, phoneNumber, email);
        setAddress(address);
    }
    
    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        if (!ADDRESS_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Address must be 5-100 characters long and contain only letters, numbers, spaces, and basic punctuation");
        }
    }
    
    public void setAddress(String address) {
        validateAddress(address);
        this.address = address.trim();
    }
    
    public String getAddress() {
        return address;
    }
    
    @Override
    protected void validateDate(int year, int month, int dayOfMonth) {
        super.validateDate(year, month, dayOfMonth);
        
        LocalDate birthDate = LocalDate.of(year, month, dayOfMonth);
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthDate, now);
        
        if (age.getYears() < 12) {
            throw new IllegalArgumentException("Reader must be at least 12 years old to use library services");
        }
    }
    
    public static void updateCountFromFile(ArrayList<Reader> readers) {
        int max = 0;
        for (Reader r : readers) {
            try {
                String idNum = r.getId().substring(1);
                int num = Integer.parseInt(idNum);
                if (num > max) max = num;
            } catch (NumberFormatException e) {
                System.out.println("Warning: Invalid reader ID format: " + r.getId());
            }
        }
        count = max;
    }
    
    @Override
    public void showInfo() {
        System.out.println("Reader ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Birth Date: " + getDate());
        System.out.println("Phone: " + getPhoneNumber());
        System.out.println("Email: " + getEmail());
        System.out.println("Address: " + getAddress());
        System.out.println("---");
    }
}