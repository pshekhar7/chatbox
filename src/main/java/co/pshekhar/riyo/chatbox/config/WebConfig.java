package co.pshekhar.riyo.chatbox.config;

import co.pshekhar.riyo.chatbox.interceptor.ClientPayloadInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ClientPayloadInterceptor clientPayloadInterceptor;

    public WebConfig(ClientPayloadInterceptor clientPayloadInterceptor) {
        this.clientPayloadInterceptor = clientPayloadInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clientPayloadInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/create/user");
    }
}
