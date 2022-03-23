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
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/hotels")
public class HotelRessource {

    private static long ID = 0;

    private HotelRepository hr;
    private KafkaTemplate<String, Hotel> kafkaTemplateReservation;
    //private KafkaTemplate<String, Hotel> kafkaTemplateRollback;
    private final String KAFKA_TOPIC_RESERVATION = "reservation-topic";
    private final String KAFKA_TOPIC_ROLLBACK = "error-topic";

    @Autowired
    public HotelRessource(/*final KafkaTemplate<String, Hotel> template,*/final KafkaTemplate<String, Hotel> templateReservation, HotelRepository hr) {
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
        Hotel newHotel = new Hotel(++ID,hotel.getNom(),hotel.getVille(),"Reservé");
        hr.save(newHotel);
        System.out.println("Envoie de l'hotel au service avion dans 5s");
        TimeUnit.SECONDS.sleep(5);
        this.kafkaTemplateReservation.send(KAFKA_TOPIC_RESERVATION, newHotel);
        //this.kafkaTemplateRollback.send(KAFKA_TOPIC_ROLLBACK, new Hotel(ID,hotel.getNom(),hotel.getVille(),"Reservé"));

        return "Nouvel hotel reservé: "+hotel.getNom()+ " à "+ hotel.getVille();
    }

    @DeleteMapping(value = "/{id}")
    public String writeHotel(@PathVariable(name = "id") Long id) throws Exception {
        Hotel hotel = hr.findById(id).get();
        hr.save(new Hotel(id,hotel.getNom(),hotel.getVille(),"Remboursé"));
        return "Nouvel hotel remboursé: "+hotel.getNom()+ " à "+ hotel.getVille();
    }

    @GetMapping("/hotelId/{hotelId}/rembourser")
    public String annulerHotel(@PathVariable(name = "hotelId") Long id) {
        Hotel hotel = hr.findById(id).get();
        hr.save(new Hotel(id,hotel.getNom(),hotel.getVille(),"Remboursé"));
        return "Hotel remboursé: "+hotel.getNom()+ " à "+ hotel.getVille();
    }

}
