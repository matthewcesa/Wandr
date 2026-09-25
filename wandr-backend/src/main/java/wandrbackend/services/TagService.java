package wandrbackend.services;

import wandrbackend.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> getAllTagsByMemory(Long memoryId);
    Tag getTagById(Long tagId);
    Tag createTag(Long memoryId, Tag tag);
    Tag updateTag(Long tagId, Tag updatedTag);
    void deleteTag(Long tagId);
}