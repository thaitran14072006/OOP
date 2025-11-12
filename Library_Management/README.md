# Library Management System

## Mô tả hệ thống
Hệ thống quản lý thư viện với đầy đủ các chức năng: quản lý sách, tác giả, độc giả, mượn/trả sách, và báo cáo thống kê.

## Cấu trúc file dữ liệu

### 1. books.txt
Định dạng:
BookID|Title|PublicationYear
CATEGORIES:Category1,Category2,...
AUTHORS:AuthorID1,AuthorID2,...

text

Ví dụ:
B001|To Kill a Mockingbird|1960
CATEGORIES:Fiction,Classic
AUTHORS:A001

text

### 2. authors.txt
Định dạng:
AuthorID|Name|BirthDate|PenName|Email|PhoneNumber

text

Ví dụ:
A001|Harper Lee|1926-04-28|Harper Lee|harper.lee@email.com|0123456789

text

### 3. readers.txt
Định dạng:
ReaderID|Name|BirthDate|PhoneNumber|Email|Address

text

Ví dụ:
R001|Nguyen Van A|1990-05-15|0912345678|nguyen.a@email.com|123 Nguyen Hue, District 1, HCMC

text

### 4. book_copies.txt
Định dạng:
CopyID|BookID|IsAvailable

text

Ví dụ:
B001-001|B001|true

text

### 5. borrow_slips.txt
Định dạng:
BorrowID|BorrowDate|ReaderID|CopyID1,CopyID2,...

text

Ví dụ:
Bor001|2024-01-15|R001|B001-003,B002-002

text

## Quy tắc validation

### Sách (Book)
- Tiêu đề: 1-100 ký tự, chỉ chứa chữ cái, số, khoảng trắng và dấu câu cơ bản
- Năm xuất bản: 1900-2099, không được là năm tương lai
- Tối đa 3 thể loại/sách
- Tối đa 5 tác giả/sách

### Tác giả (Author)
- Tên: 2-50 ký tự, chỉ chữ cái và khoảng trắng
- Email: định dạng email hợp lệ
- Số điện thoại: 10-11 chữ số
- Bút danh: 2-30 ký tự
- Tuổi: 5-120 tuổi

### Độc giả (Reader)
- Tên: 2-50 ký tự, chỉ chữ cái và khoảng trắng
- Email: định dạng email hợp lệ
- Số điện thoại: 10-11 chữ số
- Địa chỉ: 5-100 ký tự
- Tuổi: 12-120 tuổi

### Phiếu mượn (BorrowSlip)
- Tối đa 5 sách/độc giả
- Thời hạn mượn: 14 ngày
- Phí trễ: 5,000 VND/ngày

## Cách chạy chương trình

### Chương trình chính
```bash
javac *.java
java LibraryApp
Demo với dữ liệu mẫu
bash
java DemoApp
Test nhanh
bash
java QuickTest
Menu chính
Quản lý sách - Thêm, xóa, sửa, tìm kiếm sách

Quản lý tác giả - Thêm, xóa, sửa, tìm kiếm tác giả

Quản lý độc giả - Thêm, xóa, sửa, tìm kiếm độc giả

Quản lý mượn/trả - Tạo phiếu mượn, thêm sách, trả sách

Báo cáo thống kê - Thống kê tổng quan, sách quá hạn, sách có sẵn

Quản lý dữ liệu - Lưu dữ liệu, tạo backup, kiểm tra tính nhất quán

Backup
Hệ thống tự động tạo backup trong thư mục backup_timestamp khi người dùng yêu cầu.

text

**7. SAMPLE_DATA_SETUP.md** - Hướng dẫn thiết lập dữ liệu mẫu
```markdown
# Hướng dẫn thiết lập dữ liệu mẫu

## Bước 1: Tạo file authors.txt
A001|Harper Lee|1926-04-28|Harper Lee|harper.lee@email.com|0123456789
A002|George Orwell|1903-06-25|George Orwell|george.orwell@email.com|0123456790
A003|F. Scott Fitzgerald|1896-09-24|F. Scott Fitzgerald|fitzgerald@email.com|0123456791
A004|Jane Austen|1775-12-16|Jane Austen|jane.austen@email.com|0123456792
A005|J.R.R. Tolkien|1892-01-03|J.R.R. Tolkien|tolkien@email.com|0123456793

text

## Bước 2: Tạo file books.txt
B001|To Kill a Mockingbird|1960
CATEGORIES:Fiction,Classic
AUTHORS:A001

B002|1984|1949
CATEGORIES:Science Fiction,Dystopian
AUTHORS:A002

B003|The Great Gatsby|1925
CATEGORIES:Fiction,Classic
AUTHORS:A003

B004|Pride and Prejudice|1813
CATEGORIES:Romance,Classic
AUTHORS:A004

B005|The Hobbit|1937
CATEGORIES:Fantasy,Adventure
AUTHORS:A005

text

## Bước 3: Tạo file readers.txt
R001|Nguyen Van A|1990-05-15|0912345678|nguyen.a@email.com|123 Nguyen Hue, District 1, HCMC
R002|Tran Thi B|1985-08-22|0912345679|tran.b@email.com|456 Le Loi, District 3, HCMC
R003|Le Van C|1995-03-10|0912345680|le.c@email.com|789 Pasteur, District 1, HCMC

text

## Bước 4: Tạo file book_copies.txt
B001-001|B001|true
B001-002|B001|true
B002-001|B002|true
B003-001|B003|true
B004-001|B004|true
B005-001|B005|true
B005-002|B005|true

text

## Bước 5: Tạo file borrow_slips.txt (tùy chọn)
Bor001|2024-01-15|R001|B001-001
Bor002|2024-01-20|R002|B005-001

text

## Bước 6: Chạy chương trình
```bash
javac *.java
java LibraryApp
Hệ thống sẽ tự động đọc các file dữ liệu và khởi tạo hệ thống.

text

**8. VALIDATION_RULES.md** - Tài liệu quy tắc validation chi tiết
```markdown
# Quy tắc Validation chi tiết

## 1. Định dạng ID
- **Sách**: Bxxx (B001, B002, ...)
- **Tác giả**: Axxx (A001, A002, ...)  
- **Độc giả**: Rxxx (R001, R002, ...)
- **Phiếu mượn**: Borxxx (Bor001, Bor002, ...)
- **Bản sao sách**: BookID-xxx (B001-001, B001-002, ...)

## 2. Quy tắc tên
- **Độ dài**: 2-50 ký tự
- **Ký tự cho phép**: Chữ cái (a-z, A-Z), khoảng trắng
- **Không cho phép**: Số, ký tự đặc biệt (trừ khoảng trắng)

## 3. Quy tắc email
- **Định dạng**: local-part@domain
- **Local-part**: Chữ cái, số, +, _, -, .
- **Domain**: Ít nhất 2 ký tự, chứa dấu chấm
- **Ví dụ hợp lệ**: user.name@domain.com, user+tag@domain.co.uk
- **Ví dụ không hợp lệ**: user@, @domain.com, user@domain

## 4. Quy tắc số điện thoại
- **Độ dài**: 10-11 chữ số
- **Định dạng**: Chỉ chứa số (0-9)
- **Tự động làm sạch**: Xóa khoảng trắng, dấu gạch ngang
- **Ví dụ**: 0912345678 → 0912345678, 09-123-456-78 → 0912345678

## 5. Quy tắc ngày tháng
- **Định dạng**: YYYY-MM-DD
- **Ngày sinh**: Không được là tương lai, tuổi từ 5-120
- **Ngày mượn**: Không được là tương lai, không quá 1 năm trước
- **Độc giả**: Tối thiểu 12 tuổi

## 6. Giới hạn số lượng
- **Thể loại/sách**: Tối đa 3
- **Tác giả/sách**: Tối đa 5
- **Sách/phiếu mượn**: Tối đa 5
- **Bản sao/sách**: Tối đa 50

## 7. Quy tắc duy nhất
- **Email tác giả**: Không trùng lặp
- **Email độc giả**: Không trùng lặp
- **Tiêu đề sách + năm**: Không trùng lặp
- **Thể loại trong sách**: Không trùng lặp
- **Tác giả trong sách**: Không trùng lặp

## 8. Xử lý lỗi
- **Lỗi validation**: Hiển thị thông báo cụ thể
- **Lỗi file I/O**: Hiển thị thông báo và tiếp tục hoạt động
- **Lỗi tính nhất quán**: Kiểm tra và báo cáo khi khởi động
- **Backup tự động**: Khi lưu dữ liệu quan trọng
9. USER_MANUAL.md - Hướng dẫn sử dụng cho người dùng cuối

markdown
# Hướng dẫn sử dụng hệ thống quản lý thư viện

## Đăng nhập và khởi động
1. Chạy file `LibraryApp.java`
2. Hệ thống tự động tải dữ liệu từ các file
3. Menu chính sẽ hiển thị

## Quản lý sách

### Thêm sách mới
1. Chọn "Book Management" → "Add new book"
2. Nhập thông tin:
   - Tiêu đề sách
   - Năm xuất bản
3. Thêm thể loại (tối đa 3)
4. Thêm tác giả (tác giả phải tồn tại trong hệ thống)

### Tìm kiếm sách
1. Chọn "Book Management" → "Search book"
2. Nhập ID sách hoặc tìm theo tên
3. Xem thông tin chi tiết và số bản sao có sẵn

## Quản lý tác giả

### Thêm tác giả mới
1. Chọn "Author Management" → "Add new author"
2. Nhập đầy đủ thông tin:
   - Tên thật
   - Ngày sinh
   - Bút danh
   - Email (không trùng)
   - Số điện thoại

## Quản lý độc giả

### Đăng ký độc giả mới
1. Chọn "Reader Management" → "Add new reader"
2. Nhập thông tin:
   - Tên độc giả
   - Ngày sinh (tối thiểu 12 tuổi)
   - Số điện thoại
   - Email (không trùng)
   - Địa chỉ

## Mượn và trả sách

### Tạo phiếu mượn
1. Chọn "Borrow/Return Management" → "Create new borrow slip"
2. Chọn độc giả (theo ID)
3. Nhập ngày mượn
4. Thêm sách vào phiếu (tối đa 5 sách)

### Trả sách
1. Chọn "Borrow/Return Management" → "Return book"
2. Nhập ID phiếu mượn
3. Chọn sách cần trả

## Báo cáo và thống kê

### Xem thống kê tổng quan
1. Chọn "Reports and Statistics" → "Library statistics"
2. Xem:
   - Tổng số sách, tác giả, độc giả
   - Số sách đang được mượn
   - Số phiếu mượn quá hạn
   - Tỷ lệ sách có sẵn

### Báo cáo sách quá hạn
1. Chọn "Reports and Statistics" → "Overdue books report"
2. Xem danh sách sách quá hạn và phí phạt

## Quản lý dữ liệu

### Lưu dữ liệu
- Hệ thống tự động lưu khi thoát
- Có thể lưu thủ công: "Data Management" → "Save all data"

### Tạo backup
1. Chọn "Data Management" → "Create backup"
2. Backup được lưu trong thư mục `backup_timestamp`

### Kiểm tra tính nhất quán
1. Chọn "Data Management" → "Validate data consistency"
2. Hệ thống kiểm tra và báo cáo lỗi nếu có

## Mẹo sử dụng
- Luôn sử dụng ID để tìm kiếm chính xác
- Kiểm tra tính khả dụng của sách trước khi cho mượn
- Theo dõi ngày hết hạn để nhắc nhở độc giả
- Tạo backup định kỳ để đảm bảo an toàn dữ liệu