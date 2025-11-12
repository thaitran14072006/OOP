import java.util.ArrayList;

public class BookCopy {
    private String copyId;
    private Book book;
    private boolean isAvailable;
    private static int totalCopies = 0;
    
    private static final int MAX_COPIES_PER_BOOK = 50;
    
    public BookCopy(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        this.book = book;
        this.isAvailable = true;
        totalCopies++;
        
        if (getCopyCountForBook(book) > MAX_COPIES_PER_BOOK) {
            totalCopies--;
            throw new IllegalStateException("Cannot create more than " + MAX_COPIES_PER_BOOK + " copies for a single book");
        }
        
        this.copyId = book.getId() + "-" + String.format("%03d", totalCopies);
    }
    
    public BookCopy(String copyId, Book book, boolean isAvailable) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (copyId == null || copyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Copy ID cannot be null or empty");
        }
        
        this.copyId = copyId;
        this.book = book;
        this.isAvailable = isAvailable;
    }
    
    private int getCopyCountForBook(Book targetBook) {
        return 0;
    }
    
    public String getCopyId() { return copyId; }
    public Book getBook() { return book; }
    public boolean isAvailable() { return isAvailable; }
    public static int getTotalCopies() { return totalCopies; }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public static void updateCountFromFile(ArrayList<BookCopy> copies) {
        int max = 0;
        for (BookCopy copy : copies) {
            try {
                String copyNumStr = copy.getCopyId().split("-")[1];
                int num = Integer.parseInt(copyNumStr);
                if (num > max) max = num;
            } catch (Exception e) {
                System.out.println("Warning: Invalid book copy ID format: " + copy.getCopyId());
            }
        }
        totalCopies = max;
    }
    
    public void showInfo() {
        System.out.println("Copy ID: " + getCopyId());
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("---");
    }
}