package com.example.O_Way.common.response;

import lombok.Data;

@Data
public class PaginationMeta {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private String method;
    private String endpoint;
}


