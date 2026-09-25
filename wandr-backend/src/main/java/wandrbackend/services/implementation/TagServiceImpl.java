package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.Memory;
import wandrbackend.entity.Tag;
import wandrbackend.entity.repository.MemoryJPARepository;
import wandrbackend.entity.repository.TagJPARepository;
import wandrbackend.exception.MemoryNotFoundException;
import wandrbackend.exception.TagNotFoundException;
import wandrbackend.services.TagService;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagJPARepository tagRepository;
    private final MemoryJPARepository memoryRepository;

    @Autowired
    public TagServiceImpl(TagJPARepository tagRepository, MemoryJPARepository memoryRepository) {
        this.tagRepository = tagRepository;
        this.memoryRepository = memoryRepository;
    }

    @Override
    public List<Tag> getAllTagsByMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
        logger.info("Get tags for memory {}", memoryId);
        return memory.getTags();
    }

    @Override
    public Tag getTagById(Long tagId) {
        logger.info("Get tag {}", tagId);
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag not found : " + tagId));
    }

    @Override
    public Tag createTag(Long memoryId, Tag tag) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new MemoryNotFoundException("Memory not found : " + memoryId));
        tag.setMemory(memory);
        logger.info("Craete tag for memory {}", memoryId);
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long tagId, Tag updatedTag) {
        Tag existing = getTagById(tagId);
        existing.setName(updatedTag.getName());
        existing.setColor(updatedTag.getColor());
        logger.info("Update tag {}", tagId);
        return tagRepository.save(existing);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag existing = getTagById(tagId);
        logger.info("Delete tag {}", tagId);
        tagRepository.delete(existing);
    }
}