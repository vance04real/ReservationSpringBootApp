package com.vancekay.reservations.repository;

import com.vancekay.reservations.domain.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Evans K F C on May ,2019
 **/

@Repository
public interface RoomRepository  extends CrudRepository<Room,Long> {

    Room findByNumber(String number);
}
