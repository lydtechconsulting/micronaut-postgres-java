package demo.controller;

import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;

import demo.exception.ItemNotFoundException;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import demo.service.ItemService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/v1/items")
public class ItemController {

    private final ItemService itemService;

    @Inject
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Post
    public HttpResponse<Void> createItem(@Body @Valid CreateItemRequest request) {
        log.info("Received request to create item with name: {}", request.name());
        try {
            UUID itemId = itemService.createItem(request);
            return HttpResponse.created(URI.create(itemId.toString()));
        } catch(Exception e) {
            log.error(e.getMessage());
            return HttpResponse.serverError();
        }
    }

    @Get("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<GetItemResponse> getItem(@PathVariable UUID itemId) {
        log.info("Looking up item with id: {}", itemId);
        try {
            GetItemResponse response = itemService.getItem(itemId);
            return HttpResponse.ok().body(response);
        } catch(ItemNotFoundException e) {
            return HttpResponse.notFound();
        }
    }

    @Put("/{itemId}")
    public HttpResponse<Void> updateItem(@PathVariable UUID itemId, @Body @Valid UpdateItemRequest request) {
        log.info("Received request to update item with id: {} - name: {}", itemId, request.name());
        try {
            itemService.updateItem(itemId, request);
            return HttpResponse.noContent();
        } catch(ItemNotFoundException e) {
            return HttpResponse.notFound();
        } catch(Exception e) {
            log.error(e.getMessage());
            return HttpResponse.serverError();
        }
    }

    @Delete("/{itemId}")
    public HttpResponse<Void> deleteItem(@PathVariable UUID itemId) {
        log.info("Deleting item with id: {}", itemId);
        try {
            itemService.deleteItem(itemId);
            return HttpResponse.noContent();
        } catch(ItemNotFoundException e) {
            log.error(e.getMessage());
            return HttpResponse.notFound();
        }
    }
}
