package view;

import controller.LibraryController;
import core.Model;
import core.View;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Book;

public class LibraryView extends JFrame implements View {
    private LibraryController controller;
    private JTextField titleField, authorField, yearField;
    private JComboBox<String> genreCombo;
    private JTextArea resultArea;

    public LibraryView() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setTitle("Library Management System");

       
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        titleField = new JTextField();
        authorField = new JTextField();
        yearField = new JTextField();
        genreCombo = new JComboBox<>(new String[]{"Drama", "Romántico", "Terror"});

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreCombo);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);

        
        JButton saveButton = new JButton("Save Book");
        saveButton.addActionListener(e -> controller.saveBook());
        JButton advancedSearchButton = new JButton("Búsqueda Avanzada");
        advancedSearchButton.addActionListener(e -> controller.showSearchOptions());
        JButton editButton = new JButton("Editar Libro");
        editButton.addActionListener(e -> controller.editBook());
        JButton deleteButton = new JButton("Eliminar Libro");
        deleteButton.addActionListener(e -> controller.deleteBook());

        inputPanel.add(saveButton);
        inputPanel.add(advancedSearchButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);

        
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Model model, Object data) {
        if (model instanceof Book) {
            Book book = (Book) model;
            resultArea.append(String.format("Book: %s by %s (%s, %d)\n",
                book.getTitle(), book.getAuthor(), book.getGenre(), book.getYear()));
        }
    }

    public void displaySearchResults(List<Book> books) {
        if (books.isEmpty()) {
            resultArea.setText("No se encontraron resultados.\n");
        } else {
            resultArea.setText(""); // Clear previous results
            for (Book book : books) {
                resultArea.append(String.format("Libro: %s por %s (%s, %d)\n",
                        book.getTitle(), book.getAuthor(), book.getGenre(), book.getYear()));
            }
        }
    }

    public void clearInputFields() {
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
        genreCombo.setSelectedIndex(0);
    }

    
    public String getTitle() { return titleField.getText(); }
    public String getAuthor() { return authorField.getText(); }
    public String getGenre() { return (String) genreCombo.getSelectedItem(); }
    public int getYear() { return Integer.parseInt(yearField.getText()); }
    public void setController(LibraryController controller) { this.controller = controller; }
}