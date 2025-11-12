import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowSlip {
    private String borrowId;
    private ArrayList<String> bookCopyIds;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private Reader reader;
    private static int count = 0;
    
    private static final int MAX_BOOKS_PER_READER = 5;
    private static final int BORROW_PERIOD_DAYS = 14;
    
    public BorrowSlip(int year, int month, int dayOfMonth, Reader reader) {
        validateReader(reader);
        validateBorrowDate(year, month, dayOfMonth);
        
        count++;
        this.borrowId = "Bor" + String.format("%03d", count);
        this.bookCopyIds = new ArrayList<>();
        this.borrowDate = LocalDate.of(year, month, dayOfMonth);
        this.dueDate = this.borrowDate.plusDays(BORROW_PERIOD_DAYS);
        this.reader = reader;
    }
    
    public BorrowSlip(String borrowId, int year, int month, int dayOfMonth, Reader reader) {
        validateReader(reader);
        validateBorrowDate(year, month, dayOfMonth);
        
        this.borrowId = borrowId;
        this.bookCopyIds = new ArrayList<>();
        this.borrowDate = LocalDate.of(year, month, dayOfMonth);
        this.dueDate = this.borrowDate.plusDays(BORROW_PERIOD_DAYS);
        this.reader = reader;
    }

    public BorrowSlip() {
    }
    
    private void validateReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
    }
    
    private void validateBorrowDate(int year, int month, int dayOfMonth) {
        try {
            LocalDate borrowDate = LocalDate.of(year, month, dayOfMonth);
            LocalDate today = LocalDate.now();
            
            if (borrowDate.isAfter(today)) {
                throw new IllegalArgumentException("Borrow date cannot be in the future");
            }
            
            if (borrowDate.isBefore(today.minusYears(1))) {
                throw new IllegalArgumentException("Borrow date cannot be more than 1 year in the past");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid borrow date: " + e.getMessage());
        }
    }
    
    private void validateBookLimit() {
        if (bookCopyIds.size() >= MAX_BOOKS_PER_READER) {
            throw new IllegalStateException("Reader cannot borrow more than " + MAX_BOOKS_PER_READER + " books");
        }
    }
    
    public boolean addBookCopy(BookList bookList, String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        validateBookLimit();
        
        ArrayList<BookCopy> availableCopies = bookList.getAvailableCopies(bookId);
        if (!availableCopies.isEmpty()) {
            BookCopy copy = availableCopies.get(0);
            copy.setAvailable(false);
            bookCopyIds.add(copy.getCopyId());
            System.out.println("Book copy added to borrow slip: " + copy.getCopyId());
            return true;
        } else {
            System.out.println("No available copies for book: " + bookId);
            return false;
        }
    }
    
    public boolean addBookCopyById(BookList bookList, String copyId) {
        if (copyId == null || copyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Copy ID cannot be null or empty");
        }
        
        validateBookLimit();
        
        BookCopy copy = bookList.searchCopy(copyId);
        if (copy != null && copy.isAvailable()) {
            copy.setAvailable(false);
            bookCopyIds.add(copyId);
            System.out.println("Book copy added to borrow slip: " + copyId);
            return true;
        } else {
            System.out.println("Book copy not available or not found: " + copyId);
            return false;
        }
    }
    
    public boolean returnBookCopy(BookList bookList, String copyId) {
        if (copyId == null || copyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Copy ID cannot be null or empty");
        }
        
        if (bookCopyIds.contains(copyId)) {
            BookCopy copy = bookList.searchCopy(copyId);
            if (copy != null) {
                copy.setAvailable(true);
                bookCopyIds.remove(copyId);
                System.out.println("Book copy returned: " + copyId);
                return true;
            }
        }
        System.out.println("Book copy not found in this borrow slip: " + copyId);
        return false;
    }
    
    public String getBorrowId() { return borrowId; }
    public ArrayList<String> getBookCopyIds() { return new ArrayList<>(bookCopyIds); }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public Reader getReader() { return reader; }
    
    public void setBorrowDate(int year, int month, int dayOfMonth) {
        validateBorrowDate(year, month, dayOfMonth);
        this.borrowDate = LocalDate.of(year, month, dayOfMonth);
        this.dueDate = this.borrowDate.plusDays(BORROW_PERIOD_DAYS);
    }
    
    public void setReader(Reader reader) {
        validateReader(reader);
        this.reader = reader;
    }
    
    public void showInfo(BookList bookList) {
        System.out.println("\n=== BORROW SLIP ===");
        System.out.println("Borrow ID: " + getBorrowId());
        System.out.println("Reader: " + reader.getName() + " (" + reader.getId() + ")");
        System.out.println("Borrow Date: " + getBorrowDate());
        System.out.println("Due Date: " + getDueDate());
        System.out.println("Borrowed Books (" + bookCopyIds.size() + "/" + MAX_BOOKS_PER_READER + "):");
        
        if (bookCopyIds.isEmpty()) {
            System.out.println("  No books borrowed.");
        } else {
            for (String copyId : bookCopyIds) {
                BookCopy copy = bookList.searchCopy(copyId);
                if (copy != null) {
                    System.out.println("  - " + copy.getBook().getTitle() + " (" + copyId + ")");
                }
            }
        }
        
        if (isOverdue()) {
            System.out.println("⚠️  OVERDUE by " + getDaysOverdue() + " days!");
            System.out.println("Late fee: " + calculateLateFee() + " VND");
        }
        System.out.println("===================");
    }
    
    public static void updateCountFromFile(ArrayList<BorrowSlip> slips) {
        int max = 0;
        for (BorrowSlip slip : slips) {
            try {
                String numStr = slip.getBorrowId().substring(3);
                int num = Integer.parseInt(numStr);
                if (num > max) max = num;
            } catch (Exception e) {
                System.out.println("Warning: Invalid borrow slip ID format: " + slip.getBorrowId());
            }
        }
        count = max;
    }
    
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }
    
    public int getDaysOverdue() {
        if (isOverdue()) {
            return (int) (LocalDate.now().toEpochDay() - dueDate.toEpochDay());
        }
        return 0;
    }
    
    public double calculateLateFee() {
        if (!isOverdue()) return 0.0;
        
        int overdueDays = getDaysOverdue();
        double dailyFee = 5000.0;
        return overdueDays * dailyFee;
    }
    
    public boolean canBorrowMoreBooks() {
        return bookCopyIds.size() < MAX_BOOKS_PER_READER;
    }
    
    public int getRemainingBookSlots() {
        return MAX_BOOKS_PER_READER - bookCopyIds.size();
    }
}