package org.miage.biblio.boundary;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.apache.kafka.streams.kstream.KStream;
import org.miage.biblio.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
@RestController
public class HotelKafkaConsumer {

  LoadBalancerClientFactory clientFactory;
  RestTemplate template;

  @Autowired
  public HotelKafkaConsumer(LoadBalancerClientFactory clientFactory, RestTemplate template) {
    this.clientFactory = clientFactory;
    this.template = template;
  }

  @Bean
  public Consumer<KStream<String, Hotel>> rollbackService() {
    return kstream -> kstream.foreach((key, hotel) -> {
      System.out.println(String.format("Demande annulation Hotel recue:  ID [%s]", hotel.getId().toString()));
      System.out.println("Passage de l'hotel en etat annulé dans 5s");
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Hotel a = new Hotel(hotel.getId(), hotel.getNom(), hotel.getVille(), "Remboursé");
      this.annulerHotel(a);
    });
  }

  public void annulerHotel(Hotel hotel){
    RoundRobinLoadBalancer lb = clientFactory.getInstance("hotel-service", RoundRobinLoadBalancer.class);
    ServiceInstance instance = lb.choose().block().getServer();
    String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/hotels/hotelId/"+hotel.getId()+"/rembourser";

    String res = template.getForObject(url, String.class);
    System.out.println("Hotel remboursé");

    // System.out.println(res);
  }
}
