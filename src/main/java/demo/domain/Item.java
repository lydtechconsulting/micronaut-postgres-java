package demo.domain;

import java.util.UUID;
import javax.persistence.Id;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedEntity("ITEM")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

        @Id
        @AutoPopulated
        @MappedProperty("ID")
        private UUID id;

        @MappedProperty("NAME")
        private String name;
}

