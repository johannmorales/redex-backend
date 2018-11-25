package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.redex.backend.controller.reportes.RangoFechas;
import org.redex.backend.model.dashboard.PaquetesVuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.albatross.zelpers.miscelanea.JsonHelper;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("dashboard")
public class GraficosController {

    @Autowired
    GraficosService service;

    @PostMapping("/paquetesXoficinaXfecha_linea")
    public ResponseEntity<?> paquetesXoficinaXfecha_linea(@RequestBody ObjectNode payload) {
        String fechaInicio = payload.get("fecha_ini").asText();
        String fechaFin = payload.get("fecha_fin").asText();
        int idOficina = payload.get("idOf").asInt();

        ObjectNode s = service.paquetesXoficinaXfecha_linea(idOficina, fechaInicio, fechaFin);
        return ResponseEntity.ok(s);
    }

    @GetMapping("paquetesXvuelosXfecha")
    public ResponseEntity<?> paquetesXvuelosXfecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<PaquetesVuelo> data = service.paquetesXvuelosXfecha(inicio, fin);

        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        for (PaquetesVuelo datum : data) {
            ObjectNode item = new ObjectNode(JsonNodeFactory.instance);
            item.put("vuelo", String.format("%s - %s", datum.getVuelo().getOficinaOrigen().getPais().getNombre(), datum.getVuelo().getOficinaDestino().getPais().getNombre()));
            item.put("capacidad", datum.getCantidad());
            item.put("horaInicio", datum.getVuelo().getHoraInicioString());
            item.put("horaFin", datum.getVuelo().getHoraFinString());
            array.add(item);
        }

        return ResponseEntity.ok(array);
    }

    @PostMapping("/paquetesXoficinasXfecha_barra")
    public ResponseEntity<?> paquetesXoficinasXfecha_barra(@RequestBody RangoFechas rf) {
        ArrayNode arr = service.paquetesXoficinasXfecha_barra(rf.fecha_ini, rf.fecha_fin);
        return ResponseEntity.ok(arr);
    }
}
