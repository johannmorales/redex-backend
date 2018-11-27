package org.redex.backend.controller.vuelosagendados;

import javax.validation.Valid;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.security.CurrentUser;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vuelosagendados")
public class VuelosAgendadosController {

    @Autowired
    VuelosAgendadosService service;

    @GetMapping
    public CrimsonTableResponse crimsonList(@Valid CrimsonTableRequest request, @CurrentUser DataSession ds) {
        Page<VueloAgendado> list = service.crimsonList(request, ds);

        return CrimsonTableResponse.of(list, new String[]{
            "id",
            "vuelo.id",
            "vuelo.oficinaOrigen.id",
            "vuelo.oficinaOrigen.codigo",
            "vuelo.oficinaOrigen.pais.id",
            "vuelo.oficinaOrigen.pais.nombre",
            "vuelo.oficinaDestino.id",
            "vuelo.oficinaDestino.codigo",
            "vuelo.oficinaDestino.pais.id",
            "vuelo.oficinaDestino.pais.nombre",
            "capacidadActual",
            "capacidadMaxima",
            "estado",
            "fechaInicioString",
            "fechaFinString"
        });
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generar(@RequestBody GenerarPayload payload) {
        Integer registros = service.generar(payload);
        return ResponseEntity.ok(registros);
    }

    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminar() {
        service.eliminarInnecesarios();
        return ResponseEntity.ok("Vuelos agendados eliminados");
    }

}
