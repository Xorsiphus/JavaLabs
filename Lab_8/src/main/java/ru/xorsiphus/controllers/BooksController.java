package ru.xorsiphus.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.xorsiphus.dao.services.BookServiceImpl;
import ru.xorsiphus.entity.Book;
import ru.xorsiphus.parser.forms.FilterAuthor;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController
{
    private final BookServiceImpl bookService;

    public BooksController(@Qualifier("bookService") BookServiceImpl bookService, JmsTemplate jmsTemplate)
    {
        this.bookService = bookService;
        this.jmsTemplate = jmsTemplate;
    }

    private final JmsTemplate jmsTemplate;

    @GetMapping
    public String viewAll(Model model)
    {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("count", bookService.count());
        model.addAttribute("filterAuthor", new FilterAuthor());
        return "books/all";
    }

    @RequestMapping(method=RequestMethod.GET, headers =
            {"Accept=application/json"})
    public @ResponseBody ResponseEntity<List<Book>> viewAll()
    {
        return new ResponseEntity<List<Book>>(bookService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET, headers =
            {"Accept=application/json"})
    public @ResponseBody Book getBook(@PathVariable("id") int id)
    {
        return bookService.findById(id).orElse(new Book());
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET, headers = {"Accept=text/html"})
            public String getBook(@PathVariable("id") int id, Model model)
    {
        model.addAttribute(bookService.findById(id).orElse(new Book()));
        return "books/single";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.insert(book);
        jmsTemplate.convertAndSend("Add Notification", "New book added: " + book.toString());
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") Book book)
    {
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("book", bookService.findById(id).orElse(new Book()));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id)
    {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.updateById(id, book);
        jmsTemplate.convertAndSend("Edit Notification", "Book " + id + " updated: " + book.toString());
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id)
    {
        bookService.removeById(id);
        jmsTemplate.convertAndSend("Delete Notification", "Book " + id + " deleted!");
        return "redirect:/books";
    }

    @PostMapping("/filtered")
    public String viewByFilter(@ModelAttribute("filterAuthor") @Valid FilterAuthor filterAuthor, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("books", bookService.findAll());
            model.addAttribute("count", bookService.count());
            return "books/all";
        }

        model.addAttribute("books", bookService.findByAuthor(filterAuthor.getFilter()));
        return "books/filtered";
    }

}
