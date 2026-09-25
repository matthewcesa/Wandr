package wandrbackend.services;

import wandrbackend.entity.Photo;
import java.util.List;

public interface PhotoService {
    List<Photo> getAllPhotosByMemory(Long memoryId);
    Photo getPhotoById(Long photoId);
    Photo createPhoto(Long memoryId, Photo photo);
    Photo updatePhoto(Long photoId, Photo updatedPhoto);
    void deletePhoto(Long photoId);
}