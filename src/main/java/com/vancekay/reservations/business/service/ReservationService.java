package com.vancekay.reservations.business.service;

import com.vancekay.reservations.business.domain.RoomReservation;
import com.vancekay.reservations.domain.Guest;
import com.vancekay.reservations.domain.Reservation;
import com.vancekay.reservations.domain.Room;
import com.vancekay.reservations.repository.GuestRepository;
import com.vancekay.reservations.repository.ReservationRepository;
import com.vancekay.reservations.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evans K F C on May ,2019
 **/

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }


    public List<RoomReservation> getRoomReservationsForDate(String dateString) {

        Date date = this.createDateFromString(dateString);

        Iterable<Room> rooms = this.roomRepository.findAll();

        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();

        rooms.forEach(room -> {

            RoomReservation roomReservation = new RoomReservation();

            roomReservation.setRoomId(room.getId());
            roomReservation.setFirstName(room.getName());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());

            roomReservationMap.put(room.getId(), roomReservation);
        });

        Iterable<Reservation> reservations = this.reservationRepository.findByDate(new java.sql.Date(date.getTime()));

        if (reservations != null) {

            reservations.forEach(reservation -> {

                Optional<Guest> guestResponse = this.guestRepository.findById(reservation.getGuestId());

                if (guestResponse.isPresent()) {

                    Guest guest = guestResponse.get();

                    final RoomReservation roomReservation = roomReservationMap.get(reservation.getId());
                    roomReservation.setDate(date);
                    roomReservation.setFirstName(guest.getFirstName());
                    roomReservation.setLastName(guest.getLastName());
                    roomReservation.setGuestId(guest.getId());

                }
            });
        }

        List<RoomReservation> roomReservationsList = new ArrayList<>();

        for (Long roomId : roomReservationMap.keySet()) {

            roomReservationsList.add(roomReservationMap.get(roomId));
        }

        return roomReservationsList;
    }

    private Date createDateFromString(String dateString) {

        Date date = null;

        if (dateString != null) {
            try {
                date = DATE_FORMAT.parse(dateString);
            } catch (ParseException p) {
                date = new Date();
            }
        } else {

            date = new Date();
        }

        return date;
    }

}
