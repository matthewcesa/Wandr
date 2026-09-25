package wandrbackend.services;


import wandrbackend.entity.Rating;
import java.util.List;

public interface RatingService {
    List<Rating> getAllRatingsByMemory(Long memoryId);
    Rating getRatingById(Long ratingId);
    Rating createRating(Long userId, Long memoryId, Rating rating);
    Rating updateRating(Long ratingId, Rating updatedRating);
    void deleteRating(Long ratingId);
}
