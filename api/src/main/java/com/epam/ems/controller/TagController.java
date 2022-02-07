package com.epam.ems.controller;

import com.epam.ems.entity.Tag;
import com.epam.ems.handler.ValidationHandler;
import com.epam.ems.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final ValidationHandler validationHandler;

    @Autowired
    public TagController(TagService tagService, ValidationHandler validationHandler) {
        this.tagService = tagService;
        this.validationHandler = validationHandler;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("{id}")
    public Tag getTag(@PathVariable long id) {
        return tagService.getTag(id);
    }


    @PostMapping(consumes = {"application/json"})
    public Tag addTag(@RequestBody @Valid Tag tag, BindingResult bindingResult) {
        validationHandler.handleBindingResult(bindingResult);
        return tagService.createTag(tag);
    }

    @PutMapping("{id}")
    public Tag updateTag(@PathVariable long id, @RequestBody @Valid Tag tag, BindingResult bindingResult) {
        validationHandler.handleBindingResult(bindingResult);
        tag.setId(id);
        return tagService.updateTag(tag);
    }

    @DeleteMapping("{id}")
    public boolean deleteTag(@PathVariable long id) {
        return tagService.deleteTag(id);
    }
}
