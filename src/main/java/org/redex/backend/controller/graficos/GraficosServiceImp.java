package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigInteger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.redex.backend.dao.dashboard.PaquetesVueloDAO;
import org.redex.backend.model.dashboard.PaquetesVuelo;
import org.redex.backend.repository.OficinasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@Service
@Transactional(readOnly = true)
public class GraficosServiceImp implements GraficosService {

    @Autowired
    PaquetesVueloDAO paquetesVueloDAO;
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    OficinasRepository oficinasRepository;

    @Override
    public ObjectNode paquetesXoficinaXfecha_linea(int id, String fechaI, String fechaF) {
        EntityManager em = emf.createEntityManager();
        String q = "select count(*),date(instante_registro) from paquete "
                + "where id_oficina_origen= " + id + " and instante_registro between STR_TO_DATE('" + fechaI + "','%d-%m-%Y') and STR_TO_DATE('" + fechaF + "','%d-%m-%Y') "
                + "group by date(instante_registro)";
        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q)
                .getResultList();

        Linea_POF lpof = new Linea_POF();
        List<POF> lAux = new ArrayList<POF>();
        lpof.setIdOficina(id);
        lpof.setFechaI(fechaI);
        lpof.setFechaF(fechaF);
        for (Object[] a : arr_cust) {
            POF pof = new POF();
            pof.setCantidad((int) a[0]);
            pof.setFecha((String) a[1]);
            lAux.add(pof);
        }
        lpof.setPof(lAux);
        ObjectNode graficoJson = JsonHelper.createJson(lpof, JsonNodeFactory.instance, new String[]{
            "idOficina",
            "fechaI",
            "fechaF",
            "pof.*"
        });
        return graficoJson;
    }

    @Override
    public List<PaquetesVuelo> paquetesXvuelosXfecha(LocalDate inicio, LocalDate fin) {
        return paquetesVueloDAO.top(inicio, fin, 10);
    }

    @Override
    public ArrayNode paquetesXoficinasXfecha_barra(String fechaI, String fechaF) {
        EntityManager em = emf.createEntityManager();
        String q = ""
                + " select pa.nombre, count(*) co"
                + " from paquete p"
                + "     inner join oficina o on p.id_oficina_origen = o.id "
                + "     inner join pais pa on o.id_pais = pa.id "
                + " where"
                + "     p.instante_registro between STR_TO_DATE('" + fechaI + "','%d-%m-%Y') and STR_TO_DATE('" + fechaF + "','%d-%m-%Y') "
                + " group by o.id  "
                + " order by co desc ";
        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q)
                .getResultList();

        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Object[] a : arr_cust) {
            ObjectNode item = new ObjectNode(JsonNodeFactory.instance);
            item.put("pais", (String) a[0]);
            item.put("cantidad", (BigInteger) a[1]);
            arr.add(item);
        }
        return arr;
    }

}
