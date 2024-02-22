package demo.rest.api;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UpdateItemRequest(String name) {}
