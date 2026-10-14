
package com.kmgtechnologies.librarymanagement.repository;

import com.kmgtechnologies.librarymanagement.model.Book;
import com.kmgtechnologies.librarymanagement.persistence.BookCsvStorage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookRepository {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    private final BookCsvStorage storage;

    // 1. Charger les livres au démarrage
    public BookRepository(BookCsvStorage storage) {
        this.storage = storage;

        for (Book book : storage.load()) {
            books.put(book.id(), book);

            sequence.updateAndGet(current ->
                    Math.max(current, book.id())
            );
        }
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    // 2. Créer un livre et sauvegarder le CSV
    public synchronized Book create(String title, String author, int year) {
        long id = sequence.incrementAndGet();

        Book book = new Book(id, title, author, year);
        books.put(id, book);

        storage.save(findAll());

        return book;
    }

    // 3. Modifier un livre et sauvegarder le CSV
    public synchronized Book save(Book book) {
        books.put(book.id(), book);

        sequence.updateAndGet(current ->
                Math.max(current, book.id())
        );

        storage.save(findAll());

        return book;
    }

    // 4. Supprimer un livre et sauvegarder le CSV
    public synchronized void deleteById(Long id) {
        books.remove(id);

        storage.save(findAll());
    }
}
