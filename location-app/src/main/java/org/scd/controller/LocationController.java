package org.scd.controller;

import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.LocationCreationDTO;
import org.scd.model.dto.LocationDetailsResponseDTO;
import org.scd.model.dto.LocationUpdateRequestDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.model.security.Role;
import org.scd.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin
@RequestMapping(path = "/locations")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping
    public ResponseEntity createLocation(@RequestBody final LocationCreationDTO locationCreationDTO)
            throws BusinessException {
        final URI location = ServletUriComponentsBuilder // http://localhost:8080
                .fromCurrentRequest()// http://localhost:8080/locations
                .path("/{id}")// http://localhost:8080/locations/{id}
                .buildAndExpand(locationService.createLocation(locationCreationDTO).getId())// http://localhost:8080/locations/location.getId()
                .toUri();
        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "/{locationId}")
    public ResponseEntity<LocationDetailsResponseDTO> getLocationById(@PathVariable(value = "locationId") Long locationId)
            throws BusinessException {

        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @PutMapping(path = "/{locationId}")
    public ResponseEntity<Location> updateLocationById(@PathVariable(value = "locationId") Long locationId,
                                                       @RequestBody final LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException {
        return ResponseEntity.ok(locationService.updateLocationById(locationId, locationUpdateRequestDTO));
    }

    @DeleteMapping(path = "/{locationId}")
    public ResponseEntity deleteLocationById(@PathVariable(value = "locationId") Long locationId)
            throws BusinessException {
        locationService.deleteLocationById(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<LocationDetailsResponseDTO>> filterLocations(
            @RequestParam(name = "startDate") final @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate") final @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(name = "userId") final Long userId) throws BusinessException{
        return ResponseEntity.ok(locationService.filterLocations(startDate, endDate, userId));
    }
}
