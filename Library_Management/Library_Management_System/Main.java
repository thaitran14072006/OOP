public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Library System!");

        BookList b = new BookList();
        ReaderList r = new ReaderList();
        // Ví dụ khởi tạo danh sách sách:
        BorrowSlipList al = new BorrowSlipList(b, r.getReadersMap());
        b.showAll();
    }
}
