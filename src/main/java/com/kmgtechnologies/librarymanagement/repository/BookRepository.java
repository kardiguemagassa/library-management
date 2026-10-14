package com.kmgtechnologies.librarymanagement.repository;

import com.kmgtechnologies.librarymanagement.model.Book;
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

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book create(String title, String author, int year) {
        long id = sequence.incrementAndGet();
        Book book = new Book(id, title, author, year);
        books.put(id, book);
        return book;
    }

    public Book save(Book book) { books.put(book.id(), book);
        return book;
    }

    public void deleteById(Long id) {
        books.remove(id);
    }
}
