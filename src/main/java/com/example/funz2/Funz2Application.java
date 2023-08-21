package com.example.funz2;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class Funz2Application {

    public static void main(String[] args) {
        SpringApplication.run(Funz2Application.class, args);
    }


    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("https://jsonplaceholder.typicode.com/todos/").build();
    }

    @Bean
    TodoClient todoClient(RestClient restClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(TodoClient.class);
    }

    @Bean
    ApplicationRunner applicationRunner(TodoClient todoClient) {
        return args -> {
            var response = todoClient.todos();

            System.out.println(response);
        };

    }

    record Todo(
            Long userId,
            Long id,
            String title
    ) {}

    interface TodoClient {
        @GetExchange
        List<Todo> todos();
    }

}
