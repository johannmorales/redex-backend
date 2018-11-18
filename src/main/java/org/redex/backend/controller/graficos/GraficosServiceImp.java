/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.apache.poi.ss.usermodel.Row;
import static org.hibernate.internal.util.collections.CollectionHelper.arrayList;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.repository.OficinasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.albatross.zelpers.miscelanea.JsonHelper;

/**
 *
 * @author Oscar
 */
@Service
@Transactional(readOnly = true)
public class GraficosServiceImp implements GraficosService{
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Autowired
    OficinasRepository oficinasRepository;

    
    @Override
    public ObjectNode paquetesXoficinaXfecha_linea(int id,String fechaI,String fechaF){
        EntityManager em = emf.createEntityManager();
        String q = "select count(*),date(instante_registro) from paquete " +
            "where id_oficina_origen= "+ id +"and instante_registro between '"+fechaI+"' and '"+fechaF+"' " +
            "group by date(instante_registro);";
        List<Object[]> arr_cust = (List<Object[]>)em.createNativeQuery(q)
                              .getResultList();
        
        Linea_POF lpof = new Linea_POF();
        List<POF> lAux = new ArrayList<POF>();
        lpof.setIdOficina(id);
        lpof.setFechaI(fechaI);
        lpof.setFechaF(fechaF);
        for (Object[] a : arr_cust) {
            POF pof = new POF();
            pof.setCantidad((int)a[0]);
            pof.setFecha((String)a[1]);
            lAux.add(pof);
        }
        lpof.setPof(lAux);
        ObjectNode graficoJson = JsonHelper.createJson(lpof, JsonNodeFactory.instance, new String []{
          "idOficina",
          "fechaI",
          "fechaF",
          "pof.*"
        });
        return graficoJson;
    }
    
    @Override
    public ObjectNode paquetesXvuelosXfecha(String fechaI,String fechaF){
        
        Map<Long, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getId(), oficina -> oficina));
        
        EntityManager em = emf.createEntityManager();
        String q = "select count(*), id_oficina_origen from paquete " +
            "where  instante_registro between '"+fechaI+"' and '"+fechaF+"'" +
            "group by id_oficina_origen ;";
        List<Object[]> arr_cust = (List<Object[]>)em.createQuery(q)
                              .getResultList();
        Grafico_PVF gpvf = new Grafico_PVF();
        List<PVF> lAux = new ArrayList<PVF>();
        gpvf.setFechaI(fechaI);
        gpvf.setFechaF(fechaF);
        for (Object[] a : arr_cust) {
            PVF pvf = new PVF();
            pvf.setCantidad((int)a[0]);
            pvf.setOficina(oficinas.get((int)a[1]).getCodigo());
            lAux.add(pvf);
        }
        gpvf.setPvf(lAux);
        ObjectNode graficoJson = JsonHelper.createJson(gpvf, JsonNodeFactory.instance, new String []{
          "fechaI",
          "fechaF",
          "pvf.*"
        });
        return graficoJson;
    }
    
    @Override
    public ObjectNode paquetesXoficinasXfecha_barra(String fechaI,String fechaF){
        EntityManager em = emf.createEntityManager();
        String q = "select sum(va.capacidad_actual) Suma,o1.codigo Inicio,o2.codigo Fin,time(v.hora_inicio) as hora  " +
            "from vuelo_agendado va, vuelo v, plan_vuelo pv,oficina o1, " +
            "oficina o2 " +
            "where va.id_vuelo = v.id and pv.estado = 'ACTIVO' and v.id_plan_vuelo=pv.id " +
            "and v.id_oficina_origen = o1.id and v.id_oficina_destino = o2.id and va.fecha_fin  between '"+fechaI+"' and '"+fechaF+"'" +
            "group by va.id_vuelo " +
            "order by sum(va.capacidad_actual) desc limit 10;";
        List<Object[]> arr_cust = (List<Object[]>)em.createQuery(q)
                              .getResultList();
        Barra_POF gpvf = new Barra_POF();
        List<BPVF> lAux = new ArrayList<BPVF>();
        gpvf.setFechaI(fechaI);
        gpvf.setFechaF(fechaF);
        for (Object[] a : arr_cust) {
            BPVF bpvf = new BPVF();
            bpvf.setSumCap((int)a[0]);
            bpvf.setOficinaI((String)a[1]);
            bpvf.setOficinaF((String)a[2]);
            bpvf.setHoraI((String)a[3]);
            lAux.add(bpvf);
        }
        gpvf.setBpvf(lAux);
        ObjectNode graficoJson = JsonHelper.createJson(gpvf, JsonNodeFactory.instance, new String []{
          "fechaI",
          "fechaF",
          "bpvf.*"
        });
        return graficoJson;
    }
    
}