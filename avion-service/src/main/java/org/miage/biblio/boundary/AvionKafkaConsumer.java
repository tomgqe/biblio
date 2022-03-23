package org.miage.biblio.boundary;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.apache.kafka.streams.kstream.KStream;
import org.miage.biblio.entity.Avion;
import org.miage.biblio.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AvionKafkaConsumer {
  private final AvionRessource avionRessource;

  private KafkaTemplate<String, Hotel> kafkaTemplate;
  private final String KAFKA_TOPIC_ROLLBACK = "error-topic";


  @Autowired
  public AvionKafkaConsumer(final KafkaTemplate<String, Hotel> template,AvionRessource avionRessource) {
    this.avionRessource = avionRessource;
    this.kafkaTemplate = template;
  }

  @Bean
  public Consumer<KStream<String, Hotel>> avionService() {
    return kstream -> kstream.foreach((key, hotel) -> {
      System.out.println(String.format("Demande Réservation Avion recue: [%s], ID [%s]", hotel.getVille(), hotel.getId()));
      Avion a = new Avion(hotel.getId(), hotel.getVille());

      // cas pour montrer la compensation
      if (hotel.getVille().equalsIgnoreCase("Madrid")){
        System.out.println("ATTENTION, reservation avion impossible, rollback de l'hotel reservé");
        System.out.println("Envoie de l'erreur dans un nouveau topics dans 5s");
        try {
          TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        this.kafkaTemplate.send(KAFKA_TOPIC_ROLLBACK, hotel);
      }else{
        System.out.println("Enregistrement de l'avion");
        this.avionRessource.save(a);
      }
    });
  }
}
