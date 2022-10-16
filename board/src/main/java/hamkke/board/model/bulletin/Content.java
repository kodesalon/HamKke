package hamkke.board.model.bulletin;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private static final int MAXIMUM_CONTENT_SIZE = 1000;

    @Column(name="content")
    private String value;

    public Content(final String value) {
        validateSize(value);
        this.value = value;
    }

    private void validateSize(final String content) {
        if (content.isBlank() || content.length() > MAXIMUM_CONTENT_SIZE) {
            throw new IllegalArgumentException("글 내용은 최소 1자 이상, 최대 1000자 입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
