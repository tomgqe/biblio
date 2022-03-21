package org.miage.biblio.boundary;

import org.miage.biblio.entity.Avion;
import org.miage.biblio.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hotels")
public class HotelRessource {

    private static long ID = 0;

    private HotelRepository hr;
    private KafkaTemplate<String, Avion> kafkaTemplateReservation;
    //private KafkaTemplate<String, Hotel> kafkaTemplateRollback;
    private final String KAFKA_TOPIC_RESERVATION = "reservation-topic";
    private final String KAFKA_TOPIC_ROLLBACK = "error-topic";

    @Autowired
    public HotelRessource(/*final KafkaTemplate<String, Hotel> template,*/final KafkaTemplate<String, Avion> templateReservation, HotelRepository hr) {
        this.hr = hr;
        //this.kafkaTemplateRollback = template;
        this.kafkaTemplateReservation = templateReservation;
    }

    @GetMapping
    public String allHotels() {
        Collection<Hotel> content = this.hr.findAll();
        String listeHotel = "";
        for (Hotel h :content) {
            listeHotel += h.toString() + '\n';
        }
        return listeHotel;
    }

    @PostMapping
    public String writeHotel(@RequestBody Hotel hotel) throws Exception {
        hr.save(new Hotel(++ID,hotel.getNom(),hotel.getVille(),"Reservé"));
        this.kafkaTemplateReservation.send(KAFKA_TOPIC_RESERVATION, new Avion(ID,hotel.getVille()));
        //this.kafkaTemplateRollback.send(KAFKA_TOPIC_ROLLBACK, new Hotel(ID,hotel.getNom(),hotel.getVille(),"Reservé"));

        return "Nouvel hotel reservé: "+hotel.getNom()+ " à "+ hotel.getVille();
    }

    @DeleteMapping(value = "/{id}")
    public String writeHotel(@PathVariable(name = "id") Long id) throws Exception {
        Hotel hotel = hr.findById(id).get();
        hr.save(new Hotel(id,hotel.getNom(),hotel.getVille(),"annulé"));
        return "Nouvel hotel annulé: "+hotel.getNom()+ " à "+ hotel.getVille();
    }
}
