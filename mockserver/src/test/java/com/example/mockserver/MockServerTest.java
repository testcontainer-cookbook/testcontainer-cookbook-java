package com.example.mockserver;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
@EnableFeignClients(basePackages = {"com.example.mockserver"})
@ImportAutoConfiguration({FeignAutoConfiguration.class, LoadBalancerAutoConfiguration.class, FeignLoadBalancerAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@LoadBalancerClient(name = "fooClient", configuration = TestBean.class)
@Testcontainers
public class MockServerTest {

    @Autowired
    private Client client;

    @Autowired
    private HelloController helloController;
    static DockerImageName mockServerImage = DockerImageName.parse("mockserver/mockserver:5.15.0");

    @Container
    protected static final MockServerContainer mockServerContainer = new MockServerContainer(mockServerImage);

    @Test
    void test() {

        try (MockServerClient mockServerClient = new MockServerClient("localhost", mockServerContainer.getServerPort())) {

            mockServerClient.when(
                    request()
                            .withMethod("GET")
                            .withQueryStringParameter("name", "foo")
                            .withPath("/foo")
                            .withContentType(MediaType.APPLICATION_JSON)
            ).respond(
                    response()
                            .withStatusCode(200)
                            .withBody("Hello World!")
                            .withContentType(MediaType.APPLICATION_JSON)
            );

            mockServerClient
                    .when(request().withPath("/foo").withQueryStringParameter("name", "peter"))
                    .respond(response().withBody("Peter the person!"));

            // ...a GET request to '/person?name=peter' returns "Peter the person!"


            helloController.foo();

            Assertions.assertEquals("Hello World!", s);
        }


    }


    @DynamicPropertySource
    static void set(DynamicPropertyRegistry registry) {
        registry.add("fooClient.url", () -> "localhost:" + mockServerContainer.getServerPort());
    }


}
