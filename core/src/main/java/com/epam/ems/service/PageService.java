package com.epam.ems.service;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.HashMap;
import java.util.List;

public interface PageService {

    /**
     * Allows greating links for object
     *
     * @param size        size of page
     * @param page        number of current page
     * @param totalSize   total elements
     * @param link        links
     * @param extraParams params for links
     * @return list of links
     */
    List<Link> createLinksWithNumberParameters(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, Number> extraParams);


    /**
     * Allows greating links for object
     *
     * @param size        size of page
     * @param page        number of current page
     * @param totalSize   total elements
     * @param link        links
     * @param extraParams params for links
     * @return list of links
     */
    List<Link> createLinksWithStringParameters(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, String> extraParams);

    /**
     * Allows greating links for object
     *
     * @param size      size of page
     * @param page      number of current page
     * @param totalSize total elements
     * @param link      links
     * @return list of links
     */
    List<Link> createLinks(int size, int page, long totalSize, WebMvcLinkBuilder link);


    /**
     * Allows checking page for existence
     *
     * @param page      page number
     * @param totalSize total size of elements
     * @param offset    page offset
     */
    void isPageExist(int page, long totalSize, int offset);

    /**
     * Allows getting page offset
     *
     * @param size element of page
     * @param page number of page
     * @return offset
     */

    int getOffset(int size, int page);
}
