package com.epam.ems.service;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.HashMap;
import java.util.List;

public interface PageService {

    List<Link> createLinks(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, Number> extraParams);

    List<Link> createLinksWithStringParameters(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, String> extraParams);

    List<Link> createLinks(int size, int page, long totalSize, WebMvcLinkBuilder link);

    void isPageExist(int page, long totalSize, int offset);

    int getOffset(int size, int page);
}
