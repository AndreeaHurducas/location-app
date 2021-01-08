package org.scd.service;

import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.LocationCreationDTO;
import org.scd.model.dto.LocationDetailsResponseDTO;
import org.scd.model.dto.LocationUpdateRequestDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.model.security.Role;
import org.scd.repository.LocationRepository;
import org.scd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Location createLocation(LocationCreationDTO locationCreationDTO) throws BusinessException {

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (Objects.isNull(locationCreationDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(locationCreationDTO.getLatitude())) {
            throw new BusinessException(400, "Latitude cannot be null ! ");
        }

        if (Objects.isNull(locationCreationDTO.getLongitude())) {
            throw new BusinessException(400, "Longitude cannot be null !");
        }

        Location location = new Location();
        location.setLatitude(locationCreationDTO.getLatitude());
        location.setLongitude(locationCreationDTO.getLongitude());
        location.setUser(currentUser);
        locationRepository.save(location);

        return location;
    }

    @Override
    public LocationDetailsResponseDTO getLocationById(Long locationId) throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);

        if (Objects.isNull(location)) {
            throw new BusinessException(404, "Location does not exist!");
        }
        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            if (role.getRole().equals(adminRole)) {
                isAdmin = true;
            }
        }

        if ((isAdmin) || ((location.getUser().getId()) == (currentUser.getId()))) {

            final LocationDetailsResponseDTO locationDetailsResponseDTO = new LocationDetailsResponseDTO();
            locationDetailsResponseDTO.setId(location.getId());
            locationDetailsResponseDTO.setLatitude(location.getLatitude());
            locationDetailsResponseDTO.setLongitude(location.getLongitude());
            locationDetailsResponseDTO.setUser(location.getUser());
            locationDetailsResponseDTO.setCreatedAt(location.getCreatedAt());
            locationDetailsResponseDTO.setUpdatedAt(location.getUpdatedAt());

            return locationDetailsResponseDTO;
        } else throw new BusinessException(401, "Unauthorized!");

    }

    @Override
    public Location updateLocationById(Long locationId, LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);

        if (Objects.isNull(location)) {
            throw new BusinessException(404, "Location does not exist!");
        }

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            if (role.getRole().equals(adminRole)) {
                isAdmin = true;
            }
        }

        if ((isAdmin) || (location.getUser().getId() == currentUser.getId())) {

            location.setLatitude(locationUpdateRequestDTO.getLatitude());
            location.setLongitude(locationUpdateRequestDTO.getLongitude());

            locationRepository.save(location);
            return location;
        } else throw new BusinessException(401, "Unauthorized!");
    }

    @Override
    public void deleteLocationById(Long locationId) throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);

        if (Objects.isNull(location))
            throw new BusinessException(404, "Location does not exist!");

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            if (role.getRole().equals(adminRole)) {
                isAdmin = true;
            }
        }

        if ((isAdmin) || (location.getUser().getId() == currentUser.getId())) {

            locationRepository.delete(location);

        } else throw new BusinessException(401, "Unauthorized!");
    }

    @Override
    public List<LocationDetailsResponseDTO> filterLocations(Date startDate, Date endDate, Long userId) throws BusinessException {

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            if (role.getRole().equals(adminRole)) {
                isAdmin = true;
            }
        }
        if (isAdmin) {

            User user = userRepository.findById(userId).orElse(null);
            if (Objects.isNull(user))
                throw new BusinessException(404, "User not found!");

            final List<LocationDetailsResponseDTO> locationDetailsResponseDTOS = new ArrayList<>();

            final List<Location> locations = locationRepository.findByUserId(userId);

            for (Location location : locations) {
                if (location.getCreatedAt().after(startDate) && location.getCreatedAt().before(endDate)) {
                    LocationDetailsResponseDTO locationDetailsResponseDTO = new LocationDetailsResponseDTO();
                    locationDetailsResponseDTO.setId(location.getId());
                    locationDetailsResponseDTO.setUser(location.getUser());
                    locationDetailsResponseDTO.setLongitude(location.getLongitude());
                    locationDetailsResponseDTO.setLatitude(location.getLatitude());
                    locationDetailsResponseDTO.setCreatedAt(location.getCreatedAt());
                    locationDetailsResponseDTO.setUpdatedAt(location.getUpdatedAt());
                    locationDetailsResponseDTOS.add(locationDetailsResponseDTO);
                }
            }
            return locationDetailsResponseDTOS;
        } else throw new BusinessException(401, "Unauthorized!");

    }
}
