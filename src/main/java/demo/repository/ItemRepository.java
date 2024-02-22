package demo.repository;

import java.util.UUID;

import demo.domain.Item;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
}
