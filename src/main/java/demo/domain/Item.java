package demo.domain;

import java.util.UUID;
import javax.persistence.Id;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.MappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedEntity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

        @Id
        @AutoPopulated
        private UUID id;

        private String name;
}

