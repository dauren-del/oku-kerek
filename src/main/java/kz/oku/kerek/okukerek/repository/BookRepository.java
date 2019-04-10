package kz.oku.kerek.okukerek.repository;

import kz.oku.kerek.okukerek.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * This class provides data access to books
 *
 * @author Dauren Delmukhambetov
 * @since 0.1.0
 */
public interface BookRepository extends CrudRepository<BookEntity, Long>{
}
