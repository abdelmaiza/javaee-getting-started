package com.pluralsight.bookstore.rest;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.repository.BookRepository;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by b010nsm on 21/02/2019.
 */
@Path("/books")
public class BookEndpoint {

    @Inject
    BookRepository bookRepository;

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book =  bookRepository.find(id);
        if (book == null){
            return Response.noContent().build();
        }else{
            return Response.ok(book).build();
        }

    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()){
            return Response.noContent().build();
        }else{
            return Response.ok(books).build();
        }
    }

    @GET
    @Path("/cout")
    public Response countBooks() {
        Long nbOfBooks = bookRepository.countAll();
        if(nbOfBooks == 0 ){
            return Response.noContent().build();
        }else{
            return Response.ok(nbOfBooks).build();
        }
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdURI).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }




}
