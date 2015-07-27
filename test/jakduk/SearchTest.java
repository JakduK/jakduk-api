package jakduk;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.repo2.Article;
import com.jakduk.repo2.Book;
import com.jakduk.repo2.SampleArticleRepository;
import com.jakduk.repo2.SampleBookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class SearchTest {
	
    @Resource
    private SampleBookRepository repository;
    
    @Resource
    private SampleArticleRepository sampleArticleRepository;
	
    @Test
    public void shouldIndexSingleBookEntity(){

        Book book = new Book();
        book.setId("123455");
        book.setName("Spring Data Elasticsearch");
        book.setVersion(System.currentTimeMillis());
        repository.save(book);
        //lets try to search same record in elasticsearch
        Book indexedBook = repository.findOne(book.getId());
        
        System.out.println("indexedBook=" + indexedBook);
        
        assertThat(indexedBook,is(notNullValue()));
        assertThat(indexedBook.getId(),is(book.getId()));
    }
    
    @Test
    public void shouldIndexSingleBookEntity2(){

        Article article = new Article();
        article.setId("123455");
        article.setTitle("Spring Data Elasticsearch Test Article");
        List<String> authors = new ArrayList<String>();
        authors.add("Author1");
        authors.add("Author2");
        article.setAuthors(authors);
        List<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        article.setTags(tags);
        //Indexing using sampleArticleRepository
        sampleArticleRepository.save(article);
        //lets try to search same record in elasticsearch
        Article indexedArticle = sampleArticleRepository.findOne(article.getId());
        System.out.println("indexedArticle=" + indexedArticle);
        assertThat(indexedArticle,is(notNullValue()));
        assertThat(indexedArticle.getId(),is(article.getId()));
        assertThat(indexedArticle.getAuthors().size(),is(authors.size()));
        assertThat(indexedArticle.getTags().size(),is(tags.size()));
    }

}
