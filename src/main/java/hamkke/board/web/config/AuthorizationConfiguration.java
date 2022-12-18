package hamkke.board.web.config;

import hamkke.board.web.argumentresolver.LoginUserArgumentResolver;
import hamkke.board.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthorizationConfiguration implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final LoginCheckInterceptor loginCheckInterceptor;

    public AuthorizationConfiguration(final LoginUserArgumentResolver loginUserArgumentResolver, final LoginCheckInterceptor loginCheckInterceptor) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/join", "/api/user/login", "/api/user/login12");
    }
}
