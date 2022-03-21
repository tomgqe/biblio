package org.miage.biblio.boundary;

import java.util.function.Consumer;
import org.apache.kafka.streams.kstream.KStream;
import org.miage.biblio.entity.Avion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AvionKafkaConsumer {
  private final AvionRessource avionRessource;

  private KafkaTemplate<String, Avion> kafkaTemplate;
  private final String KAFKA_TOPIC_ROLLBACK = "error-topic";


  @Autowired
  public AvionKafkaConsumer(final KafkaTemplate<String, Avion> template,AvionRessource avionRessource) {
    this.avionRessource = avionRessource;
    this.kafkaTemplate = template;
  }

  @Bean
  public Consumer<KStream<String, Avion>> avionService() {
    return kstream -> kstream.foreach((key, avion) -> {
      System.out.println(String.format("Demande Avion recue: [%s], ID [%s]", avion.getDestination(), avion.getId()));
      Avion a = new Avion(avion.getId(), avion.getDestination());

      // cas pour montrer la compensation
      if (avion.getDestination().equalsIgnoreCase("Madrid")){
        this.kafkaTemplate.send(KAFKA_TOPIC_ROLLBACK,new Avion(avion.getId(), avion.getDestination()));
      }else
      {
        this.avionRessource.save(a);
      }
    });
  }
}
