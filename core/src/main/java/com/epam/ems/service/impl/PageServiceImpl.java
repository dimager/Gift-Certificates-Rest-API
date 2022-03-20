package com.epam.ems.service.impl;

import com.epam.ems.service.PageService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PageServiceImpl implements PageService {
    private static final String MSG_PAGE_NOT_FOUND = "30607";

    @Override
    public List<Link> createLinksWithNumberParameters(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, Number> extraParams) {
        HashMap<String, UriComponentsBuilder> uriMap = this.createUriMap(size, page, totalSize, link);
        if (!extraParams.isEmpty()) {
            extraParams.forEach((s, number) -> uriMap.forEach((s1, uriComponentsBuilder) -> uriComponentsBuilder.queryParam(s, number)));
        }
        List<Link> links = new ArrayList<>();
        uriMap.forEach((s, uriComponentsBuilder) -> links.add(Link.of(uriComponentsBuilder.build().toString()).withRel(s)));
        return links;
    }

    @Override
    public List<Link> createLinksWithStringParameters(int size, int page, long totalSize, WebMvcLinkBuilder link, HashMap<String, String> extraParams) {
        HashMap<String, UriComponentsBuilder> uriMap = this.createUriMap(size, page, totalSize, link);
        if (!extraParams.isEmpty()) {
            extraParams.forEach((s, name) -> uriMap.forEach((s1, uriComponentsBuilder) -> uriComponentsBuilder.queryParam(s, name)));
        }
        List<Link> links = new ArrayList<>();
        uriMap.forEach((s, uriComponentsBuilder) -> links.add(Link.of(uriComponentsBuilder.build().toString()).withRel(s)));
        return links;
    }

    @Override
    public List<Link> createLinks(int size, int page, long totalSize, WebMvcLinkBuilder link) {
        HashMap<String, UriComponentsBuilder> uriMap = this.createUriMap(size, page, totalSize, link);
        List<Link> links = new ArrayList<>();
        uriMap.forEach((s, uriComponentsBuilder) -> links.add(Link.of(uriComponentsBuilder.build().toString()).withRel(s)));
        return links;
    }

    private HashMap<String, UriComponentsBuilder> createUriMap(int size, int page, long totalSize, WebMvcLinkBuilder link) {
        long pagesCount = totalSize / size + 1;
        UriComponentsBuilder firstPage;
        UriComponentsBuilder lastPage;
        UriComponentsBuilder previousPage;
        UriComponentsBuilder nextPage;
        UriComponentsBuilder currentPage;
        HashMap<String, UriComponentsBuilder> map = new LinkedHashMap<>();

        if (page > 1) {
            firstPage = UriComponentsBuilder.fromUriString(link.toString())
                    .queryParam("size", size)
                    .queryParam("page", 1);
            map.put("firstPage", firstPage);
        }

        if (page > 2) {
            previousPage = UriComponentsBuilder.fromUriString(link.toString())
                    .queryParam("size", size)
                    .queryParam("page", page - 1);
            map.put("previousPage", previousPage);
        }

        currentPage = UriComponentsBuilder.fromUriString(link.toString())
                .queryParam("size", size)
                .queryParam("page", page);
        map.put("currentPage", currentPage);

        if (page < pagesCount) {
            nextPage = UriComponentsBuilder.fromUriString(link.toString())
                    .queryParam("size", size)
                    .queryParam("page", page + 1);
            map.put("nextPage", nextPage);
        }
        if (page < pagesCount - 1 && pagesCount > 2) {
            lastPage = UriComponentsBuilder.fromUriString(link.toString())
                    .queryParam("size", size)
                    .queryParam("page", pagesCount);
            map.put("lastPage", lastPage);
        }
        return map;
    }

    @Override
    public void isPageExist(int page, long totalSize, int offset) {
        if (offset > totalSize) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_PAGE_NOT_FOUND + page);
        }
    }

    @Override
    public int getOffset(int size, int page) {
        if (page > 0) {
            return page * size - size;
        } else {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_PAGE_NOT_FOUND + page);
        }
    }

}
