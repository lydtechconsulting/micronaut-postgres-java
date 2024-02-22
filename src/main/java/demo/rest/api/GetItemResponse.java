package demo.rest.api;

import java.util.UUID;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GetItemResponse(UUID id, String name) {}
