package com.parent_child.demo.repositories;

import com.parent_child.demo.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a JOIN FETCH a.books")
    List<Author> findAllWithBooks();

    @Query(value = """
            SELECT
                a.id,
                a.name,
                a.bio,
                jsonb_agg(
                    jsonb_build_object(
                        'id', b.id,
                        'title', b.title,
                        'isbn', b.isbn,
                        'publishedYear', b.published_year
                    )
                ) AS books
            FROM authors a
            JOIN books b ON b.author_id = a.id
            GROUP BY a.id;
        """, nativeQuery = true)
    List<Object[]> findAllWithBooksAsJson();
}
