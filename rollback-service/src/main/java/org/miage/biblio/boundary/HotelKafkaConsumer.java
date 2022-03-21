package org.miage.biblio.boundary;

import java.util.function.Consumer;
import org.apache.kafka.streams.kstream.KStream;
import org.miage.biblio.entity.Avion;
import org.miage.biblio.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HotelKafkaConsumer {
  //private final HotelWriter hotelWriter;

  public HotelKafkaConsumer() {

  }

  @Bean
  public Consumer<KStream<String, Avion>> hotelService() {
    return kstream -> kstream.foreach((key, avion) -> {
      System.out.println(String.format("Demande annulation Hotel recue:  ID [%s]", avion.getId().toString()));
      //Hotel a = new Hotel(hotel.getId(), hotel.getNom(), hotel.getVille(), "annulé");
      //this.hotelWriter.cancel(avion.getId());
    });
  }
}
