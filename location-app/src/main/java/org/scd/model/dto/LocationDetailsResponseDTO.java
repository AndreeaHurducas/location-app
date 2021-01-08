package org.scd.model.dto;

import org.scd.model.AuditModel;
import org.scd.model.User;

public class LocationDetailsResponseDTO extends AuditModel {

    private Long id;
    private double latitude;
    private double longitude;
    private User user;

    public LocationDetailsResponseDTO() {

    }

    public LocationDetailsResponseDTO(Long id, double latitude, double longitude, User user) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
