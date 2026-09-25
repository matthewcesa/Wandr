package wandrbackend.services;

import wandrbackend.entity.Trip;
import java.util.List;

public interface TripService {
    List<Trip> getAllTripsByUser(Long userId);
    Trip getTripById(Long tripId);
    Trip createTrip(Long userId, Trip trip);
    Trip updateTrip(Long tripId, Trip updatedTrip);
    void deleteTrip(Long tripId);
}