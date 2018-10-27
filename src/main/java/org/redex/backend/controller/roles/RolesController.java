package org.redex.backend.controller.roles;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import javax.validation.Valid;
import org.redex.backend.model.seguridad.Rol;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("/roles")
public class RolesController {
    
    @Autowired
    RolesService service;
    
    @GetMapping
    public CrimsonTableResponse crimsonList(@Valid CrimsonTableRequest request){
        Page<Rol> list = service.crimsonList(request);
        return CrimsonTableResponse.of(list, new String[]{
            "*"
        });
    }
    
    @GetMapping("all")
    public ArrayNode all(){
        List<Rol> list = service.all();
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Rol rol : list) {
            arr.add(JsonHelper.createJson(rol, JsonNodeFactory.instance));
        }
        return arr;
    }
}
