public interface I_Management<T> {
    void add(T item);
    void remove(String id);
    T search(String id);
    void editProfile(String id);
    void show(String id);
    void showAll();
    void save();
}