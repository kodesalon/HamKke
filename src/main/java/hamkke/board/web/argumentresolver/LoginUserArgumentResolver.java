package hamkke.board.web.argumentresolver;

import hamkke.board.web.jwt.TokenResolver;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenResolver tokenResolver;

    public LoginUserArgumentResolver(final TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasUserIdType = Long.class
                .isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasUserIdType;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        String authorizationHeader = webRequest.getHeader("Authorization");
        tokenResolver.validateToken(authorizationHeader);
        return tokenResolver.getUserId(authorizationHeader);
    }
}
