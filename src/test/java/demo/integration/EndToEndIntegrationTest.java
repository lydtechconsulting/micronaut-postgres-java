package demo.integration;

import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import demo.util.TestRestData;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest
public class EndToEndIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testItemCRUD() {
        // Create the item.
        CreateItemRequest createItemRequest = TestRestData.buildCreateItemRequest(RandomStringUtils.randomAlphabetic(8).toLowerCase());
        HttpRequest<?> createItemHttpRequest = HttpRequest.POST("/v1/items", createItemRequest).accept(MediaType.APPLICATION_JSON);
        HttpResponse<Void> createItemResponse = client.toBlocking().exchange(createItemHttpRequest, Void.class);
        assertThat(createItemResponse.status(), equalTo(HttpStatus.CREATED));
        assertThat(createItemResponse.header("Location"), notNullValue());
        String itemId = createItemResponse.header("Location").toString();
        assertThat(itemId, notNullValue());

        // Retrieve the new item.
        HttpRequest<?> getItemHttpRequest = HttpRequest.GET("/v1/items/"+itemId).accept(MediaType.APPLICATION_JSON);
        HttpResponse<GetItemResponse> getItemResponse = client.toBlocking().exchange(getItemHttpRequest, GetItemResponse.class);
        assertThat(getItemResponse.status(), equalTo(HttpStatus.OK));
        assertThat(getItemResponse.body().name(), equalTo(createItemRequest.name()));
        assertThat(getItemResponse.body().id().toString(), equalTo(itemId));

        // Update the item.
        UpdateItemRequest updateItemRequest = TestRestData.buildUpdateItemRequest(RandomStringUtils.randomAlphabetic(8).toLowerCase());
        HttpRequest<?> updateItemHttpRequest = HttpRequest.PUT("/v1/items/"+itemId, updateItemRequest).accept(MediaType.APPLICATION_JSON);
        HttpResponse<Void> updateItemResponse = client.toBlocking().exchange(updateItemHttpRequest, Void.class);
        assertThat(updateItemResponse.status(), equalTo(HttpStatus.NO_CONTENT));

        // Retrieve the updated item.
        HttpResponse<GetItemResponse> getItemResponseUpdated = client.toBlocking().exchange(getItemHttpRequest, GetItemResponse.class);
        assertThat(getItemResponseUpdated.status(), equalTo(HttpStatus.OK));
        assertThat(getItemResponseUpdated.body().name(), equalTo(getItemResponseUpdated.body().name()));

        // Delete the item
        HttpRequest<?> deleteItemHttpRequest = HttpRequest.DELETE("/v1/items/"+itemId).accept(MediaType.APPLICATION_JSON);
        HttpResponse<Void> deleteItemResponse = client.toBlocking().exchange(deleteItemHttpRequest, Void.class);
        assertThat(deleteItemResponse.status(), equalTo(HttpStatus.NO_CONTENT));

        // Retrieve the deleted item - should be NOT FOUND.
        try {
            HttpResponse<GetItemResponse> getItemResponseDeleted = client.toBlocking().exchange(getItemHttpRequest, GetItemResponse.class);
            fail("Expected item to be deleted, but found item with Id: " + getItemResponseDeleted.body().id());
        } catch (HttpClientResponseException e) {
            assertThat(e.getStatus(), equalTo(HttpStatus.NOT_FOUND));
        }
    }
}
