package com.example.mockserver;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.ServiceInstanceListSuppliers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class TestBean {

    @Configuration(proxyBeanMethods = false)
    public static class LocalClientConfiguration {


        @Bean
        public ServiceInstanceListSupplier staticServiceInstanceListSupplier() {
            return ServiceInstanceListSuppliers.from("fooClient",
                    new DefaultServiceInstance("fooClient-1", "fooClient", "localhost", MockServerTest.mockServerContainer.getServerPort(), false));
        }

    }

}
