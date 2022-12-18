package hamkke.board.web.interceptor;

import hamkke.board.web.jwt.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final TokenResolver tokenResolver;

    public LoginCheckInterceptor(final TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            tokenResolver.validateToken(authorizationHeader);
        } catch (final RuntimeException e) {

            throw new IllegalAccessException("잘못된 접근입니다.");
        }
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        log.info("{}", "@@@@postHandle@@@");
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        log.info("{}", "@@@@afterCompletion@@@");
    }
}
