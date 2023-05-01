package com.example.mockserver;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MockserverApplication.class)
@Testcontainers
public class MockServerTest {

    @Autowired
    private Client client;
    static DockerImageName mockServerImage = DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.11.2");

    @Container
    private static final MockServerContainer mockServerContainer = new MockServerContainer(mockServerImage);

    static {


    }
    @Test
    void test() {

        try (MockServerClient mockServerClient = new MockServerClient("localhost", mockServerContainer.getServerPort())) {

            mockServerClient.when(
                    request()
                            .withMethod("GET")
                            .withPathParameter("name", "foo")
                            .withPath("/foo")
            ).respond(
                    response()
                            .withStatusCode(200)
                            .withBody("Hello World!")
            );

            mockServerClient
                    .when(request().withPath("/foo").withQueryStringParameter("name", "peter"))
                    .respond(response().withBody("Peter the person!"));

            // ...a GET request to '/person?name=peter' returns "Peter the person!"

            String s = client.get("foo");
            System.out.println(s);
        }


    }


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("fooClient", () -> "http://localhost:" + mockServerContainer.getServerPort());
        registry.add("foo-client.url", () -> "localhost:" + mockServerContainer.getServerPort());
    }




}
