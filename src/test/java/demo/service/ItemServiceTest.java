package demo.service;

import java.util.Optional;
import java.util.UUID;

import demo.domain.Item;
import demo.exception.ItemNotFoundException;
import demo.repository.ItemRepository;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import demo.util.TestDomainData;
import demo.util.TestRestData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ItemService(itemRepository);
    }

    @Test
    public void testCreateItem() {
        CreateItemRequest request = TestRestData.buildCreateItemRequest(RandomStringUtils.randomAlphabetic(8));
        UUID itemId = UUID.randomUUID();
        when(itemRepository.save(any(Item.class))).thenReturn(TestDomainData.buildItem(itemId, request.name()));

        UUID createdItemId = service.createItem(request);

        assertThat(createdItemId, equalTo(itemId));
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testUpdateItem() {
        UUID itemId = randomUUID();
        UpdateItemRequest request = TestRestData.buildUpdateItemRequest(randomAlphabetic(8));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(TestDomainData.buildItem(itemId, request.name())));

        service.updateItem(itemId, request);

        verify(itemRepository, times(1)).save(any(Item.class));

    }

    @Test
    public void testUpdateItem_NotFound() {
        UUID itemId = randomUUID();
        UpdateItemRequest updateRequest = TestRestData.buildUpdateItemRequest(randomAlphabetic(8));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.updateItem(itemId, updateRequest));
    }

    @Test
    public void testGetItem() {
        UUID itemId = randomUUID();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(TestDomainData.buildItem(itemId, "test-item")));

        GetItemResponse item = service.getItem(itemId);

        assertThat(item.id(), equalTo(itemId));
        assertThat(item.name(), equalTo("test-item"));
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    public void testGetItem_NotFound() {
        UUID itemId = randomUUID();
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.getItem(itemId));
    }

    @Test
    public void testDeleteItem() {
        UUID itemId = randomUUID();
        Item item = TestDomainData.buildItem(itemId, "test-item");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        service.deleteItem(itemId);

        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    public void testDeleteItem_NotFound() {
        UUID itemId = randomUUID();
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.deleteItem(itemId));
    }
}
