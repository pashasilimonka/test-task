package com.sylymonka;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
  @author   silim
  @project   Default (Template) Project
  @class  ${NAME}
  @version  1.0.0 
  @since 29.11.2024 - 18.15
*/public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();
        manager.generateDocuments();
        DocumentManager.Author author1 = DocumentManager.Author.builder()
                .id(UUID.randomUUID().toString())
                .name("Pavlo Sylymonka")
                .build();
        DocumentManager.Document savedDocument = manager.save(DocumentManager.Document.builder()
                .title("Test file 205")
                .content("Hi! This is a test for method! ")
                .author(author1)
                .created(Instant.now())
                .build());
        System.out.println(savedDocument.toString());

        DocumentManager.Document searchByIdResult = manager.findById(author1.getId()).orElse(null);
        System.out.println(searchByIdResult);

        List<DocumentManager.Document> emptyRequestResult = manager.search(DocumentManager.SearchRequest.builder().build());
        System.out.println("Result if request is empty: ");
        emptyRequestResult.forEach(doc -> System.out.println(doc + "\n"));

        List<DocumentManager.Document> titleRequestResult = manager
                .search(DocumentManager.SearchRequest
                        .builder()
                        .titlePrefixes(Arrays.asList("1", "6", "3", "4 Title"))
                        .build());
        System.out.println("Filter by title prefixes: ");
        titleRequestResult.forEach(doc -> System.out.println(doc + "\n"));

        List<DocumentManager.Document> authorRequestResult = manager
                .search(DocumentManager.SearchRequest
                        .builder()
                        .authorIds(Collections.singletonList(author1.getId()))
                        .build());
        System.out.println("Filter by author: ");
        authorRequestResult.forEach(doc -> System.out.println(doc + "\n"));

        List<DocumentManager.Document> timePeriodRequestResult = manager.search(DocumentManager.SearchRequest
                .builder()
                .createdFrom(Instant.now().minusSeconds(5*3600))
                .createdTo(Instant.now().minusSeconds(3600))
                .build());
        System.out.println("Filter from to: ");
        timePeriodRequestResult.forEach(doc -> System.out.println(doc + "\n"));

        List<DocumentManager.Document> multipleRequestsResult = manager
                .search(DocumentManager.SearchRequest
                        .builder()
                        .titlePrefixes(Arrays.asList("2", "3"))
                        .containsContents(Arrays.asList("3","4","5","1"))
                        .createdFrom(Instant.now().minusSeconds(4*3600))
                        .build());

        System.out.println("Multiple filters request: ");
        multipleRequestsResult.forEach(doc -> System.out.println(doc + "\n"));
    }
}