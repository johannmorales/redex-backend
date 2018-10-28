package org.redex.backend.zelper.response;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pe.albatross.zelpers.miscelanea.JsonHelper;

public class ApplicationResponse {

    private String msg;
    
    private ObjectNode data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjectNode getData() {
        return data;
    }

    public void setData(ObjectNode data) {
        this.data = data;
    }
    
    public static ApplicationResponse of(String msg){
        ApplicationResponse ar = new ApplicationResponse();
        ar.setMsg(msg);
        return ar;
    }
    
    public static ApplicationResponse of(String msg, ObjectNode data){
        ApplicationResponse ar = new ApplicationResponse();
        ar.setMsg(msg);
        ar.setData(data);
        return ar;
    }
    
    public static ApplicationResponse of(String msg, Object data){
        ApplicationResponse ar = new ApplicationResponse();
        ar.setMsg(msg);
        ar.setData(JsonHelper.createJson(data, JsonNodeFactory.instance));
        return ar;
    }       
}
