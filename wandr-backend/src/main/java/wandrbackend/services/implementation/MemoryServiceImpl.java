package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.Memory;
import wandrbackend.entity.Trip;
import wandrbackend.entity.repository.MemoryJPARepository;
import wandrbackend.entity.repository.TripJPARepository;
import wandrbackend.exception.MemoryNotFoundException;
import wandrbackend.exception.TripNotFoundException;
import wandrbackend.services.MemoryService;

import java.util.List;

@Service
public class MemoryServiceImpl implements MemoryService {

    private static final Logger logger = LoggerFactory.getLogger(MemoryServiceImpl.class);

    private final MemoryJPARepository memoryRepository;
    private final TripJPARepository tripRepository;

    @Autowired
    public MemoryServiceImpl(MemoryJPARepository memoryRepository, TripJPARepository tripRepository) {
        this.memoryRepository = memoryRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Memory> getAllMemoriesByTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found: " + tripId));
        logger.info("Get memories for trip {}", tripId);
        return trip.getMemories();
    }

    @Override
    public Memory getMemoryById(Long memoryId) {
        logger.info("Get memory {}", memoryId);
        return memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
    }

    @Override
    public Memory createMemory(Long tripId, Memory memory) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found : " + tripId));
        memory.setTrip(trip);
        logger.info("Create memory '{}' for trip {}", memory.getTitle(), tripId);
        return memoryRepository.save(memory);
    }

    @Override
    public Memory updateMemory(Long memoryId, Memory updatedMemory) {
        Memory existing = getMemoryById(memoryId);
        existing.setTitle(updatedMemory.getTitle());
        existing.setType(updatedMemory.getType());
        existing.setDescription(updatedMemory.getDescription());
        existing.setVisitedAt(updatedMemory.getVisitedAt());
        existing.setAddress(updatedMemory.getAddress());
        logger.info("Update memory {}", memoryId);
        return memoryRepository.save(existing);
    }

    @Override
    public void deleteMemory(Long memoryId) {
        Memory existing = getMemoryById(memoryId);
        logger.info("Delete memory {}", memoryId);
        memoryRepository.delete(existing);
    }
}