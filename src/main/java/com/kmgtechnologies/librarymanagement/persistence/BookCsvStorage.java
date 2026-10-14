package com.kmgtechnologies.librarymanagement.persistence;

import com.kmgtechnologies.librarymanagement.model.Book;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import java.nio.file.Path;
import java.util.List;

@Component
public class BookCsvStorage {

    private final Path file = Path.of("data", "books.csv");

    public List<Book> load() {
        try {
            if (Files.notExists(file)) {
                return List.of();
            }

            List<String> lines = Files.readAllLines(file);
            List<Book> books = new ArrayList<>();

            // On commence à 1 pour ignorer l'en-tête CSV
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length != 4) {
                    throw new IllegalStateException("Ligne CSV invalide : " + (i + 1));
                }

                Book book = new Book(Long.parseLong(values[0]), values[1], values[2], Integer.parseInt(values[3]));
                books.add(book);
            }

            return books;

        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire les livres depuis le CSV", e);
        }
    }

    public void save(List<Book> books) {
        try {
            Files.createDirectories(file.getParent());

            List<String> lines = new ArrayList<>();
            lines.add("id,title,author,publicationYear");

            for (Book book : books) {
                lines.add(book.id() + "," + book.title() + "," + book.author() + "," + book.publicationYear());
            }

            Files.write(file, lines);

        } catch (IOException e) {
            throw new IllegalStateException("Impossible de sauvegarder les livres dans le CSV", e);
        }
    }

}