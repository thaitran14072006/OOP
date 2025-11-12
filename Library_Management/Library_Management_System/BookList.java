import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BookList implements I_Management<Book> {
    private final Map<String, Book> books = new TreeMap<>();
    private final Map<String, BookCopy> bookCopies = new TreeMap<>();

    private void TranstoMap(ArrayList<Book> load){
    books.clear();           // Chỉ clear books
    for(Book k: load){
        books.put(k.getId(), k);
    }
    }

private void TranstoMap2(ArrayList<BookCopy> load){
    bookCopies.clear();      // Chỉ clear bookCopies
    for(BookCopy k: load){
        bookCopies.put(k.getCopyId(), k);
    }
}

    
    public BookList() {
        TranstoMap(FileHandler.loadBooks());
        TranstoMap2(FileHandler.loadBookCopies(FileHandler.loadBooks()));
    }
    
    @Override
    public void add(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        for (Book existingBook : books.values()) {
            if (existingBook.getTitle().equalsIgnoreCase(book.getTitle()) && 
                existingBook.getPublicationYear().equals(book.getPublicationYear())) {
                throw new IllegalArgumentException("A book with the same title and publication year already exists");
            }
        }
        
        books.put(book.getId(), book);
        System.out.println("Book added successfully: " + book.getTitle());
    }
    
    public void addCopy(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (!books.containsValue(book)) {
            throw new IllegalArgumentException("Book must be added to BookList before creating copies");
        }
        
        try {
            BookCopy copy = new BookCopy(book);
            bookCopies.put(copy.getCopyId(), copy);
            System.out.println("Book copy added: " + copy.getCopyId());
        } catch (IllegalStateException e) {
            System.out.println("Cannot add more copies: " + e.getMessage());
        }
    }
    
    public void addCopy(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        if (books.containsKey(bookId)) {
            addCopy(books.get(bookId));
        } else {
            System.out.println("Book not found!");
        }
    }
    
    @Override
    public void remove(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        if (books.containsKey(id)) {
            ArrayList<String> copiesToRemove = new ArrayList<>();
            for (BookCopy copy : bookCopies.values()) {
                if (copy.getBook().getId().equals(id)) {
                    copiesToRemove.add(copy.getCopyId());
                }
            }
            for (String copyId : copiesToRemove) {
                bookCopies.remove(copyId);
            }
            Book removedBook = books.remove(id);
            System.out.println("The book and all its copies have been removed: " + removedBook.getTitle());
        } else {
            System.out.println("Book does not exist!");
        }
    }
    
    public void removeCopy(String copyId) {
        if (copyId == null || copyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Copy ID cannot be null or empty");
        }
        
        if (bookCopies.containsKey(copyId)) {
            bookCopies.remove(copyId);
            System.out.println("Book copy removed: " + copyId);
        } else {
            System.out.println("Book copy not found!");
        }
    }
    
    @Override
    public Book search(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        if (books.containsKey(id)) {
            return books.get(id);
        } else {
            System.out.println("Book not found!");
            return null;
        }
    }
    
    public BookCopy searchCopy(String copyId) {
        if (copyId == null || copyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Copy ID cannot be null or empty");
        }
        
        if (bookCopies.containsKey(copyId)) {
            return bookCopies.get(copyId);
        } else {
            System.out.println("Book copy not found!");
            return null;
        }
    }
    
    public ArrayList<BookCopy> getAvailableCopies(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        ArrayList<BookCopy> available = new ArrayList<>();
        for (BookCopy copy : bookCopies.values()) {
            if (copy.getBook().getId().equals(bookId) && copy.isAvailable()) {
                available.add(copy);
            }
        }
        return available;
    }
    
    @Override
    public void editProfile(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Book ID cannot be null or empty");
            return;
        }
        
        if (!books.containsKey(id)) {
            System.out.println("Book not found!");
            return;
        }
        
        Scanner sc = new Scanner(System.in);
        Book book = books.get(id);
        int choice;
        
        do {
            System.out.println("\n--- Edit Book: " + book.getTitle() + " ---");
            System.out.println("1. Edit title");
            System.out.println("2. Edit publication year");
            System.out.println("3. Add category");
            System.out.println("4. Remove category");
            System.out.println("5. Show current information");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter new title: ");
                        String newTitle = sc.nextLine();
                        book.setTitle(newTitle);
                        System.out.println("Title updated successfully.");
                    }
                    case 2 -> {
                        System.out.print("Enter new publication year: ");
                        String newYear = sc.nextLine();
                        book.setPublicationYear(newYear);
                        System.out.println("Publication year updated successfully.");
                    }
                    case 3 -> {
                        System.out.print("Enter category to add: ");
                        String category = sc.nextLine();
                        book.addCategory(category);
                        System.out.println("Category added successfully.");
                    }
                    case 4 -> {
                        System.out.print("Enter category to remove: ");
                        String category = sc.nextLine();
                        if (book.removeCategory(category)) {
                            System.out.println("Category removed successfully.");
                        } else {
                            System.out.println("Category not found.");
                        }
                    }
                    case 5 -> book.showInfo();
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
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        
        if (books.containsKey(id)) {
            Book book = books.get(id);
            book.showInfo();
            
            ArrayList<BookCopy> availableCopies = getAvailableCopies(id);
            System.out.println("Available copies: " + availableCopies.size());
            for (BookCopy copy : availableCopies) {
                copy.showInfo();
            }
        } else {
            System.out.println("Book not found!");
        }
    }
    
    @Override
    public void showAll() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        
        System.out.println("\n--- ALL BOOKS ---");
        for (Book book : books.values()) {
            book.showInfo();
            System.out.println("---------------");
        }
    }
    
    public void showAllCopies() {
        if (bookCopies.isEmpty()) {
            System.out.println("No book copies available.");
            return;
        }
        
        System.out.println("\n--- ALL BOOK COPIES ---");
        for (BookCopy copy : bookCopies.values()) {
            copy.showInfo();
        }
    }
    
    public int getTotalBooks() {
        return books.size();
    }
    
    public int getTotalCopies() {
        return bookCopies.size();
    }
    
    public int getAvailableCopiesCount(String bookId) {
        return getAvailableCopies(bookId).size();
    }
    
    public ArrayList<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        
        ArrayList<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
    
    public TreeMap<String, Book> getBooksMap() {
        return new TreeMap<>(books);
    }
    
    public TreeMap<String, BookCopy> getBookCopiesMap() {
        return new TreeMap<>(bookCopies);
    }
    // Lưu toàn bộ sách và bản sao
    @Override
    public void save() {
        saveBooks();
        saveBookCopies(bookCopies);
    }

    // Lưu sách
    private void saveBooks() {
        String filePath = System.getProperty("user.dir") + "/Library_Management_System/books.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Book book : books.values()) {
                writer.println(book.getId() + "|" + book.getTitle() + "|" + book.getPublicationYear());

                ArrayList<String> categories = book.getCategories();
                if (!categories.isEmpty()) {
                    writer.print("CATEGORIES:" + String.join(",", categories));
                    writer.println();
                }

                ArrayList<Author> authors = book.getAuthors();
                if (!authors.isEmpty()) {
                    List<String> authorIds = new ArrayList<>();
                    for (Author a : authors) authorIds.add(a.getId());
                    writer.print("AUTHORS:" + String.join(",", authorIds));
                    writer.println();
                }

                writer.println("---");
            }

            System.out.println("Books saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Lưu bản sao từ Map
    public static void saveBookCopies(Map<String, BookCopy> bookCopies) {
        final String BOOK_COPIES_FILE = System.getProperty("user.dir") + "/Library_Management_System/book_copies.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOK_COPIES_FILE))) {
            for (BookCopy copy : bookCopies.values()) {
                writer.println(copy.getCopyId() + "|" + copy.getBook().getId() + "|" + copy.isAvailable());
            }
            System.out.println("Book copies saved successfully to " + BOOK_COPIES_FILE);
        } catch (IOException e) {
            System.out.println("Error saving book copies: " + e.getMessage());
        }
    }

    

}