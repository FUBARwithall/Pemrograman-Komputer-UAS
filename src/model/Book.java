package model;

public class Book<T> {
    private T id;
    private String title;
    private String author;

    public Book(T id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public T getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
}
