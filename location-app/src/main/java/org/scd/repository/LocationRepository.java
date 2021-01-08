package org.scd.repository;


import org.scd.model.Location;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LocationRepository extends CrudRepository <Location , Long> {
    /**
     * find locations by user id
     * @param userId
     * @return
     */
   List<Location> findByUserId (Long userId );


   Location save (Location location);
}
