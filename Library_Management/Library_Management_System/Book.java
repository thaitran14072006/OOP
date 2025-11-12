import java.util.ArrayList;
import java.util.regex.Pattern;

public class Book {
    private String id;
    private String title;
    private final ArrayList<String> categories;
    private final ArrayList<Author> authors;
    private String publicationYear;
    private static int count = 0;
    
    private ArrayList<String> authorIds;
    
    private static final Pattern TITLE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,!?()-]{1,100}$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^(19|20)\\d{2}$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,30}$");
    
    private static final int MAX_AUTHORS_PER_BOOK = 5;
    private static final int MAX_CATEGORIES_PER_BOOK = 3;
    
    public Book(String title, String publicationYear) {
        setTitle(title);
        setPublicationYear(publicationYear);
        this.categories = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.authorIds = new ArrayList<>();
        count++;
        this.id = "B" + String.format("%03d", count);
    }
    
    public Book(String id, String title, String publicationYear) {
        this.id = id;
        setTitle(title);
        setPublicationYear(publicationYear);
        this.categories = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.authorIds = new ArrayList<>();
    }
    
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        if (!TITLE_PATTERN.matcher(title).matches()) {
            throw new IllegalArgumentException("Book title must be 1-100 characters long and contain only letters, numbers, spaces, and basic punctuation");
        }
    }
    
    private void validatePublicationYear(String year) {
        if (year == null || year.trim().isEmpty()) {
            throw new IllegalArgumentException("Publication year cannot be null or empty");
        }
        if (!YEAR_PATTERN.matcher(year).matches()) {
            throw new IllegalArgumentException("Publication year must be between 1900-2099");
        }
        
        int yearValue = Integer.parseInt(year);
        int currentYear = java.time.Year.now().getValue();
        if (yearValue > currentYear) {
            throw new IllegalArgumentException("Publication year cannot be in the future");
        }
    }
    
    private void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (!CATEGORY_PATTERN.matcher(category).matches()) {
            throw new IllegalArgumentException("Category must be 2-30 characters long and contain only letters and spaces");
        }
    }
    
    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }
    
    public void setPublicationYear(String publicationYear) {
        validatePublicationYear(publicationYear);
        this.publicationYear = publicationYear;
    }
    
    public void addCategory(String category) {
        validateCategory(category);
        
        if (categories.size() >= MAX_CATEGORIES_PER_BOOK) {
            throw new IllegalStateException("Cannot add more than " + MAX_CATEGORIES_PER_BOOK + " categories to a book");
        }
        
        if (categories.contains(category)) {
            throw new IllegalArgumentException("Category '" + category + "' already exists for this book");
        }
        
        categories.add(category.trim());
    }
    
    public void addAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        
        if (authors.size() >= MAX_AUTHORS_PER_BOOK) {
            throw new IllegalStateException("Cannot add more than " + MAX_AUTHORS_PER_BOOK + " authors to a book");
        }
        
        if (authors.contains(author)) {
            throw new IllegalArgumentException("Author '" + author.getName() + "' already exists for this book");
        }
        
        authors.add(author);
    }
    
    public void linkAuthors(ArrayList<Author> allAuthors) {
        authors.clear();
        
        for (String authorId : authorIds) {
            Author author = findAuthorById(allAuthors, authorId);
            if (author != null && !authors.contains(author)) {
                authors.add(author);
            }
        }
    }
    
    private Author findAuthorById(ArrayList<Author> allAuthors, String authorId) {
        for (Author author : allAuthors) {
            if (author.getId().equals(authorId)) {
                return author;
            }
        }
        return null;
    }
    
    public boolean removeCategory(String category) {
        return categories.remove(category);
    }
    
    public boolean removeAuthor(Author author) {
        return authors.remove(author);
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public ArrayList<String> getCategories() { return new ArrayList<>(categories); }
    public ArrayList<Author> getAuthors() { return new ArrayList<>(authors); }
    public String getPublicationYear() { return publicationYear; }
    public static int getTotal() { return count; }
    
    public ArrayList<String> getAuthorIds() { return new ArrayList<>(authorIds); }
    public void setAuthorIds(ArrayList<String> authorIds) { 
        this.authorIds = new ArrayList<>(authorIds); 
    }
    public void addAuthorId(String authorId) {
        if (authorId != null && !authorId.trim().isEmpty()) {
            this.authorIds.add(authorId.trim());
        }
    }
    
    public void setBookId() {
        this.id = "B" + String.format("%03d", count);
    }
    
    public static void updateCountFromFile(ArrayList<Book> books) {
        int max = 0;
        for (Book b : books) {
            try {
                String idNum = b.getId().substring(1);
                int num = Integer.parseInt(idNum);
                if (num > max) max = num;
            } catch (NumberFormatException e) {
                System.out.println("Warning: Invalid book ID format: " + b.getId());
            }
        }
        count = max;
    }
    
    public void showInfo() {
        System.out.println("ID: " + getId());
        System.out.println("Title: " + getTitle());
        System.out.println("Categories: ");
        for (String c : categories) {
            System.out.println(" - " + c);
        }
        System.out.println("Authors: ");
        if (authors.isEmpty()) {
            System.out.println(" - No authors linked");
        } else {
            for (Author au : authors) {
                au.showInfo();
            }
        }
        System.out.println("Publication year: " + getPublicationYear());
        System.out.println();
    }
    // Trong class Book
    private int copyCount = 0;

    public void incrementCopyCount() {
        copyCount++;
    }

    public void setCopyCount(int count) {
        this.copyCount = count;
    }

    public int getCopyCount() {
        return copyCount;
    }

}