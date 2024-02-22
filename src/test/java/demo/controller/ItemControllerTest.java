package demo.controller;

import java.util.UUID;

import demo.exception.ItemNotFoundException;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import demo.service.ItemService;
import demo.util.TestRestData;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Mock
    private ItemService serviceMock;
    private ItemController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ItemController(serviceMock);
    }

    @Test
    public void testCreateItem_Success() {
        UUID itemId = randomUUID();
        CreateItemRequest request = TestRestData.buildCreateItemRequest(RandomStringUtils.randomAlphabetic(8));
        when(serviceMock.createItem(request)).thenReturn(itemId);
        HttpResponse response = controller.createItem(request);
        assertThat(response.status(), equalTo(HttpStatus.CREATED));
        assertThat(response.header("Location"), equalTo(itemId.toString()));
        verify(serviceMock, times(1)).createItem(request);
    }

    @Test
    public void testCreateItem_ServiceThrowsException() {
        CreateItemRequest request = TestRestData.buildCreateItemRequest(RandomStringUtils.randomAlphabetic(8));
        doThrow(new RuntimeException("Service failure")).when(serviceMock).createItem(request);
        HttpResponse response = controller.createItem(request);
        assertThat(response.status(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(serviceMock, times(1)).createItem(request);
    }

    @Test
    public void testGetItem_Success() {
        UUID itemId = randomUUID();
        GetItemResponse getItemResponse = TestRestData.buildGetItemResponse(itemId, "test-item");
        when(serviceMock.getItem(itemId)).thenReturn(getItemResponse);
        HttpResponse<GetItemResponse> response = controller.getItem(itemId);
        assertThat(response.status(), equalTo(HttpStatus.OK));
        assertThat(response.body().id(), equalTo(itemId));
        assertThat(response.body().name(), equalTo("test-item"));
        verify(serviceMock, times(1)).getItem(itemId);
    }

    @Test
    public void testGetItem_NotFound() {
        UUID itemId = randomUUID();
        when(serviceMock.getItem(itemId)).thenThrow(new ItemNotFoundException());
        HttpResponse<GetItemResponse> response = controller.getItem(itemId);
        assertThat(response.status(), equalTo(HttpStatus.NOT_FOUND));
        verify(serviceMock, times(1)).getItem(itemId);
    }

    @Test
    public void testUpdateItem_Success() {
        UUID itemId = randomUUID();
        UpdateItemRequest request = TestRestData.buildUpdateItemRequest(RandomStringUtils.randomAlphabetic(8));
        HttpResponse response = controller.updateItem(itemId, request);
        assertThat(response.status(), equalTo(HttpStatus.NO_CONTENT));
        verify(serviceMock, times(1)).updateItem(itemId, request);
    }

    @Test
    public void testUpdateItem_NotFound() {
        UUID itemId = randomUUID();
        UpdateItemRequest request = TestRestData.buildUpdateItemRequest(RandomStringUtils.randomAlphabetic(8));
        doThrow(new ItemNotFoundException()).when(serviceMock).updateItem(itemId, request);
        HttpResponse response = controller.updateItem(itemId, request);
        assertThat(response.status(), equalTo(HttpStatus.NOT_FOUND));
        verify(serviceMock, times(1)).updateItem(itemId, request);
    }

    @Test
    public void testUpdateItem_ServiceThrowsException() {
        UUID itemId = randomUUID();
        UpdateItemRequest request = TestRestData.buildUpdateItemRequest(RandomStringUtils.randomAlphabetic(8));
        doThrow(new RuntimeException("Service failure")).when(serviceMock).updateItem(itemId, request);
        HttpResponse response = controller.updateItem(itemId, request);
        assertThat(response.status(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(serviceMock, times(1)).updateItem(itemId, request);
    }

    @Test
    public void testDeleteItem_Success() {
        UUID itemId = randomUUID();
        HttpResponse response = controller.deleteItem(itemId);
        assertThat(response.status(), equalTo(HttpStatus.NO_CONTENT));
        verify(serviceMock, times(1)).deleteItem(itemId);
    }

    @Test
    public void testDeleteItem_NotFound() {
        UUID itemId = randomUUID();
        doThrow(new ItemNotFoundException()).when(serviceMock).deleteItem(itemId);
        HttpResponse response = controller.deleteItem(itemId);
        assertThat(response.status(), equalTo(HttpStatus.NOT_FOUND));
        verify(serviceMock, times(1)).deleteItem(itemId);
    }
}
