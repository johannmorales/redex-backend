/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

import java.io.FileNotFoundException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.redex.backend.model.general.Archivo;
import org.redex.backend.repository.ArchivosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportesServiceImp implements ReportesService{
 
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    
    @Override
    public String paquetesXvuelo(Long id){
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        
        String[] columns = {"Codigo", "Fecha Ingreso", "Oficina origen", "Oficina destino"};
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        
        EntityManager em = emf.createEntityManager();
        String q = "select p.codigo_rastreo,p.fecha_ingreso, o1.codigo as origen ,o2.codigo as destino " +
                    "from " +
                    "paquete p, paquete_ruta pr, oficina o1, oficina o2 " +
                    "where " +
                    "p.id_oficina_origen = o1.id and o2.codigo=p.id_oficina_destino " +
                    "and pr.id_vuelo_agendado="+id;
        List<Object[]> arr_cust = (List<Object[]>)em.createNativeQuery(q)
                              .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            cont++;
        }
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try {
            Random r = new Random();
            String filename = "Reporte_paquete_vuelo_"+Instant.now().toString().substring(0, 10) +r.nextInt(100000)+".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    @Override    
    public String enviosXfechas(String fI,String fF){
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        
        String[] columns = {"Codigo", "Estado", "Fecha Ingreso", "Fecha Llegada",
        "Oficina Origen", "Oficina Destino"};
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        EntityManager em = emf.createEntityManager();
        String q ="select p.codigo_rastreo, p.estado, p.fecha_ingreso, va.fecha_fin,o.codigo as origen, o2.codigo as destino " +
            "from oficina o, paquete p, oficina o2, " +
            "(select id_paquete,id_vuelo_agendado, max(orden) from paquete_ruta ) aux," +
            "vuelo_agendado va " +
            "where p.id_oficina_origen = o.id and p.id_oficina_destino = o2.id and " +
            "aux.id_paquete = p.id and aux.id_vuelo_agendado = va.id and " +
            "p.fecha_ingreso between '"+fI+"' and '"+fF+"'";
        //String q = "select p.codigo_rastreo, p.estado, p.fecha_ingreso, va.fecha_fin,o.codigo as origen, o2.codigo as destino from oficina o, paquete p, oficina o2, (select id_paquete,id_vuelo_agendado, max(orden) from paquete_ruta ) aux,vuelo_agendado va where p.id_oficina_origen = o.id and p.id_oficina_destino = o2.id and aux.id_paquete = p.id and aux.id_vuelo_agendado = va.id and p.fecha_ingreso between '16-11-2018' and '18-11-2018'";
        
        List<Object[]> arr_cust = (List<Object[]>)em.createNativeQuery(q).getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            row.createCell(4).setCellValue(obj[4].toString());
            row.createCell(5).setCellValue(obj[5].toString());
            cont++;
        }
        
        
        
//        for(int i = 0; i < columns.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
        try {
            Random r = new Random();
            String filename = "envio_fecha_"+Instant.now().toString().substring(0,10) +r.nextInt(100000)+".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    @Override    
    public String paquetesXusuario(Long id){
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        
        String[] columns = {"Codigo", "Estado", "Fecha Ingreso", "Fecha Llegada",
        "Oficina Origen", "Oficina Destino"};
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        EntityManager em = emf.createEntityManager();
        String q = "select p.codigo_rastreo, p.estado, p.fecha_ingreso, va.fecha_fin,o.codigo as origen, o2.codigo as destino " +
            "from oficina o, paquete p, persona pr, oficina o2, " +
            "(select id_paquete,id_vuelo_agendado, max(orden) from paquete_ruta ) aux," +
            "vuelo_agendado va " +
            "where p.id_oficina_origen = o.id and p.id_oficina_destino = o2.id and " +
            "aux.id_paquete = p.id and aux.id_vuelo_agendado = va.id and " +
            "p.id_persona_origen = pr.id and pr.numero_documento_identidad="+id;
        List<Object[]> arr_cust = (List<Object[]>)em.createNativeQuery(q)
                              .getResultList();
        Iterator it = arr_cust.iterator();
        int cont = 1;
        
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            row.createCell(4).setCellValue(obj[4].toString());
            row.createCell(5).setCellValue(obj[5].toString());
            cont++;
        }
        
        
        
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        
        try {
            Random r = new Random();
            String filename = "Reporte_paquete_usuario_"+Instant.now().toString().substring(0, 10) +r.nextInt(100000)+".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.close();
            workbook.close();
            return filename;
        } catch (Exception e) {
            Logger.getLogger(ReportesServiceImp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    @Override    
    public String accionesXusuarioXoficinaXfecha(){
        return null;
    }
}
