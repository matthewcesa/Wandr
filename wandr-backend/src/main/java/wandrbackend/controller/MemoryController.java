package wandrbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wandrbackend.entity.Memory;
import wandrbackend.services.implementation.MemoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/memories")
public class MemoryController {

    private final MemoryServiceImpl memoryServiceImpl;

    @Autowired
    public MemoryController(MemoryServiceImpl memoryServiceImpl){
        this.memoryServiceImpl = memoryServiceImpl;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Memory> getAllMemoriesByTrip(@PathVariable("tripId") Long tripId){
        return this.memoryServiceImpl.getAllMemoriesByTrip(tripId);
    }

    @GetMapping("/{memoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Memory getMemoryById(@PathVariable("memoryId") Long memoryId){
        return this.memoryServiceImpl.getMemoryById(memoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Memory createMemory(@PathVariable("tripId") Long tripId, @RequestBody Memory memory){
        return this.memoryServiceImpl.createMemory(tripId, memory);
    }

    @PutMapping("/{memoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Memory updateMemory(@PathVariable("memoryId") Long memoryId, @RequestBody Memory memory){
        return this.memoryServiceImpl.updateMemory(memoryId, memory);
    }

    @DeleteMapping("/{memoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemory(@PathVariable("memoryId") Long memoryId) {
        this.memoryServiceImpl.deleteMemory(memoryId);
    }
}