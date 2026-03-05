package dolpi.CivicInsight.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Sabhi endpoints ke liye
                .allowedOrigins("https://civic-insight-2i2101yj3-avenger-manas-projects.vercel.app") // Aapka Frontend URL
                .allowedOrigins("https://civic-insight-2i2101yj3-avenger-manas-projects.vercel.app") // Aapka Vercel URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
