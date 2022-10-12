package hamkke.board.model.bulletin;

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
}
