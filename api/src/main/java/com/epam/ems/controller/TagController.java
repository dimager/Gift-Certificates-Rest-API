package com.epam.ems.controller;

import com.epam.ems.dto.TagDTO;
import com.epam.ems.dto.converter.DtoConverter;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final DtoConverter dtoConverter;
    @Autowired
    public TagController(TagService tagService, DtoConverter dtoConverter) {
        this.tagService = tagService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Allows getting all tags
     *
     * @param size number of tags per page
     * @param page number of page
     * @return list of tags
     */
    @GetMapping
    public CollectionModel<Tag> getAllTags(@RequestParam(name = "size", defaultValue = "10") int size,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        CollectionModel<Tag> tags = tagService.getAllTags(size, page, linkTo(TagController.class));
        for (Tag tag : tags.getContent()) {
            this.createLinks(tag);
        }
        return tags;
    }


    /**
     * Allows getting tag info
     *
     * @param id tag id
     * @return tag
     */
    @GetMapping("{id}")
    public Tag getTag(@PathVariable long id) {
        Tag tag = tagService.getTag(id);
        this.createLinks(tag);
        return tag;
    }

    /**
     * Allows creating tag
     *
     * @param tagDTO tag data
     * @return created tag with id
     */
    @PostMapping(consumes = {"application/json"})
    @PreAuthorize("hasAuthority('tag:write')")
    public Tag addTag(@RequestBody @Valid TagDTO tagDTO) {
        Tag tag = dtoConverter.convertToEntity(tagDTO);
        tag = tagService.createTag(tag);
        createLinks(tag);
        return tag;
    }

    /**
     * Allows updating tag
     *
     * @param id  tag id
     * @param tagDTO tag data
     * @return updated tag
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('tag:write')")
    public Tag updateTag(@PathVariable long id, @RequestBody @Valid TagDTO tagDTO) {
        Tag tag = dtoConverter.convertToEntity(tagDTO);
        tag.setId(id);
        tag = tagService.updateTag(tag);
        this.createLinks(tag);
        return tag;
    }

    /**
     * Allows getting most popular tag
     *
     * @return tag
     */
    @GetMapping("mostpopular")
    public Tag mostPopuparTag() {
        Tag tag = tagService.getMostPopularTag();
        this.createLinks(tag);
        return tag;
    }

    /**
     * Allows deleting tag by id
     *
     * @param id tag id
     * @return true
     */

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('tag:write')")
    public ResponseEntity<Tag> deleteTag(@PathVariable long id) {
        if (tagService.deleteTag(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void createLinks(Tag tag) {
        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
        tag.add(Link.of(linkTo(CertificatesController.class)
                        .toUriComponentsBuilder().queryParam("tags", tag.getName()).build().toString())
                .withRel("Certificates"));
    }

}
