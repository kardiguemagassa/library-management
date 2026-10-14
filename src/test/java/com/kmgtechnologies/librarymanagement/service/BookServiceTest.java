package com.kmgtechnologies.librarymanagement.service;

import com.kmgtechnologies.librarymanagement.dto.BookRequest;
import com.kmgtechnologies.librarymanagement.exception.BookNotFoundException;
import com.kmgtechnologies.librarymanagement.model.Book;
import com.kmgtechnologies.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository repository;

    @InjectMocks
    BookService service;

    Book book;
    BookRequest request;

    @BeforeEach void setUp() {
        book = new Book(1L, "Clean Code", "Robert C. Martin", 2008);
        request = new BookRequest("Clean Code", "Robert C. Martin", 2008);
    }

    @Test void getAllBooks() {
        when(repository.findAll()).thenReturn(List.of(book));
        assertEquals(List.of(book), service.getAllBooks());
    }

    @Test void getBookById() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        assertEquals(book, service.getBookById(1L));
    }

    @Test void getBookByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> service.getBookById(99L));
    }

    @Test void createBook() {
        when(repository.create("Clean Code", "Robert C. Martin", 2008)).thenReturn(book);
        assertEquals(book, service.createBook(request));
    }

    @Test void updateBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(book);
        assertEquals(book, service.updateBook(1L, request));
    }

    @Test void deleteBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        service.deleteBook(1L);
        verify(repository).deleteById(1L);
    }

}
