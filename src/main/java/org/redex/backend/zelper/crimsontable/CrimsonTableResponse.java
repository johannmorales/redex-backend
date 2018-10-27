package org.redex.backend.zelper.crimsontable;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.data.domain.Page;
import pe.albatross.zelpers.miscelanea.JsonHelper;

public class CrimsonTableResponse {
    
    private Integer current;
    
    private Long total;
    
    private ArrayNode data;

    public static CrimsonTableResponse of(Page<?> page, String[] attrs){
        CrimsonTableResponse response = new CrimsonTableResponse();
        response.setCurrent(page.getNumber());
        response.setTotal(page.getTotalElements());
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Object object : page) {
            arr.add(JsonHelper.createJson(object, JsonNodeFactory.instance, attrs));
        }
        response.setData(arr);
        return response;
    }
    
    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public ArrayNode getData() {
        return data;
    }

    public void setData(ArrayNode data) {
        this.data = data;
    }
    
}
