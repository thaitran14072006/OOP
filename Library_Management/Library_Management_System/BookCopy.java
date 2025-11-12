import java.util.ArrayList;

public class BookCopy {
    private String copyId;
    private Book book;
    private boolean isAvailable;
    private static int totalCopies = 0; // Tổng số bản sao trong thư viện

    private static final int MAX_COPIES_PER_BOOK = 50;

    public BookCopy(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        // Kiểm tra số bản sao của cuốn sách này
        if (book.getCopyCount() >= MAX_COPIES_PER_BOOK) {
            throw new IllegalStateException(
                "Cannot create more than " + MAX_COPIES_PER_BOOK + " copies for a single book"
            );
        }

        this.book = book;
        this.isAvailable = true;

        // Tăng số lượng bản sao của cuốn sách
        book.incrementCopyCount();

        // Tăng tổng số bản sao trong thư viện
        totalCopies++;

        // Tạo copyId dựa trên ID sách và số bản sao của sách đó
        this.copyId = book.getId() + "-" + String.format("%03d", book.getCopyCount());
    }

    // Khởi tạo từ dữ liệu đã lưu (file)
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

        // Cập nhật số bản sao của sách nếu cần
        int numberPart;
        try {
            numberPart = Integer.parseInt(copyId.split("-")[1]);
        } catch (Exception e) {
            numberPart = book.getCopyCount() + 1;
        }
        if (numberPart > book.getCopyCount()) {
            book.setCopyCount(numberPart);
        }

        // Cập nhật tổng số bản sao
        if (numberPart > totalCopies) totalCopies = numberPart;
    }

    public String getCopyId() {
        return copyId;
    }

    public Book getBook() {
        return book;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public static int getTotalCopies() {
        return totalCopies;
    }

    public void showInfo() {
        System.out.println("Copy ID: " + copyId);
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("---");
    }

    public static void updateCountFromFile(ArrayList<BookCopy> copies) {
        int max = 0;
        for (BookCopy copy : copies) {
            try {
                String copyNumStr = copy.getCopyId().split("-")[1];
                int num = Integer.parseInt(copyNumStr);
                if (num > max) max = num;

                // Cập nhật số bản sao riêng của sách
                if (num > copy.getBook().getCopyCount()) {
                    copy.getBook().setCopyCount(num);
                }
            } catch (Exception e) {
                System.out.println("Warning: Invalid book copy ID format: " + copy.getCopyId());
            }
        }
        totalCopies = max;
    }
}
