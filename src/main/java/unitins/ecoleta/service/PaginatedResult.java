package unitins.ecoleta.service;

import java.util.List;

public record PaginatedResult<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {}
