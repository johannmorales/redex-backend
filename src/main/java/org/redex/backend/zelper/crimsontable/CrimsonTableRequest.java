package org.redex.backend.zelper.crimsontable;

import javax.validation.constraints.NotNull;

public class CrimsonTableRequest {

    @NotNull
    private Integer current;

    @NotNull
    private Integer pageSize;

    private String search;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
