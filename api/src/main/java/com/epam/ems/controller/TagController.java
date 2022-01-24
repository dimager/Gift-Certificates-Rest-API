package com.epam.ems.controller;

import com.epam.ems.dao.exception.TagNotFoundException;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable long id) {
        try {
            return tagService.getTag(id);
        } catch (Exception e) {
            throw new TagNotFoundException(HttpStatus.BAD_GATEWAY, "Tag was not found");
        }
    }

    @PostMapping("/tags")
    public Tag addTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @PostMapping("/tags/{id}")
    public Tag updateTag(@PathVariable long id, @RequestBody Tag tag) {
        tag.setId(id);
        return tagService.updateTag(tag);
    }


}
