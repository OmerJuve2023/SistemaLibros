package model;

import core.Model;
import core.View;
import java.util.ArrayList;
import java.util.List;

public class Book implements Model {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private List<View> views;
    
    public Book() {
        views = new ArrayList<>();
    }
    
    public static class BookBuilder {
        private Book book;
        
        public BookBuilder() {
            book = new Book();
        }
        
        public BookBuilder withTitle(String title) {
            book.setTitle(title);
            return this;
        }
        
        public BookBuilder withAuthor(String author) {
            book.setAuthor(author);
            return this;
        }
        
        public BookBuilder withGenre(String genre) {
            book.setGenre(genre);
            return this;
        }
        
        public BookBuilder withYear(int year) {
            book.setYear(year);
            return this;
        }
        
        public Book build() {
            return book;
        }
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    @Override
    public void attach(View view) {
        views.add(view);
    }
    
    @Override
    public void detach(View view) {
        views.remove(view);
    }
    
    @Override
    public void notifyViews() {
        for (View view : views) {
            view.update(this, null);
        }
    }
}