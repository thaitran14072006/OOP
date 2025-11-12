import java.util.*;

public class LibraryManagementSystem {
    private BookList bookList;
    private AuthorList authorList;
    private ReaderList readerList;
    private BorrowSlipList borrowSlipsList;

    public LibraryManagementSystem() {
        initializeSystem();
    }

    private void initializeSystem() {
        bookList = new BookList();
        authorList = new AuthorList();
        readerList = new ReaderList();
        borrowSlipsList = new BorrowSlipList(bookList, readerList.getReadersMap());

        System.out.println("\n============================================================");
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM INITIALIZED ===");
        System.out.println("============================================================");
        System.out.println("Loaded: " + bookList.getTotalBooks() + " books");
        System.out.println("Loaded: " + authorList.getTotalAuthors() + " authors");
        System.out.println("Loaded: " + readerList.getTotalReaders() + " readers");
        System.out.println("Loaded: " + bookList.getBookCopiesMap().size() + " book copies");
        System.out.println("Loaded: " + borrowSlipsList.getSlips().size() + " borrow slips");
        System.out.println("============================================================");
    }

    // Save tất cả dữ liệu
    public void saveAllData() {
        System.out.println("\nSAVING ALL DATA TO FILES...");
        try {
            bookList.save();
            authorList.save();
            readerList.save();
            borrowSlipsList.save();
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

    // =================== MAIN MENU ===================
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
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
        sc.close();
    }

    // =================== BOOK MENU ===================
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
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }

    // =================== AUTHOR MENU ===================
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
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }

    // =================== READER MENU ===================
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
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }

    // =================== BORROW/RETURN MENU ===================
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

                            BorrowSlip slip = new BorrowSlip(year, month, day, reader);
                            borrowSlipsList.add(slip);
                            System.out.println("Borrow slip created: " + slip.getBorrowId());
                        } else {
                            System.out.println("Reader not found!");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = borrowSlipsList.search(slipId);
                        if (slip != null) {
                            System.out.print("Enter book copy ID to borrow: ");
                            String copyId = sc.nextLine();
                            slip.addBookCopyById(bookList, copyId);
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = borrowSlipsList.search(slipId);
                        if (slip != null) {
                            System.out.print("Enter book copy ID to return: ");
                            String copyId = sc.nextLine();
                            if (borrowSlipsList.returnBook(slip, bookList, copyId)) {
                                System.out.println("Book copy returned successfully.");
                            } else {
                                System.out.println("Book copy not found in this borrow slip.");
                            }
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter borrow slip ID: ");
                        String slipId = sc.nextLine();
                        BorrowSlip slip = borrowSlipsList.search(slipId);
                        if (slip != null) {
                            slip.showInfo(bookList);
                        } else {
                            System.out.println("Borrow slip not found!");
                        }
                    }
                    case 5 -> borrowSlipsList.showAll();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }

    // =================== REPORT MENU ===================
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

    // =================== DATA MENU ===================
    private void showDataManagementMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("=== DATA MANAGEMENT ===");
            System.out.println("==============================");
            System.out.println("1. Save all data");
            System.out.println("2. Create backup");
            System.out.println("3. Reload all data");
            System.out.println("0. Back to main menu");
            System.out.print("Select option: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> saveAllData();
                case 2 -> createBackup();
                case 3 -> initializeSystem();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (choice != 0);
    }

    // =================== STATISTICS ===================
    private void showLibraryStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + bookList.getTotalBooks());
        System.out.println("Total Book Copies: " + bookList.getTotalCopies());
        System.out.println("Total Authors: " + authorList.getTotalAuthors());
        System.out.println("Total Readers: " + readerList.getTotalReaders());
        System.out.println("Active Borrow Slips: " + borrowSlipsList.getSlips().size());

        int totalBorrowedBooks = 0, overdueCount = 0;
        for (BorrowSlip slip : borrowSlipsList.getSlips()) {
            totalBorrowedBooks += slip.getBookCopyIds().size();
            if (slip.isOverdue()) overdueCount++;
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
        boolean found = false;
        for (BorrowSlip slip : borrowSlipsList.getSlips()) {
            if (slip.isOverdue()) {
                found = true;
                System.out.println("Borrow Slip: " + slip.getBorrowId());
                System.out.println("Reader: " + slip.getReader().getName());
                System.out.println("Days Overdue: " + slip.getDaysOverdue());
                System.out.println("Late Fee: " + slip.calculateLateFee() + " VND");
                System.out.println("---");
            }
        }
        if (!found) System.out.println("No overdue books found.");
    }

    private void showAvailableBooksReport() {
        System.out.println("\n=== AVAILABLE BOOKS REPORT ===");
        int count = 0;
        for (Book book : bookList.getBooksMap().values()) {
            int available = bookList.getAvailableCopiesCount(book.getId());
            if (available > 0) {
                System.out.println("Book: " + book.getTitle() + " | Available Copies: " + available);
                count++;
            }
        }
        if (count == 0) System.out.println("No available books found.");
        else System.out.println("Total books with available copies: " + count);
    }

    private void showReaderActivityReport() {
        System.out.println("\n=== READER ACTIVITY REPORT ===");
        for (Reader reader : readerList.getReadersMap().values()) {
            int total = 0;
            for (BorrowSlip slip : borrowSlipsList.getSlips()) {
                if (slip.getReader().getId().equals(reader.getId())) {
                    total += slip.getBookCopyIds().size();
                }
            }
            System.out.println("Reader: " + reader.getName() + " | Total Books Borrowed: " + total);
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Library Management System...");
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.showMainMenu();
    }
}
