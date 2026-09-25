package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.Trip;
import wandrbackend.entity.User;
import wandrbackend.entity.repository.TripJPARepository;
import wandrbackend.entity.repository.UserJPARepository;
import wandrbackend.exception.TripNotFoundException;
import wandrbackend.exception.UserNotFoundException;
import wandrbackend.services.TripService;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private static final Logger logger = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripJPARepository tripRepository;
    private final UserJPARepository userRepository;

    @Autowired
    public TripServiceImpl(TripJPARepository tripRepository, UserJPARepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Trip> getAllTripsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found : " + userId));
        logger.info("Get trips for user {}", userId);
        return user.getTrips();
    }

    @Override
    public Trip getTripById(Long tripId) {
        logger.info("Get trip {}", tripId);
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found  " + tripId));
    }

    @Override
    public Trip createTrip(Long userId, Trip trip) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found : " + userId));
        trip.setUser(user);
        logger.info("Create trip '{}' for user {}", trip.getTitle(), userId);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTrip(Long tripId, Trip updatedTrip) {
        Trip existing = getTripById(tripId);
        existing.setTitle(updatedTrip.getTitle());
        existing.setCountry(updatedTrip.getCountry());
        existing.setCity(updatedTrip.getCity());
        existing.setStartDate(updatedTrip.getStartDate());
        existing.setEndDate(updatedTrip.getEndDate());
        existing.setDescription(updatedTrip.getDescription());
        existing.setCover(updatedTrip.getCover());
        existing.setPlannedBudget(updatedTrip.getPlannedBudget());
        existing.setStatus(updatedTrip.getStatus());
        logger.info("UPdate trip {}", tripId);
        return tripRepository.save(existing);
    }

    @Override
    public void deleteTrip(Long tripId) {
        Trip existing = getTripById(tripId);
        logger.info("Delete trip {}", tripId);
        tripRepository.delete(existing);
    }
}