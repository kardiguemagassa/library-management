package com.kmgtechnologies.librarymanagement.service;

import com.kmgtechnologies.librarymanagement.model.Book;
import com.kmgtechnologies.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public void getBookById(Long id) {
        repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book createBook(BookRequest request) {
        return repository.create(request.title(), request.author(), request.publicationYear());
    }

    public Book updateBook(Long id, BookRequest request) {
        getBookById(id);
        return repository.save(new Book(id, request.title(), request.author(), request.publicationYear()));
    }

    public void deleteBook(Long id) {
        getBookById(id); repository.deleteById(id);
    }
}
