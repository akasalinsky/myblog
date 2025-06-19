package org.example.model;

public record Paging(int pageNumber, int pageSize, long total) {
    public Paging(int pageNumber, int pageSize) {
        this(pageNumber, pageSize, 0);
    }

    public int offset() {
        return (pageNumber - 1) * pageSize;
    }

    public boolean hasNext() {
        return pageNumber * pageSize < total;
    }

    public boolean hasPrevious() {
        return pageNumber > 1;
    }

    public Paging withTotal(long total) {
        return new Paging(pageNumber, pageSize, total);
    }
}