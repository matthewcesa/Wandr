package wandrbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wandrbackend.entity.Trip;
import wandrbackend.exception.TripNotFoundException;
import wandrbackend.exception.UserNotFoundException;
import wandrbackend.services.implementation.TripServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripServiceImpl tripServiceImpl;

    @Autowired
    public TripController(TripServiceImpl tripServiceImpl){
        this.tripServiceImpl = tripServiceImpl;
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Trip> getAllTripsByUser(@PathVariable("userId") Long userId)  {
        return this.tripServiceImpl.getAllTripsByUser(userId);
    }

    @GetMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public Trip getTripById(@PathVariable("tripId") Long tripId)  {
        return this.tripServiceImpl.getTripById(tripId);
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Trip createTrip(@PathVariable("userId") Long userId, @RequestBody Trip trip){
        return this.tripServiceImpl.createTrip(userId, trip);
    }

    @PutMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public Trip updateTrip(@PathVariable("tripId") Long tripId, @RequestBody Trip updatedTrip){
        return this.tripServiceImpl.updateTrip(tripId, updatedTrip);
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrip(@PathVariable("tripId") Long tripId){
        this.tripServiceImpl.deleteTrip(tripId);
    }
}

