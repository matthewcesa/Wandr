package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.Memory;
import wandrbackend.entity.Photo;
import wandrbackend.entity.repository.MemoryJPARepository;
import wandrbackend.entity.repository.PhotoJPARepository;
import wandrbackend.exception.MemoryNotFoundException;
import wandrbackend.exception.PhotoNotFoundException;
import wandrbackend.services.PhotoService;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoJPARepository photoRepository;
    private final MemoryJPARepository memoryRepository;

    @Autowired
    public PhotoServiceImpl(PhotoJPARepository photoRepository, MemoryJPARepository memoryRepository) {
        this.photoRepository = photoRepository;
        this.memoryRepository = memoryRepository;
    }

    @Override
    public List<Photo> getAllPhotosByMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
        logger.info("Get photos for memory {}", memoryId);
        return memory.getPhotos();
    }

    @Override
    public Photo getPhotoById(Long photoId) {
        logger.info("Get photo {}", photoId);
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found : " + photoId));
    }

    @Override
    public Photo createPhoto(Long memoryId, Photo photo) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
        photo.setMemory(memory);
        logger.info("Create photo for memory {}", memoryId);
        return photoRepository.save(photo);
    }

    @Override
    public Photo updatePhoto(Long photoId, Photo updatedPhoto) {
        Photo existing = getPhotoById(photoId);
        existing.setUrl(updatedPhoto.getUrl());
        existing.setCaption(updatedPhoto.getCaption());
        existing.setTakenAt(updatedPhoto.getTakenAt());
        existing.setPosition(updatedPhoto.getPosition());
        logger.info("Update photo {}", photoId);
        return photoRepository.save(existing);
    }

    @Override
    public void deletePhoto(Long photoId) {
        Photo existing = getPhotoById(photoId);
        logger.info("Delete photo {}", photoId);
        photoRepository.delete(existing);
    }
}