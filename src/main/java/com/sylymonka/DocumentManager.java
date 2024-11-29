package com.sylymonka;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;


/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
class DocumentManager {
    private final static List<Document> documents = new ArrayList<>(); //storage for documents
    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if(document.getId() == null || document.getId().isEmpty()) {
            String id = UUID.randomUUID().toString();
            document.setId(id);
        }
        documents.add(document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        List<Document> searchResult = documents;
        if (request.titlePrefixes!=null && !request.titlePrefixes.isEmpty()) {
            searchResult = searchResult.stream()
                    .filter(doc->request.titlePrefixes.stream()
                            .anyMatch(prefix -> doc
                                    .getTitle()
                                    .startsWith(prefix)))
                    .toList();
        }

        if (request.containsContents!=null && !request.containsContents.isEmpty()) {
            searchResult = searchResult.stream()
                    .filter(doc -> request.containsContents.stream()
                            .anyMatch(content -> doc.content.contains(content))).toList();
        }

        if (request.authorIds!=null && !request.authorIds.isEmpty()) {
            searchResult = searchResult.stream().filter(doc->request.authorIds.contains(doc.getAuthor().getId())).toList();
        }

        if(request.createdFrom != null){
            searchResult = searchResult.stream().filter(doc ->request.createdFrom.isBefore(doc.created)).toList();
        }
        if(request.createdTo != null){
            searchResult = searchResult.stream().filter(doc ->request.createdTo.isAfter(doc.created)).toList();
        }

        return searchResult;
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {

        return documents.stream().filter(doc -> doc.getId().equals(id)).findFirst();
    }

    public void generateDocuments(){
        for (int i = 0; i < 7; i++) {
            save(
                Document.builder()
                    .title(i+" Title")
                    .content("Content of document "+ i)
                    .author(
                    Author.builder()
                            .id(UUID.randomUUID().toString())
                            .name("Author " + ((i % 3) + 1))
                            .build()
                    )
                    .created(Instant.now().minusSeconds(i * 3600))
                    .build()
            );
        }
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}