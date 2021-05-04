//package com.github.euler.api;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import io.swagger.models.Scheme;
//import io.swagger.models.Swagger;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.swagger2.web.SwaggerTransformationContext;
//import springfox.documentation.swagger2.web.WebMvcSwaggerTransformationFilter;
//
//@Component
//@ConditionalOnClass(WebMvcSwaggerTransformationFilter.class)
//@Order(Ordered.LOWEST_PRECEDENCE)
//public class SpringfoxSwaggerBasePathResolver implements WebMvcSwaggerTransformationFilter {
//
//    private final APIConfiguration apiConfiguration;
//
//    @Autowired
//    public SpringfoxSwaggerBasePathResolver(APIConfiguration apiConfiguration) {
//        super();
//        this.apiConfiguration = apiConfiguration;
//    }
//
//    @Override
//    public Swagger transform(SwaggerTransformationContext<HttpServletRequest> context) {
//        Swagger specification = context.getSpecification();
//
//        try {
//            URL baseURL = new URL(apiConfiguration.getConfig().getString("euler.http-api.base-url"));
//            specification.host(getHost(baseURL));
//            specification.basePath(baseURL.getPath());
//            specification.scheme(Scheme.forValue(baseURL.getProtocol()));
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        return specification;
//    }
//
//    private String getHost(URL baseURL) {
//        String protocol = baseURL.getProtocol();
//        String host = baseURL.getHost();
//        int port = baseURL.getPort();
//        if (port >= -1 && (protocol.equalsIgnoreCase("http") && port != 80) || (protocol.equalsIgnoreCase("https") && port != 443)) {
//            host += ":" + port;
//        }
//        return host;
//    }
//
//    @Override
//    public boolean supports(DocumentationType delimiter) {
//        return delimiter.equals(DocumentationType.SWAGGER_2);
//    }
//
//}
