package wandrbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wandrbackend.entity.Rating;
import wandrbackend.services.RatingService;

import java.util.List;

@RestController
@RequestMapping("/memories/{memoryId}/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Rating> getAllRatingsByMemory(@PathVariable("memoryId") Long memoryId) {
        return ratingService.getAllRatingsByMemory(memoryId);
    }

    @GetMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.OK)
    public Rating getRatingById(@PathVariable("ratingId") Long ratingId) {
        return ratingService.getRatingById(ratingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating createRating(
            @PathVariable("memoryId") Long memoryId,
            @RequestParam Long userId,
            @RequestBody Rating rating) {
        return ratingService.createRating(userId, memoryId, rating);
    }

    @PutMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.OK)
    public Rating updateRating(@PathVariable("ratingId") Long ratingId, @RequestBody Rating rating) {
        return ratingService.updateRating(ratingId, rating);
    }

    @DeleteMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
    }
}
