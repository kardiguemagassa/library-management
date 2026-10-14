package com.kmgtechnologies.librarymanagement.controller;

import com.kmgtechnologies.librarymanagement.exception.BookNotFoundException;
import com.kmgtechnologies.librarymanagement.model.Book;
import com.kmgtechnologies.librarymanagement.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BookService service;

    private static final String JSON = """
        {"title":"Clean Code","author":"Robert C. Martin","publicationYear":2008}
        """;

    @Test void getAllBooks() throws Exception {
        when(service.getAllBooks()).thenReturn(List.of(new Book(1L, "Clean Code", "Robert C. Martin", 2008)));
        mockMvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test void getBookById() throws Exception {
        when(service.getBookById(1L)).thenReturn(new Book(1L, "Clean Code", "Robert C. Martin", 2008));
        mockMvc.perform(get("/api/books/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test void getBookByIdNotFound() throws Exception {
        when(service.getBookById(99L)).thenThrow(new BookNotFoundException(99L));
        mockMvc.perform(get("/api/books/99")).andExpect(status().isNotFound());
    }

    @Test void createBook() throws Exception {
        when(service.createBook(any())).thenReturn(new Book(1L, "Clean Code", "Robert C. Martin", 2008));
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/books/1"))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void createBookInvalid() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {"title":"","author":"A","publicationYear":2000}
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test void updateBook() throws Exception {
        when(service.updateBook(eq(1L), any())).thenReturn(new Book(1L, "Clean Code", "Robert C. Martin", 2008));
        mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test void deleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1")).andExpect(status().isNoContent());
        verify(service).deleteBook(1L);
    }
}