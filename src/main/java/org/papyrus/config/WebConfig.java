package org.papyrus.config;

import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Configuration
@EnableWebMvc
//Scans the classpath for annotated components that will be auto-registered as Spring beans.
//For example @Controller,@Service,@Component
@ComponentScan(basePackages={"org.papyrus.web.controller","org.myWeather.web.domain", "org.myWeather.persistence.DaoImpl"})
public class WebConfig extends WebMvcConfigurerAdapter{
	
	//----------------------Read and write JSON, used in all @Responsebody-------------------------------------- 
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {        
        converters.add(jackson2Converter());
    }	

    @Bean
    public MappingJackson2HttpMessageConverter jackson2Converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        Object objectMapper = new ObjectMapper();
        ((ObjectMapper) objectMapper).enable(SerializationFeature.INDENT_OUTPUT);
        return (ObjectMapper) objectMapper;
    }	
	
	//---------------------------------Resource handling-----------------------------------------------------------	
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/design/");
	}
	
	//Interceptor that allows for changing the current locale on every request, via a configurable request parameter	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}	
	
	//Support the internationalization or multiple languages features(Thymeleaf)	
	@Bean
	public LocaleResolver localeResolver() {
	    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
	    cookieLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString("en"));
	    return cookieLocaleResolver;
	}
	
	//The two below beans and the templateEngine function is Thymeleaf configurations
	//It contains what mode HMTL5 that is set and where to find views etc
	//A bit similar to jsp viewresolver. 
	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		//HTML5 as the template mode.
		resolver.setTemplateMode("HTML5");
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.setTemplateResolver(templateResolver());
	    return engine;
	}
	
	@Bean
	public ViewResolver viewResolver() {
	    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	    viewResolver.setTemplateEngine(templateEngine());
	    viewResolver.setOrder(1);
	    viewResolver.setViewNames(new String[]{"*"});
	    viewResolver.setCache(false);
	    viewResolver.setCharacterEncoding("UTF-8");
	    return viewResolver;
	}	
	
	// Provides internationalization of messages	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    //messageSource.setBasenames("classpath:/messages/messages", "classpath:messages/validation");
	    //messageSource.setBasenames(new String[] {"classpath:messages", "classpath:messages/validation", "classpath:messages/messages", "messages", "messages/messages","messages"});
	    messageSource.setBasenames("messages","/messages","resources/design/messages/messages","resources/messages/messages","/resources/design/messages/messages","/resources/messages/messages");
	    //messageSource.setBasenames("/messages/messages", "classpath:messages/validation");
	    // if true, the key of the message will be displayed if the key is not
	    // found, instead of throwing a NoSuchMessageException
	    
	    messageSource.setUseCodeAsDefaultMessage(true);
	    messageSource.setDefaultEncoding("UTF-8");
	    
	    // # -1 : never reload, 0 always reload
	    messageSource.setCacheSeconds(0);	    
	    
	    return messageSource;
	}
	
}
