package org.miage.biblio.boundary;

import org.miage.biblio.entity.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("hotel-service")
public interface HotelWriter {

    @DeleteMapping(value = "/hotels/{id}")
    void cancel(@PathVariable(name = "id") Long id);

    @GetMapping(value = "/hotels")
    CollectionModel<Hotel> read();
}

