import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public abstract class Person {
    protected String id;
    protected String name;
    protected LocalDate date;
    protected String phoneNumber;
    protected String Email;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    
    public Person(String name, int year, int month, int dayOfMonth, String phoneNumber, String email) {
        setName(name);
        setDate(year, month, dayOfMonth);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }
    
    public Person(String id, String name, int year, int month, int dayOfMonth, String phoneNumber, String email) {
        this.id = id;
        setName(name);
        setDate(year, month, dayOfMonth);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }
    
    protected void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must be 2-50 characters long and contain only letters and spaces");
        }
    }
    
    protected void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    protected void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw new IllegalArgumentException("Phone number must be 10-11 digits");
        }
    }
    
    protected void validateDate(int year, int month, int dayOfMonth) {
        try {
            LocalDate date = LocalDate.of(year, month, dayOfMonth);
            LocalDate now = LocalDate.now();
            
            if (date.isAfter(now)) {
                throw new IllegalArgumentException("Birth date cannot be in the future");
            }
            
            Period age = Period.between(date, now);
            if (age.getYears() < 12) {
                throw new IllegalArgumentException("Person must be at least 12 years old");
            }
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date: " + e.getMessage());
        }
    }
    
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }
    
    public void setDate(int year, int month, int dayOfMonth) {
        validateDate(year, month, dayOfMonth);
        this.date = LocalDate.of(year, month, dayOfMonth);
    }
    
    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
    }
    
    public void setEmail(String email) {
        validateEmail(email);
        this.Email = email.toLowerCase().trim();
    }
    
    public abstract void showInfo();
    
    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return Email; }
}