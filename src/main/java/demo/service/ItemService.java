package demo.service;

import java.util.UUID;

import demo.domain.Item;
import demo.exception.ItemNotFoundException;
import demo.repository.ItemRepository;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Singleton
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public UUID createItem(CreateItemRequest request) {
        Item item = Item.builder().name(request.name()).build();
        item = itemRepository.save(item);
        log.info("Item created with id: {}", item.getId());
        return item.getId();
    }

    public GetItemResponse getItem(UUID itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id: {} not found", itemId);
                    return new ItemNotFoundException();
                });
        log.info("Found item with id: {}", itemId);
        return new GetItemResponse(item.getId(), item.getName());
    }

    public void updateItem(UUID itemId, UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id: {} not found", itemId);
                    return new ItemNotFoundException();
                });
        log.info("Found item with id: " + itemId);
        item.setName(request.name());
        itemRepository.save(item);
        log.info("Item updated with id: {} - name: {}", itemId, request.name());
    }

    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id: {} not found", itemId);
                    return new ItemNotFoundException();
                });
        itemRepository.delete(item);
        log.info("Deleted item with id: {}", itemId);
    }
}
