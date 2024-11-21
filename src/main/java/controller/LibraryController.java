package controller;

import core.Controller;
import javax.swing.JOptionPane;
import model.Book;
import service.BookService;
import view.LibraryView;
import java.util.List;

public class LibraryController extends Controller {
    private BookService service;
    private LibraryView view;
    
    public LibraryController() {
        service = new BookService();
        view = new LibraryView();
        view.setController(this);
    }
    
    @Override
    public void run() {
        view.setVisible(true);
    }
    
    public void saveBook() {
        try {
            Book book = new Book.BookBuilder()
                .withTitle(view.getTitle())
                .withAuthor(view.getAuthor())
                .withGenre(view.getGenre())
                .withYear(view.getYear())
                .build();
                
            service.addBook(book);
            book.attach(view);
            book.notifyViews();
            view.clearInputFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
        }
    }
    
    public void showSearchOptions() {
        String[] options = {"Buscar por Título", "Buscar por Autor", "Buscar por Género"};
        int choice = JOptionPane.showOptionDialog(view,
                "Seleccione el criterio de búsqueda",
                "Opciones de Búsqueda",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) { 
            String title = JOptionPane.showInputDialog(view, "Ingrese el título del libro:");
            if (title != null && !title.trim().isEmpty()) {
                searchByTitle(title);
            }
        } else if (choice == 1) { 
            String author = JOptionPane.showInputDialog(view, "Ingrese el autor del libro:");
            if (author != null && !author.trim().isEmpty()) {
                searchByAuthor(author);
            }
        } else if (choice == 2) {
            String genre = JOptionPane.showInputDialog(view, "Ingrese el género del libro:");
            if (genre != null && !genre.trim().isEmpty()) {
                searchByGenre(genre);
            }
        }
    }

    private void searchByTitle(String title) {
        service.searchByTitle(title).forEach(book -> {
            book.attach(view);
            book.notifyViews();
        });
    }

    private void searchByAuthor(String author) {
        service.searchByAuthor(author).forEach(book -> {
            book.attach(view);
            book.notifyViews();
        });
    }

    private void searchByGenre(String genre) {
        try {
            List<Book> books = service.findByGenere(genre);
            view.displaySearchResults(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
        }
    }
    
    public void editBook() {
        String idInput = JOptionPane.showInputDialog(view, "Ingrese el ID del libro a editar:");
        if (idInput != null && !idInput.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idInput);
                Book book = service.findById(id);
                if (book != null) {
                    String newTitle = JOptionPane.showInputDialog(view, "Nuevo título:", book.getTitle());
                    String newAuthor = JOptionPane.showInputDialog(view, "Nuevo autor:", book.getAuthor());
                    String newGenre = JOptionPane.showInputDialog(view, "Nuevo género:", book.getGenre());
                    int newYear = Integer.parseInt(JOptionPane.showInputDialog(view, "Nuevo año:", book.getYear()));

                    book.setTitle(newTitle);
                    book.setAuthor(newAuthor);
                    book.setGenre(newGenre);
                    book.setYear(newYear);

                    service.update(book);
                    book.attach(view);
                    book.notifyViews();
                    JOptionPane.showMessageDialog(view, "Libro actualizado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(view, "Libro no encontrado.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
            }
        }
    }

    public void deleteBook() {
        String idInput = JOptionPane.showInputDialog(view, "Ingrese el ID del libro a eliminar:");
        if (idInput != null && !idInput.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idInput);
                boolean success = service.delete(id);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Libro eliminado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(view, "Libro no encontrado.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
            }
        }
    }
}