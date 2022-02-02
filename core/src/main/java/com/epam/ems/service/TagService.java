package com.epam.ems.service;

import com.epam.ems.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * This interface allows to manage tags.
 */
@Service
public interface TagService {

    /**
     * Allows getting list of all {@link Tag tags} from DB.
     *  @return List of {@link Tag tags}.
     */
    List<Tag> getAllTags();


    /**
     * Allows getting {@link Tag} by id from DB.
     * @param id Tag`s id.
     * @return {@link Tag} entity
     */
    Tag getTag(long id);


    /**
     * Allows updating {@link Tag} entity in DB.
     * @param tag tag with new data.
     * @return updated {@link Tag}.
     */
    Tag updateTag(Tag tag);


    /**
     * Allows creating tag entity in DB.
     * @param tag new {@link Tag}
     * @return tag with generated {@link Tag} id.
     */
    Tag createTag(Tag tag);

    /**
     * Allows getting {@link Tag tag} by name.
     * @param name of {@link Tag tag}which should be found.
     * @return tag
     */
    Tag getTag(String name);


    /**
     * Allows deleting {@link Tag} from DB
     * @param id Tag`s id.
     * @return true - if tag is deleted,
     */
    boolean deleteTag(long id);

    /**
     * This method helps to check tag existence in DB  and update or create it, depends on checking result:
     * If Tag is existed in DB by id: if needed update name, return tag;
     * If Tag is not existed in DB by id, check tag existence by name, return tag with id;
     * Otherwise create tag and return tag.
     *
     * @param tag tag
     * @return tag
     */
    Tag checkTagForExistenceInDatabase(Tag tag);


    /**
     * Allows checking tag existence by name
     * @param name {@link Tag} pattern
     * @return true - if ta is found, otherwise false
     */
    boolean isTagExistByName(String name);
}
