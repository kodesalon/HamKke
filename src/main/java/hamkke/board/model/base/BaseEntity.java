package hamkke.board.model.base;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(updatable = false)
    protected LocalDateTime createdDateTime;

    protected LocalDateTime lastModifiedDateTime;
}
