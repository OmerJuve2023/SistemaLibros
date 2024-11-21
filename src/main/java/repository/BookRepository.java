package repository;

import config.Mysql;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Book;

public class BookRepository {
   
   private final  Mysql mysql=new Mysql();
   Connection connection = mysql.getConnection();
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, genre, year) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getYear());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        book.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving book", e);
        }
    }
    
    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, genre, year FROM books WHERE title LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + title + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding books by title", e);
        }
        return books;
    }
    
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, genre, year FROM books WHERE author LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + author + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding books by author", e);
        }
        return books;
    }
    
      public Book findById(int id) {
        Book book = null;
        String query = "SELECT * FROM books WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            
            if (resultSet.next()) {
                book = new Book.BookBuilder()
                        .withTitle(resultSet.getString("title"))
                        .withAuthor(resultSet.getString("author"))
                        .withGenre(resultSet.getString("genre"))
                        .withYear(resultSet.getInt("year"))
                        .build();
                book.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return book;
    }
    
    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, year = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getYear());
            stmt.setInt(5, book.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book", e);
        }
    }
    
     public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books"; 

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Book book = new Book.BookBuilder()
                        .withTitle(resultSet.getString("title"))
                        .withAuthor(resultSet.getString("author"))
                        .withGenre(resultSet.getString("genre"))
                        .withYear(resultSet.getInt("year"))
                        .build();
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
        }
        return books;
    }
    
       public List<Book> findByGenre(String genre) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE genre = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book.BookBuilder()
                            .withTitle(resultSet.getString("title"))
                            .withAuthor(resultSet.getString("author"))
                            .withGenre(resultSet.getString("genre"))
                            .withYear(resultSet.getInt("year"))
                            .build();
                    book.setId(resultSet.getInt("id"));
                    books.add(book);
                }
            }
        }
        return books;
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book", e);
        }
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book.BookBuilder()
            .withTitle(rs.getString("title"))
            .withAuthor(rs.getString("author"))
            .withGenre(rs.getString("genre"))
            .withYear(rs.getInt("year"))
            .build();
        book.setId(rs.getInt("id"));
        return book;
    }
    
    public static void main(String[] args) throws SQLException {
        
        // Crear una instancia del repositorio de libros
        BookRepository bookRepository = new BookRepository();
        
        // Obtener todos los libros
            List<Book> allBooks = bookRepository.findAll();
            System.out.println("Todos los libros:");
            for (Book book : allBooks) {
                System.out.println(book.getTitle());
            }
        
        // Obtener libros filtrados por género
            String genre = "Fantasy"; // Ejemplo de género
            List<Book> booksByGenre = bookRepository.findByGenre(genre);
            System.out.println("\nLibros de género " + genre + ":");
            for (Book book : booksByGenre) {
                System.out.println(book.getTitle());
            }
            
        // Crear un nuevo libro y guardarlo
        Book book1 = new Book.BookBuilder()
                .withTitle("The Catcher in the Rye")
                .withAuthor("J.D. Salinger")
                .withGenre("Fiction")
                .withYear(1951)
                .build();
        
        // Guardar el libro en la base de datos
        bookRepository.save(book1);
        System.out.println("Book saved: " + book1.getTitle());
        
        // Buscar libros por título
        System.out.println("\nSearching for books with title 'The Catcher':");
        List<Book> booksByTitle = bookRepository.findByTitle("The Catcher");
        for (Book book : booksByTitle) {
            System.out.println("Found book: " + book.getTitle() + " by " + book.getAuthor());
        }

        // Buscar libros por autor
        System.out.println("\nSearching for books by author 'J.D. Salinger':");
        List<Book> booksByAuthor = bookRepository.findByAuthor("J.D. Salinger");
        for (Book book : booksByAuthor) {
            System.out.println("Found book: " + book.getTitle() + " by " + book.getAuthor());
        }

        // Actualizar un libro
        book1.setTitle("The Catcher in the Rye (Updated)");
        boolean updated = bookRepository.update(book1);
        System.out.println("\nBook updated: " + updated);

        // Eliminar un libro
        boolean deleted = bookRepository.delete(book1.getId());
        System.out.println("\nBook deleted: " + deleted);
    }
    
}

