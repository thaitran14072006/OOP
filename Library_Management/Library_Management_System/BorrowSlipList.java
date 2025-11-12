import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class BorrowSlipList implements I_Management<BorrowSlip> {
    private final Map<String, BorrowSlip> slips = new TreeMap<>();
    private static final String BORROW_SLIPS_FILE = System.getProperty("user.dir") + "/Library_Management_System/borrow_slips.txt";

    // Load từ file
    public BorrowSlipList(BookList bookList, Map<String, Reader> readers) {
        File file = new File(BORROW_SLIPS_FILE);
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;

                String borrowId = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                String readerId = parts[2];

                Reader reader = readers.get(readerId);
                if (reader == null) continue;

                BorrowSlip slip = new BorrowSlip(borrowId, date.getYear(), date.getMonthValue(), date.getDayOfMonth(), reader);

                if (parts.length > 3 && !parts[3].isEmpty()) {
                    String[] copyIds = parts[3].split(",");
                    for (String copyId : copyIds) {
                        BookCopy copy = bookList.searchCopy(copyId);
                        if (copy != null) {
                            copy.setAvailable(false);
                            slip.addBookCopyById(bookList, copyId);
                        }
                    }
                }
                slips.put(borrowId, slip);
            }
            BorrowSlip.updateCountFromFile(new ArrayList<>(slips.values()));
        } catch (Exception e) {
            System.out.println("Error loading borrow slips: " + e.getMessage());
        }
    }

    // === Implement interface I_Management ===
    
    @Override
    public void add(BorrowSlip slip) {
        addSlip(slip); // dùng lại method hiện có
    }

    public void addSlip(BorrowSlip slip) {
        if (slip == null) throw new IllegalArgumentException("Borrow slip cannot be null");
        slips.put(slip.getBorrowId(), slip);
        System.out.println("Borrow slip added: " + slip.getBorrowId());
    }

    @Override
    public void remove(String id) {
        if (slips.remove(id) != null) {
            System.out.println("Borrow slip removed: " + id);
        } else {
            System.out.println("Borrow slip not found: " + id);
        }
    }

    @Override
    public BorrowSlip search(String borrowId) {
        return slips.get(borrowId);
    }

    @Override
    public void editProfile(String id) {
        BorrowSlip slip = slips.get(id);
        if (slip == null) {
            System.out.println("Borrow slip not found: " + id);
            return;
        }
        // Nếu muốn chỉnh sửa ngày hoặc reader, có thể thêm logic ở đây
        System.out.println("Editing borrow slip not implemented yet for: " + id);
    }

    @Override
    public void show(String id) {
        BorrowSlip slip = slips.get(id);
        if (slip == null) {
            System.out.println("Borrow slip not found: " + id);
            return;
        }
        slip.showInfo(null); // nếu muốn show mà không cần BookList, hoặc truyền BookList
    }

    @Override
    public void showAll() {
        if (slips.isEmpty()) {
            System.out.println("No borrow slips available.");
            return;
        }
        for (BorrowSlip slip : slips.values()) {
            slip.showInfo(null); // có thể sửa để truyền BookList nếu cần
        }
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BORROW_SLIPS_FILE))) {
            for (BorrowSlip slip : slips.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(slip.getBorrowId()).append("|")
                  .append(slip.getBorrowDate()).append("|")
                  .append(slip.getReader().getId()).append("|")
                  .append(String.join(",", slip.getBookCopyIds()));
                writer.println(sb.toString());
            }
            System.out.println("Borrow slips saved successfully to " + BORROW_SLIPS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving borrow slips: " + e.getMessage());
        }
    }

    // Trả sách
    public boolean returnBook(BorrowSlip slip, BookList bookList, String copyId) {
        if (slip == null) return false;
        return slip.returnBookCopy(bookList, copyId);
    }

    // Trả tất cả sách trong phiếu
    public void returnAllBooks(BorrowSlip slip, BookList bookList) {
        if (slip == null) return;
        for (String copyId : new ArrayList<>(slip.getBookCopyIds())) {
            slip.returnBookCopy(bookList, copyId);
        }
    }

    // Lấy danh sách phiếu
    public Collection<BorrowSlip> getSlips() {
        return slips.values();
    }
}
