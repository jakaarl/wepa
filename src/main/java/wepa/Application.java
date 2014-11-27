package wepa;

import javax.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import wepa.helpers.CurrentUserProvider;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("20000KB");
        factory.setMaxRequestSize("20000KB");
        return factory.createMultipartConfig();
    }
    
    @Bean
    FilterRegistrationBean filterRegistrationBean() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding(DEFAULT_ENCODING);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(encodingFilter);
        return registrationBean;
    }
    
    @Bean
    CurrentUserProvider currentUserProvider() {
        return new CurrentUserProvider();
    }
}
