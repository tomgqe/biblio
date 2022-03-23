package org.miage.biblio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@LoadBalancerClients({
		@LoadBalancerClient(name = "hotel-service", configuration = ClientConfiguration.class)
})
public class RollbackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RollbackServiceApplication.class, args);
	}
	@Bean
	RestTemplate template() {
		return new RestTemplate();
	}
}
