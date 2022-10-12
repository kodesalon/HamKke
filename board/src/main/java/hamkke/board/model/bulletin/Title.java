package hamkke.board.model.bulletin;

import java.util.Objects;

public class Title {

    private static final int MAXIMUM_TITLE_SIZE = 25;

    private final String value;

    public Title(final String inputTitle) {
        validateSize(inputTitle);
        value = inputTitle;
    }

    private void validateSize(final String inputTitle) {
        if (inputTitle.isBlank() || inputTitle.length() > MAXIMUM_TITLE_SIZE) {
            throw new IllegalArgumentException("글 제목은 1글자 이상 25자 이하야합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(value, title.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
