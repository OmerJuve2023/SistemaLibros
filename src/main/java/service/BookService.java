package service;

import java.sql.SQLException;
import java.util.List;
import model.Book;
import repository.BookRepository;

public class BookService {
    private BookRepository repository;
    
    public BookService() {
        repository = new BookRepository();
    }
    
    public void addBook(Book book) {
        if (!isValidGenre(book.getGenre())) {
            throw new IllegalArgumentException("Invalid genre. Allowed genres: Drama, Romántico, Terror");
        }
        repository.save(book);
    }
    
    public List<Book> searchByTitle(String title) {
        return repository.findByTitle(title);
    }
    
    public List<Book> searchByAuthor(String author) {
        return repository.findByAuthor(author);
    }
    
    public boolean update (Book book){
       return repository.update(book);
    }
    
    public boolean delete (int id){
        return repository.delete(id);
    }
    public List<Book> findALl() throws SQLException{
        return repository.findAll();
    }
    
    public List<Book> findByGenere(String genre) throws SQLException{
        return repository.findByGenre(genre);
    }
    public Book findById(int id) {
        return repository.findById(id);
    }
    private boolean isValidGenre(String genre) {
        return genre.equals("Drama") || 
               genre.equals("Romántico") || 
               genre.equals("Terror");
    }
}
