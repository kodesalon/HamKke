package hamkke.board.model.bulletin;

import java.util.Objects;

public class Content {

    public static final int MAXIMUM_CONTENT_SIZE = 1000;

    private final String value;

    public Content(final String content) {
        validateSize(content);
        value = content;
    }

    private void validateSize(final String content) {
        if (content.isBlank() || content.length() > MAXIMUM_CONTENT_SIZE) {
            throw new IllegalArgumentException("글 내용은 최소 1자 이상, 최대 1000자 입니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(value, content.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
