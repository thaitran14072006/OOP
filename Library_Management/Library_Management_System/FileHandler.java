import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    private static final String BASE_PATH = System.getProperty("user.dir")+"/Library_Management_System/";

    private static final String BOOKS_FILE = BASE_PATH + "books.txt";
    private static final String AUTHORS_FILE = BASE_PATH + "authors.txt";
    private static final String READERS_FILE = BASE_PATH + "readers.txt";
    private static final String BOOK_COPIES_FILE = BASE_PATH + "book_copies.txt";
    private static final String BORROW_SLIPS_FILE = BASE_PATH + "borrow_slips.txt";


    
    public static ArrayList<Book> loadBooks() { //TODO: trả về Array List
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        
        if (!file.exists()) {
            System.out.println("Looking for books file at: " + file.getAbsolutePath());
            return books;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Book currentBook = null;
            ArrayList<String> currentCategories = new ArrayList<>();
            ArrayList<String> currentAuthorIds = new ArrayList<>();
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.equals("---")) {
                    if (currentBook != null) {
                        for (String category : currentCategories) {
                            try {
                                currentBook.addCategory(category);
                            } catch (Exception e) {
                                System.out.println("Warning: Could not add category: " + e.getMessage());
                            }
                        }
                        currentBook.setAuthorIds(currentAuthorIds);
                        books.add(currentBook);
                    }
                    
                    currentBook = null;
                    currentCategories.clear();
                    currentAuthorIds.clear();
                    continue;
                }
                
                if (line.isEmpty()) continue;
                
                if (line.startsWith("CATEGORIES:")) {
                    String categoriesStr = line.substring(11).trim();
                    if (!categoriesStr.isEmpty()) {
                        String[] categories = categoriesStr.split(",");
                        for (String category : categories) {
                            currentCategories.add(category.trim());
                        }
                    }
                } else if (line.startsWith("AUTHORS:")) {
                    String authorsStr = line.substring(8).trim();
                    if (!authorsStr.isEmpty()) {
                        String[] authors = authorsStr.split(",");
                        for (String authorId : authors) {
                            currentAuthorIds.add(authorId.trim());
                        }
                    }
                } else {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        currentBook = new Book(parts[0].trim(), parts[1].trim(), parts[2].trim());
                    }
                }
            }
            
            if (currentBook != null) {
                for (String category : currentCategories) {
                    try {
                        currentBook.addCategory(category);
                    } catch (Exception e) {
                        System.out.println("Warning: Could not add category: " + e.getMessage());
                    }
                }
                currentBook.setAuthorIds(currentAuthorIds);
                books.add(currentBook);
            }
            
        } catch (IOException e) {
            System.out.println("Error reading books file: " + e.getMessage());
        }
        
        return books;
    }
    
    
    public static ArrayList<Author> loadAuthors() {
        ArrayList<Author> authors = new ArrayList<>();
        File file = new File(AUTHORS_FILE);
        
        if (!file.exists()) {
            System.out.println("Authors file not found: " + AUTHORS_FILE);
            return authors;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        try {
                            String[] dateParts = parts[2].split("-");
                            int year = Integer.parseInt(dateParts[0]);
                            int month = Integer.parseInt(dateParts[1]);
                            int day = Integer.parseInt(dateParts[2]);
                            
                            Author author = new Author(parts[0].trim(), parts[1].trim(), year, month, day, 
                                                     parts[3].trim(), parts[4].trim(), parts[5].trim());
                            authors.add(author);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing date for author: " + parts[0] + " - " + parts[1]);
                        }
                    } else {
                        System.out.println("Invalid author format: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Authors file not found: " + AUTHORS_FILE);
        } catch (Exception e) {
            System.out.println("Error loading authors: " + e.getMessage());
        }
        return authors;
    }
    
    public static void saveReaders(ArrayList<Reader> readers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(READERS_FILE))) {
            for (Reader reader : readers) {
                writer.println(reader.getId() + "|" + reader.getName() + "|" + 
                             reader.getDate() + "|" + reader.getPhoneNumber() + "|" + 
                             reader.getEmail() + "|" + reader.getAddress());
            }
            System.out.println("Readers saved successfully to " + READERS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving readers: " + e.getMessage());
        }
    }
    
    public static ArrayList<Reader> loadReaders() {
        ArrayList<Reader> readers = new ArrayList<>();
        File file = new File(READERS_FILE);
        
        if (!file.exists()) {
            System.out.println("Readers file not found: " + READERS_FILE);
            return readers;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        try {
                            String[] dateParts = parts[2].split("-");
                            int year = Integer.parseInt(dateParts[0]);
                            int month = Integer.parseInt(dateParts[1]);
                            int day = Integer.parseInt(dateParts[2]);
                            
                            Reader reader = new Reader(parts[0].trim(), parts[1].trim(), year, month, day, 
                                                     parts[3].trim(), parts[4].trim(), parts[5].trim());
                            readers.add(reader);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing date for reader: " + parts[0] + " - " + parts[1]);
                        }
                    } else {
                        System.out.println("Invalid reader format: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Readers file not found: " + READERS_FILE);
        } catch (Exception e) {
            System.out.println("Error loading readers: " + e.getMessage());
        }
        return readers;
    }
    
    public static void saveBookCopies(ArrayList<BookCopy> copies) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOK_COPIES_FILE))) {
            for (BookCopy copy : copies) {
                writer.println(copy.getCopyId() + "|" + copy.getBook().getId() + "|" + 
                             copy.isAvailable());
            }
            System.out.println("Book copies saved successfully to " + BOOK_COPIES_FILE);
        } catch (IOException e) {
            System.out.println("Error saving book copies: " + e.getMessage());
        }
    }
    
    public static ArrayList<BookCopy> loadBookCopies(ArrayList<Book> books) {
        ArrayList<BookCopy> copies = new ArrayList<>();
        File file = new File(BOOK_COPIES_FILE);
        
        if (!file.exists()) {
            System.out.println("Book copies file not found: " + BOOK_COPIES_FILE);
            return copies;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String copyId = parts[0].trim();
                        String bookId = parts[1].trim();
                        boolean isAvailable = Boolean.parseBoolean(parts[2].trim());
                        
                        Book book = findBookById(books, bookId);
                        if (book != null) {
                            BookCopy copy = new BookCopy(copyId, book, isAvailable);
                            copies.add(copy);
                        } else {
                            System.out.println("Warning: Book not found for copy " + copyId + ", book ID: " + bookId);
                        }
                    } else {
                        System.out.println("Invalid book copy format: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Book copies file not found: " + BOOK_COPIES_FILE);
        } catch (Exception e) {
            System.out.println("Error loading book copies: " + e.getMessage());
        }
        return copies;
    }
    
    public static void saveBorrowSlips(ArrayList<BorrowSlip> slips) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BORROW_SLIPS_FILE))) {
            for (BorrowSlip slip : slips) {
                writer.print(slip.getBorrowId() + "|" + slip.getBorrowDate() + "|" + 
                           slip.getReader().getId() + "|");
                
                ArrayList<String> copyIds = slip.getBookCopyIds();
                for (int i = 0; i < copyIds.size(); i++) {
                    writer.print(copyIds.get(i));
                    if (i < copyIds.size() - 1) writer.print(",");
                }
                writer.println();
            }
            System.out.println("Borrow slips saved successfully to " + BORROW_SLIPS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving borrow slips: " + e.getMessage());
        }
    }
    
    public static ArrayList<BorrowSlip> loadBorrowSlips(ArrayList<Reader> readers, ArrayList<BookCopy> copies) {
        ArrayList<BorrowSlip> slips = new ArrayList<>();
        File file = new File(BORROW_SLIPS_FILE);
        
        if (!file.exists()) {
            System.out.println("Borrow slips file not found: " + BORROW_SLIPS_FILE);
            return slips;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        try {
                            String borrowId = parts[0].trim();
                            String dateStr = parts[1].trim();
                            String readerId = parts[2].trim();
                            
                            String[] dateParts = dateStr.split("-");
                            int year = Integer.parseInt(dateParts[0]);
                            int month = Integer.parseInt(dateParts[1]);
                            int day = Integer.parseInt(dateParts[2]);
                            
                            Reader reader = findReaderById(readers, readerId);
                            if (reader != null) {
                                BorrowSlip slip = new BorrowSlip(borrowId, year, month, day, reader);
                                
                                if (parts.length >= 4 && !parts[3].isEmpty()) {
                                    String[] copyIds = parts[3].split(",");
                                    for (String copyId : copyIds) {
                                        String trimmedCopyId = copyId.trim();
                                        if (!trimmedCopyId.isEmpty()) {
                                            slip.getBookCopyIds().add(trimmedCopyId);
                                        }
                                    }
                                }
                                
                                slips.add(slip);
                            } else {
                                System.out.println("Warning: Reader not found for borrow slip " + borrowId + ", reader ID: " + readerId);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing date for borrow slip: " + parts[0]);
                        }
                    } else {
                        System.out.println("Invalid borrow slip format: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Borrow slips file not found: " + BORROW_SLIPS_FILE);
        } catch (Exception e) {
            System.out.println("Error loading borrow slips: " + e.getMessage());
        }
        return slips;
    }
    
    private static Book findBookById(ArrayList<Book> books, String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }
    
    private static Reader findReaderById(ArrayList<Reader> readers, String id) {
        for (Reader reader : readers) {
            if (reader.getId().equals(id)) {
                return reader;
            }
        }
        return null;
    }
    
    public static Author findAuthorById(ArrayList<Author> authors, String id) {
        for (Author author : authors) {
            if (author.getId().equals(id)) {
                return author;
            }
        }
        return null;
    }
    
    public static void linkAuthorsToBooks(ArrayList<Book> books, ArrayList<Author> authors) {
        for (Book book : books) {
            book.linkAuthors(authors);
        }
    }
    
    public static void createBackup() {
        String backupDir = "backup_" + System.currentTimeMillis();
        File dir = new File(backupDir);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Created backup directory: " + backupDir);
            } else {
                System.out.println("Failed to create backup directory: " + backupDir);
                return;
            }
        }
        
        copyFile(BOOKS_FILE, backupDir + "/" + BOOKS_FILE);
        copyFile(AUTHORS_FILE, backupDir + "/" + AUTHORS_FILE);
        copyFile(READERS_FILE, backupDir + "/" + READERS_FILE);
        copyFile(BOOK_COPIES_FILE, backupDir + "/" + BOOK_COPIES_FILE);
        copyFile(BORROW_SLIPS_FILE, backupDir + "/" + BORROW_SLIPS_FILE);
        
        System.out.println("Backup created successfully in directory: " + backupDir);
    }
    
    private static void copyFile(String sourcePath, String destPath) {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            System.out.println("Source file not found for backup: " + sourcePath);
            return;
        }
        
        try (InputStream in = new FileInputStream(sourcePath);
             OutputStream out = new FileOutputStream(destPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Backed up: " + sourcePath);
        } catch (IOException e) {
            System.out.println("Error backing up " + sourcePath + ": " + e.getMessage());
        }
    }
    
    public static boolean validateDataConsistency(ArrayList<Book> books, ArrayList<Author> authors, 
                                                ArrayList<Reader> readers, ArrayList<BookCopy> copies,
                                                ArrayList<BorrowSlip> slips) {
        System.out.println("\n=== VALIDATING DATA CONSISTENCY ===");
        boolean consistent = true;
        int issues = 0;
        
        for (Book book : books) {
            for (Author author : book.getAuthors()) {
                if (!authors.contains(author)) {
                    System.out.println(" Book '" + book.getTitle() + "' has non-existent author: " + author.getName());
                    consistent = false;
                    issues++;
                }
            }
        }
        
        for (BorrowSlip slip : slips) {
            if (!readers.contains(slip.getReader())) {
                System.out.println(" Borrow slip " + slip.getBorrowId() + " has non-existent reader: " + slip.getReader().getName());
                consistent = false;
                issues++;
            }
            
            for (String copyId : slip.getBookCopyIds()) {
                boolean copyFound = false;
                for (BookCopy copy : copies) {
                    if (copy.getCopyId().equals(copyId)) {
                        copyFound = true;
                        break;
                    }
                }
                if (!copyFound) {
                    System.out.println(" Borrow slip " + slip.getBorrowId() + " has non-existent book copy: " + copyId);
                    consistent = false;
                    issues++;
                }
            }
        }
        
        for (BookCopy copy : copies) {
            if (!books.contains(copy.getBook())) {
                System.out.println(" Book copy " + copy.getCopyId() + " has non-existent book: " + copy.getBook().getTitle());
                consistent = false;
                issues++;
            }
        }
        
        if (consistent) {
            System.out.println(" All data is consistent! No issues found.");
        } else {
            System.out.println(" Found " + issues + " data consistency issues!");
        }
        
        return consistent;
    }

    
}