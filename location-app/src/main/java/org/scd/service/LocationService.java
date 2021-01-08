package org.scd.service;

import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.dto.LocationCreationDTO;
import org.scd.model.dto.LocationDetailsResponseDTO;
import org.scd.model.dto.LocationUpdateRequestDTO;

import java.util.Date;
import java.util.List;

public interface LocationService {

    /**
     * create new location
     *
     * @param locationCreationDTO
     * @return
     */
    Location createLocation(final LocationCreationDTO locationCreationDTO) throws BusinessException;


    /**
     * get location by id
     *
     * @param locationId
     * @return
     * @throws BusinessException
     */
    LocationDetailsResponseDTO getLocationById(final Long locationId) throws BusinessException;

    /**
     * update location by id
     *
     * @param locationId
     * @param locationUpdateRequestDTO
     * @return
     * @throws BusinessException
     */
    Location updateLocationById(final Long locationId, final LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException;

    /**
     * delete location by id
     *
     * @param locationId
     * @throws BusinessException
     */
    void deleteLocationById(Long locationId) throws BusinessException;

    /**
     * filter locations by start date, end date , userId
     *
     * @param startDate
     * @param endDate
     * @param userId
     * @return
     */
    List<LocationDetailsResponseDTO> filterLocations (final Date startDate, final Date endDate, Long userId) throws BusinessException;
}
