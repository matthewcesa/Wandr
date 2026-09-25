package wandrbackend.services;

import wandrbackend.entity.Memory;
import java.util.List;

public interface MemoryService {
    List<Memory> getAllMemoriesByTrip(Long tripId);
    Memory getMemoryById(Long memoryId);
    Memory createMemory(Long tripId, Memory memory);
    Memory updateMemory(Long memoryId, Memory updatedMemory);
    void deleteMemory(Long memoryId);
}