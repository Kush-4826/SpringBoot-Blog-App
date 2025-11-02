package in.co.lazylan.bootblog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootBlogAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBlogAppApplication.class, args);
    }

    /**
     * Creating a ModelMapper Bean in the IoC Container
     * so that I can access it to map my models to their corresponding DTOs.
     *
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
