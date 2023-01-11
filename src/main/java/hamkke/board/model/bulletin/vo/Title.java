package hamkke.board.model.bulletin.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {

    private static final int MAXIMUM_TITLE_SIZE = 25;

    @Column(name = "title")
    private String value;

    public Title(final String value) {
        validateSize(value);
        this.value = value;
    }

    private void validateSize(final String title) {
        if (title.isBlank() || title.length() > MAXIMUM_TITLE_SIZE) {
            throw new IllegalArgumentException("글 제목은 1글자 이상 25자 이하야합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
