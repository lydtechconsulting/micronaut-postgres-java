package demo.rest.api;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateItemRequest(String name) {}
