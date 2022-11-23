package hamkke.board.model.bulletin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Title {

    private static final int MAXIMUM_TITLE_SIZE = 25;

    private final String value;

    public Title(final String title) {
        validateSize(title);
        value = title;
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
