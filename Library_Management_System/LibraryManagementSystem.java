import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagementSystem {
    private BookList bookList;
    private AuthorList authorList;
    private ReaderList readerList;
    private ArrayList<BorrowSlip> borrowSlips;
    
    public LibraryManagementSystem() {
        initializeSystem();
    }
    
    private void initializeSystem() {
        this.bookList = new BookList();
        this.authorList = new AuthorList();
        this.readerList = new ReaderList();
        this.borrowSlips = new ArrayList<>();
        
        loadAllData();
        
        System.out.println("\n============================================================");
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM INITIALIZED ===");
        System.out.println("============================================================");
        System.out.println("Loaded: " + bookList.getTotalBooks() + " books");
        System.out.println("Loaded: " + authorList.getTotalAuthors() + " authors");
        System.out.println("Loaded: " + readerList.getTotalReaders() + " readers");
        System.out.println("Loaded: " + bookList.getBookCopiesMap().size() + " book copies");
        System.out.println("Loaded: " + borrowSlips.size() + " borrow slips");
        System.out.println("============================================================");
    }
    
    private void loadAllData() {
        System.out.println("INITIALIZING LIBRARY MANAGEMENT SYSTEM...");
        
        ArrayList<Book> loadedBooks = FileHandler.loadBooks();
        ArrayList<Author> loadedAuthors = FileHandler.loadAuthors();
        
        if (loadedBooks.isEmpty()) {
            System.out.println("Warning: No books loaded. Starting with empty book list.");
        }
        if (loadedAuthors.isEmpty()) {
            System.out.println("Warning: No authors loaded. Starting with empty author list.");
        }
        
        FileHandler.linkAuthorsToBooks(loadedBooks, loadedAuthors);
        
        ArrayList<Reader> loadedReaders = FileHandler.loadReaders();
        ArrayList<BookCopy> loadedCopies = FileHandler.loadBookCopies(loadedBooks);
        ArrayList<BorrowSlip> loadedSlips = FileHandler.loadBorrowSlips(loadedReaders, loadedCopies);
        
        addDataToSystem(loadedBooks, loadedAuthors, loadedReaders, loadedCopies, loadedSlips);
        
        updateSystemCounters(loadedBooks, loadedAuthors, loadedReaders, loadedCopies, loadedSlips);
        validateSystemData();
    }
    
    private void addDataToSystem(ArrayList<Book> books, ArrayList<Author> authors, 
                               ArrayList<Reader> readers, ArrayList<BookCopy> copies,
                               ArrayList<BorrowSlip> slips) {
        
        for (Author author : authors) {
            try {
                authorList.getAuthorsMap().put(author.getId(), author);
            } catch (Exception e) {
                System.out.println("Error adding author: " + e.getMessage());
            }
        }
        
        for (Book book : books) {
            try {
                bookList.getBooksMap().put(book.getId(), book);
            } catch (Exception e) {
                System.out.println("Error adding book: " + e.getMessage());
            }
        }
        
        for (Book book : books) {
            book.linkAuthors(authors);
        }
        
        for (Reader reader : readers) {
            try {
                readerList.getReadersMap().put(reader.getId(), reader);
            } catch (Exception e) {
                System.out.println("Error adding reader: " + e.getMessage());
            }
        }
        
        for (BookCopy copy : copies) {
            try {
                bookList.getBookCopiesMap().put(copy.getCopyId(), copy);
            } catch (Exception e) {
                System.out.println("Error adding book copy: " + e.getMessage());
            }
        }
        
        this.borrowSlips.clear();
        this.borrowSlips.addAll(slips);
    }
    
    private void updateSystemCounters(ArrayList<Book> books, ArrayList<Author> authors,
                                    ArrayList<Reader> readers, ArrayList<BookCopy> copies,
                                    ArrayList<BorrowSlip> slips) {
        
        Book.updateCountFromFile(books);
        Author.updateCountFromFile(authors);
        
        try {
            Reader.class.getMethod("updateCountFromFile", ArrayList.class).invoke(null, readers);
        } catch (Exception e) {
        }
        
        try {
            BookCopy.class.getMethod("updateCountFromFile", ArrayList.class).invoke(null, copies);
        } catch (Exception e) {
        }
        
        try {
            BorrowSlip.class.getMethod("updateCountFromFile", ArrayList.class).invoke(null, slips);
        } catch (Exception e) {
        }
    }
    
    private void validateSystemData() {
        ArrayList<Book> books = new ArrayList<>(bookList.getBooksMap().values());
        ArrayList<Author> authors = new ArrayList<>(authorList.getAuthorsMap().values());
        ArrayList<Reader> readers = new ArrayList<>(readerList.getReadersMap().values());
        ArrayList<BookCopy> copies = new ArrayList<>(bookList.getBookCopiesMap().values());
        
        FileHandler.validateDataConsistency(books, authors, readers, copies, borrowSlips);
    }
    
    public void saveAllData() {
        System.out.println("\nSAVING ALL DATA TO FILES...");
        
        try {
            ArrayList<Book> books = new ArrayList<>(bookList.getBooksMap().values());
            ArrayList<Author> authors = new ArrayList<>(authorList.getAuthorsMap().values());
            ArrayList<Reader> readers = new ArrayList<>(readerList.getReadersMap().values());
            ArrayList<BookCopy> copies = new ArrayList<>(bookList.getBookCopiesMap().values());
            
            FileHandler.saveBooks(books);
            FileHandler.saveAuthors(authors);
            FileHandler.saveReaders(readers);
            FileHandler.saveBookCopies(copies);
            FileHandler.saveBorrowSlips(borrowSlips);
            
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    public void createBackup() {
        System.out.println("\nCREATING SYSTEM BACKUP...");
        try {
            saveAllData();
            FileHandler.createBackup();
            System.out.println("Backup created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    public void showMainMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        
        do {
            System.out.println("\n========================================");
            System.out.println("=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("========================================");
            System.out.println("1. Book Management");
            System.out.println("2. Author Management");
            System.out.println("3. Reader Management");
            System.out.println("4. Borrow/Return Management");
            System.out.println("5. Reports and Statistics");
            System.out.println("6. Data Management");
            System.out.println("0. Exit");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> showBookManagementMenu(sc);
                    case 2 -> showAuthorManagementMenu(sc);
                    case 3 -> showReaderManagementMenu(sc);
                    case 4 -> showBorrowManagementMenu(sc);
                    case 5 -> showReportsMenu(sc);
                    case 6 -> showDataManagementMenu(sc);
                    case 0 -> {
                        System.out.println("\nSaving data before exit...");
                        saveAllData();
                        System.out.println("Thank you for using Library Management System!");
                    }
                    default -> System.out.println("Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
        
        sc.close();
    }
    
    private void showBookManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("=== BOOK MANAGEMENT ===");
            System.out.println("==============================");
            System.out.println("1. Add new book");
            System.out.println("2. Remove book");
            System.out.println("3. Search book");
            System.out.println("4. Edit book");
            System.out.println("5. Show all books");
            System.out.println("6. Add book copy");
            System.out.println("7. Show all book copies");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter book title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter publication year: ");
                        String year = sc.nextLine();
                        
                        try {
                            Book book = new Book(title, year);
                            bookList.add(book);
                            System.out.println("Book added successfully: " + book.getId());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter book ID to remove: ");
                        String bookId = sc.nextLine();
                        bookList.remove(bookId);
                    }
                    case 3 -> {
                        System.out.print("Enter book ID to search: ");
                        String bookId = sc.nextLine();
                        bookList.show(bookId);
                    }
                    case 4 -> {
                        System.out.print("Enter book ID to edit: ");
                        String bookId = sc.nextLine();
                        bookList.editProfile(bookId);
                    }
                    case 5 -> bookList.showAll();
                    case 6 -> {
                        System.out.print("Enter book ID to add copy: ");
                        String bookId = sc.nextLine();
                        bookList.addCopy(bookId);
                    }
                    case 7 -> bookList.showAllCopies();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }
    
    private void showAuthorManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("=== AUTHOR MANAGEMENT ===");
            System.out.println("==============================");
            System.out.println("1. Add new author");
            System.out.println("2. Remove author");
            System.out.println("3. Search author");
            System.out.println("4. Edit author");
            System.out.println("5. Show all authors");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter author name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter birth year: ");
                        int year = sc.nextInt();
                        System.out.print("Enter birth month: ");
                        int month = sc.nextInt();
                        System.out.print("Enter birth day: ");
                        int day = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter pen name: ");
                        String penName = sc.nextLine();
                        System.out.print("Enter email: ");
                        String email = sc.nextLine();
                        System.out.print("Enter phone number: ");
                        String phone = sc.nextLine();
                        
                        try {
                            Author author = new Author(name, year, month, day, penName, email, phone);
                            authorList.add(author);
                            System.out.println("Author added successfully: " + author.getId());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter author ID to remove: ");
                        String authorId = sc.nextLine();
                        authorList.remove(authorId);
                    }
                    case 3 -> {
                        System.out.print("Enter author ID to search: ");
                        String authorId = sc.nextLine();
                        authorList.show(authorId);
                    }
                    case 4 -> {
                        System.out.print("Enter author ID to edit: ");
                        String authorId = sc.nextLine();
                        authorList.editProfile(authorId);
                    }
                    case 5 -> authorList.showAll();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }
    
    private void showReaderManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("=== READER MANAGEMENT ===");
            System.out.println("==============================");
            System.out.println("1. Add new reader");
            System.out.println("2. Remove reader");
            System.out.println("3. Search reader");
            System.out.println("4. Edit reader");
            System.out.println("5. Show all readers");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter reader name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter birth year: ");
                        int year = sc.nextInt();
                        System.out.print("Enter birth month: ");
                        int month = sc.nextInt();
                        System.out.print("Enter birth day: ");
                        int day = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter phone number: ");
                        String phone = sc.nextLine();
                        System.out.print("Enter email: ");
                        String email = sc.nextLine();
                        System.out.print("Enter address: ");
                        String address = sc.nextLine();
                        
                        try {
                            Reader reader = new Reader(name, year, month, day, phone, email, address);
                            readerList.add(reader);
                            System.out.println("Reader added successfully: " + reader.getId());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter reader ID to remove: ");
                        String readerId = sc.nextLine();
                        readerList.remove(readerId);
                    }
                    case 3 -> {
                        System.out.print("Enter reader ID to search: ");
                        String readerId = sc.nextLine();
                        readerList.show(readerId);
                    }
                    case 4 -> {
                        System.out.print("Enter reader ID to edit: ");
                        String readerId = sc.nextLine();
                        readerList.editProfile(readerId);
                    }
                    case 5 -> readerList.showAll();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }
    
    private void showBorrowManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n===================================");
            System.out.println("=== BORROW/RETURN MANAGEMENT ===");
            System.out.println("===================================");
            System.out.println("1. Create new borrow slip");
            System.out.println("2. Add book to borrow slip");
            System.out.println("3. Return book");
            System.out.println("4. Show borrow slip details");
            System.out.println("5. Show all borrow slips");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            try {
                choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter reader ID: ");
                        String readerId = sc.nextLine();
                        Reader reader = readerList.search(readerId);
                        if (reader != null) {
                            System.out.print("Enter borrow year: ");
                            int year = sc.nextInt();
                            System.out.print("Enter borrow month: ");
                            int month = sc.nextInt();
                            System.out.print("Enter borrow day: ");
                            int day = sc.nextInt();
                            sc.nextLine();
                            
                            try {
                                BorrowSlip slip = new BorrowSlip(year, month, day, reader);
                                borrowSlips.add(slip);
                                System.out.println("Borrow slip created: " + slip.getBorrowId());
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Reader not found!");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = findBorrowSlip(slipId);
                        if (slip != null) {
                            System.out.print("Enter book copy ID to borrow: ");
                            String copyId = sc.nextLine();
                            slip.addBookCopy(bookList, copyId);
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = findBorrowSlip(slipId);
                        if (slip != null) {
                            System.out.print("Enter book copy ID to return: ");
                            String copyId = sc.nextLine();
                            slip.returnBookCopy(bookList, copyId);
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = findBorrowSlip(slipId);
                        if (slip != null) {
                            slip.showInfo(bookList);
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 5 -> showAllBorrowSlips();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }
    
    private void showReportsMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n===================================");
            System.out.println("=== REPORTS AND STATISTICS ===");
            System.out.println("===================================");
            System.out.println("1. Library statistics");
            System.out.println("2. Overdue books report");
            System.out.println("3. Available books report");
            System.out.println("4. Reader activity report");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1 -> showLibraryStatistics();
                case 2 -> showOverdueReport();
                case 3 -> showAvailableBooksReport();
                case 4 -> showReaderActivityReport();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (choice != 0);
    }
    
    private void showDataManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("=== DATA MANAGEMENT ===");
            System.out.println("==============================");
            System.out.println("1. Save all data");
            System.out.println("2. Create backup");
            System.out.println("3. Validate data consistency");
            System.out.println("4. Reload all data");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            
            choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1 -> saveAllData();
                case 2 -> createBackup();
                case 3 -> {
                    ArrayList<Book> books = new ArrayList<>(bookList.getBooksMap().values());
                    ArrayList<Author> authors = new ArrayList<>(authorList.getAuthorsMap().values());
                    ArrayList<Reader> readers = new ArrayList<>(readerList.getReadersMap().values());
                    ArrayList<BookCopy> copies = new ArrayList<>(bookList.getBookCopiesMap().values());
                    
                    boolean consistent = FileHandler.validateDataConsistency(books, authors, readers, copies, borrowSlips);
                    if (consistent) {
                        System.out.println("All data is consistent!");
                    } else {
                        System.out.println("Data consistency issues found!");
                    }
                }
                case 4 -> {
                    System.out.println("Reloading all data from files...");
                    initializeSystem();
                }
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (choice != 0);
    }
    
    private BorrowSlip findBorrowSlip(String slipId) {
        for (BorrowSlip slip : borrowSlips) {
            if (slip.getBorrowId().equals(slipId)) {
                return slip;
            }
        }
        return null;
    }
    
    private void showAllBorrowSlips() {
        if (borrowSlips.isEmpty()) {
            System.out.println("No borrow slips available.");
            return;
        }
        
        System.out.println("\n=== ALL BORROW SLIPS ===");
        for (BorrowSlip slip : borrowSlips) {
            slip.showInfo(bookList);
        }
    }
    
    private void showLibraryStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + bookList.getTotalBooks());
        System.out.println("Total Book Copies: " + bookList.getTotalCopies());
        System.out.println("Total Authors: " + authorList.getTotalAuthors());
        System.out.println("Total Readers: " + readerList.getTotalReaders());
        System.out.println("Active Borrow Slips: " + borrowSlips.size());
        
        int totalBorrowedBooks = 0;
        int overdueCount = 0;
        for (BorrowSlip slip : borrowSlips) {
            totalBorrowedBooks += slip.getBookCopyIds().size();
            if (slip.isOverdue()) {
                overdueCount++;
            }
        }
        
        System.out.println("Currently Borrowed Books: " + totalBorrowedBooks);
        System.out.println("Overdue Borrow Slips: " + overdueCount);
        
        int totalCopies = bookList.getTotalCopies();
        if (totalCopies > 0) {
            double availabilityRate = ((double) (totalCopies - totalBorrowedBooks) / totalCopies) * 100;
            System.out.println("Book Availability Rate: " + String.format("%.2f", availabilityRate) + "%");
        }
    }
    
    private void showOverdueReport() {
        System.out.println("\n=== OVERDUE BOOKS REPORT ===");
        boolean foundOverdue = false;
        
        for (BorrowSlip slip : borrowSlips) {
            if (slip.isOverdue()) {
                foundOverdue = true;
                System.out.println("Borrow Slip: " + slip.getBorrowId());
                System.out.println("Reader: " + slip.getReader().getName());
                System.out.println("Days Overdue: " + slip.getDaysOverdue());
                System.out.println("Late Fee: " + slip.calculateLateFee() + " VND");
                System.out.println("---");
            }
        }
        
        if (!foundOverdue) {
            System.out.println("No overdue books found.");
        }
    }
    
    private void showAvailableBooksReport() {
        System.out.println("\n=== AVAILABLE BOOKS REPORT ===");
        int availableCount = 0;
        
        for (Book book : bookList.getBooksMap().values()) {
            int availableCopies = bookList.getAvailableCopiesCount(book.getId());
            if (availableCopies > 0) {
                System.out.println("Book: " + book.getTitle());
                System.out.println("Available Copies: " + availableCopies);
                System.out.println("---");
                availableCount++;
            }
        }
        
        if (availableCount == 0) {
            System.out.println("No available books found.");
        } else {
            System.out.println("Total books with available copies: " + availableCount);
        }
    }
    
    private void showReaderActivityReport() {
        System.out.println("\n=== READER ACTIVITY REPORT ===");
        
        for (Reader reader : readerList.getReadersMap().values()) {
            int readerBorrowCount = 0;
            for (BorrowSlip slip : borrowSlips) {
                if (slip.getReader().getId().equals(reader.getId())) {
                    readerBorrowCount += slip.getBookCopyIds().size();
                }
            }
            
            System.out.println("Reader: " + reader.getName());
            System.out.println("Total Books Borrowed: " + readerBorrowCount);
            System.out.println("---");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Library Management System...");
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.showMainMenu();
    }
}