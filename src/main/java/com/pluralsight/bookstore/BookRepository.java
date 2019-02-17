package com.pluralsight.bookstore;

import com.pluralsight.bookstore.model.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public class BookRepository {

    @PersistenceContext(unitName = "bookStorePU")
    private EntityManager entityManager;

    public Book find(Long id){
        return entityManager.find(Book.class, id);
    }

    public List<Book> findAll(){
        TypedQuery<Book> bookTypedQuery = entityManager.createQuery("SELECT b from Book b order by b.title desc ", Book.class );
        return bookTypedQuery.getResultList();
    }

    public Long countAll(){
        TypedQuery<Long> longTypedQuery = entityManager.createQuery("select count (b) from Book b" , Long.class);
        return longTypedQuery.getSingleResult();
    }

    @Transactional(REQUIRED)
    public Book create(Book book){
        entityManager.persist(book);
        return book;
    }
    @Transactional(REQUIRED)
    public void delete(Long id){
        entityManager.remove(entityManager.getReference(Book.class, id));
    }


}
