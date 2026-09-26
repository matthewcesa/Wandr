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
import wandrbackend.entity.Tag;
import wandrbackend.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/memories/{memoryId}/tags")
public class TagController {
	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Tag> getAllTagsByMemory(@PathVariable("memoryId") Long memoryId) {
		return tagService.getAllTagsByMemory(memoryId);
	}

	@GetMapping("/{tagId}")
	@ResponseStatus(HttpStatus.OK)
	public Tag getTagById(@PathVariable("tagId") Long tagId) {
		return tagService.getTagById(tagId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Tag createTag(@PathVariable("memoryId") Long memoryId, @RequestBody Tag tag) {
		return tagService.createTag(memoryId, tag);
	}

	@PutMapping("/{tagId}")
	@ResponseStatus(HttpStatus.OK)
	public Tag updateTag(@PathVariable("tagId") Long tagId, @RequestBody Tag tag) {
		return tagService.updateTag(tagId, tag);
	}

	@DeleteMapping("/{tagId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTag(@PathVariable("tagId") Long tagId) {
		tagService.deleteTag(tagId);
	}
}
