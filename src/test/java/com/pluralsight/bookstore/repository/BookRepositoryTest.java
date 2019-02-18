
package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    BookRepository bookRepository;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Book.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addClass(Language.class)
                .addClass(BookRepository.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    @Test(expected = Exception.class)
    public void createInvalidBook(){
        Book book = new Book("isbn",null,12F,123,Language.FRENCH,new Date(),"http://blablabla" ,"description");
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    public void findWithInvalidId(){
        bookRepository.find(null);
    }

    @Test
    public void create() throws Exception {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0,bookRepository.findAll().size());
        Book book = new Book("isbn","a   titile",12F,123,Language.FRENCH,new Date(),"http://blablabla" ,"description");
        book = bookRepository.create(book);
        Long id = book.getId();
        assertNotNull(id);

        Book bookFound = bookRepository.find(id);
        assertNotNull(bookRepository.find(id));
        assertEquals("a titile",bookFound.getTitle());

        assertTrue(bookFound.getIsbn().startsWith("13"));

        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1,bookRepository.findAll().size());

        bookRepository.delete(id);
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0,bookRepository.findAll().size());
    }
}