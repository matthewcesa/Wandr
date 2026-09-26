package wandrbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wandrbackend.entity.Photo;
import wandrbackend.services.PhotoService;

import java.util.List;

@RestController
@RequestMapping("/memories/{memoryId}/photos")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Photo> getAllPhotosByMemory(@PathVariable("memoryId") Long memoryId) {
        return photoService.getAllPhotosByMemory(memoryId);
    }

    @GetMapping("/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    public Photo getPhotoById(@PathVariable("photoId") Long photoId) {
        return photoService.getPhotoById(photoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Photo createPhoto(@PathVariable("memoryId") Long memoryId, @RequestBody Photo photo) {
        return photoService.createPhoto(memoryId, photo);
    }

    @PutMapping("/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    public Photo updatePhoto(@PathVariable("photoId") Long photoId, @RequestBody Photo photo) {
        return photoService.updatePhoto(photoId, photo);
    }

    @DeleteMapping("/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhoto(@PathVariable("photoId") Long photoId) {
        photoService.deletePhoto(photoId);
    }
}
