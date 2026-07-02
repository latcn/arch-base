package io.github.latcn.archbase.foundation.pagination;

public class PageQuery {
    private int pageNum = 1;
    private int pageSize = 10;
    private String sortField;
    private String sortOrder;

    public static PageQuery of(int pageNum, int pageSize) {
        PageQuery query = new PageQuery();
        query.pageNum = Math.max(1, pageNum);
        query.pageSize = Math.max(1, Math.min(100, pageSize));
        return query;
    }

    public static PageQuery of(int pageNum, int pageSize, String sortField, String sortOrder) {
        PageQuery query = of(pageNum, pageSize);
        query.sortField = sortField;
        query.sortOrder = sortOrder;
        return query;
    }

    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = Math.max(1, pageNum); }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = Math.max(1, Math.min(100, pageSize)); }
    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public long getOffset() {
        return (long) (pageNum - 1) * pageSize;
    }
}