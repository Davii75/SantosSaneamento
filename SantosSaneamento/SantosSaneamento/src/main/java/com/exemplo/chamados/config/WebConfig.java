package com.exemplo.chamados.config;

import com.exemplo.chamados.security.AutenticacaoInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir:uploads}")
    private String pastaUploads;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AutenticacaoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/favicon.ico", "/error", "/h2-console/**");
    }

    /** Deixa as fotos enviadas acessíveis em /uploads/nome-do-arquivo. */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path pasta = Paths.get(pastaUploads).toAbsolutePath();
        try {
            // a pasta precisa existir antes: sem isso a URL do local não termina com "/"
            Files.createDirectories(pasta);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar a pasta de uploads: " + pasta, e);
        }
        String local = pasta.toUri().toString();
        if (!local.endsWith("/")) {
            local += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(local);
    }
}
