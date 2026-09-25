package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.Memory;
import wandrbackend.entity.Rating;
import wandrbackend.entity.User;
import wandrbackend.entity.repository.MemoryJPARepository;
import wandrbackend.entity.repository.RatingJPARepository;
import wandrbackend.entity.repository.UserJPARepository;
import wandrbackend.exception.MemoryNotFoundException;
import wandrbackend.exception.RatingNotFoundException;
import wandrbackend.exception.UserNotFoundException;
import wandrbackend.services.RatingService;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final RatingJPARepository ratingRepository;
    private final UserJPARepository userRepository;
    private final MemoryJPARepository memoryRepository;

    @Autowired
    public RatingServiceImpl(RatingJPARepository ratingRepository,
                             UserJPARepository userRepository,
                             MemoryJPARepository memoryRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.memoryRepository = memoryRepository;
    }

    @Override
    public List<Rating> getAllRatingsByMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
        logger.info("Get ratings for memory {}", memoryId);
        return memory.getRatings();
    }

    @Override
    public Rating getRatingById(Long ratingId) {
        logger.info("Get rating {}", ratingId);
        return ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException("Rating not found : " + ratingId));
    }

    @Override
    public Rating createRating(Long userId, Long memoryId, Rating rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found : " + userId));
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));

        rating.setUser(user);
        rating.setMemory(memory);
        logger.info("Create rating by user {} for memory {}", userId, memoryId);
        return ratingRepository.save(rating);
    }

    @Override
    public Rating updateRating(Long ratingId, Rating updatedRating) {
        Rating existing = getRatingById(ratingId);
        existing.setValueForMoney(updatedRating.getValueForMoney());
        existing.setAtmosphereAndService(updatedRating.getAtmosphereAndService());
        existing.setQualityOfActivity(updatedRating.getQualityOfActivity());
        logger.info("Update rating {}", ratingId);
        return ratingRepository.save(existing);
    }

    @Override
    public void deleteRating(Long ratingId) {
        Rating existing = getRatingById(ratingId);
        logger.info("Delete rating {}", ratingId);
        ratingRepository.delete(existing);
    }
}