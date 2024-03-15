package demo.component.repository;

import demo.repository.ItemRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.H2)
@Replaces(ItemRepository.class)
public interface TestItemRepository extends ItemRepository {
}
