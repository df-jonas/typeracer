package be.defrere.typeracer.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final String FRONTEND_INDEX = "index.html";
    private static final String FRONTEND_RESOURCES = "/META-INF/resources/ui/";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/")
                .setViewName("forward:/" + FRONTEND_INDEX);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:" + FRONTEND_RESOURCES)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        return canLoad(resource) ? resource : new ClassPathResource(FRONTEND_RESOURCES + FRONTEND_INDEX);
                    }

                    private boolean canLoad(Resource requestedResource) {
                        return requestedResource.exists() && requestedResource.isReadable();
                    }
                });
    }
}
