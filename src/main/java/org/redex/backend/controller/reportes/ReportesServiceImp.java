package org.redex.backend.controller.reportes;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.redex.backend.BackendApplication;
import org.redex.backend.controller.auditoria.AuditoriaService;
import org.redex.backend.model.AppConstants;
import org.redex.backend.model.auditoria.AuditoriaTipoEnum;
import org.redex.backend.model.auditoria.AuditoriaView;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.general.Persona;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.model.seguridad.Usuario;
import org.redex.backend.repository.AuditoriaViewRepository;
import org.redex.backend.repository.PaquetesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.UsuariosRepository;
import org.redex.backend.security.CurrentUser;
import org.redex.backend.security.DataSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.albatross.zelpers.file.excel.ExcelHelper;

@Service
@Transactional(readOnly = false)
public class ReportesServiceImp implements ReportesService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PaquetesRepository paquetesRepository;


    @Autowired
    AuditoriaViewRepository auditoriaViewRepository;

    @Autowired
    AuditoriaService auditoriaService;

    @Autowired
    UsuariosRepository usuariosRepository;

    @Autowired
    PersonaRepository personaRepository;

    @Override
    public String paquetesXvuelo(Long id, DataSession ds) {
        auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_PAQUETES_POR_VUELO, ds);

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
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        String q = "select p.codigo_rastreo, p.fecha_ingreso, o.codigo as origen, o2.codigo as destino "
                + " from paquete_ruta pr, paquete p, oficina o, oficina o2  "
                + " where pr.id_vuelo_agendado=" + id + " and "
                + " pr.id_paquete =p.id  and o.id = p.id_oficina_origen and o2.id = p.id_oficina_destino";

        List<Object[]> arr_cust = (List<Object[]>) em.createNativeQuery(q)
                .getResultList();

        Iterator it = arr_cust.iterator();

        int cont = 1;

        while (it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            Row row = sheet.createRow(cont);
            row.createCell(0).setCellValue(obj[0].toString());
            row.createCell(1).setCellValue(obj[1].toString());
            row.createCell(2).setCellValue(obj[2].toString());
            row.createCell(3).setCellValue(obj[3].toString());
            cont++;
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try {
            Random r = new Random();
            String filename = AppConstants.TMP_DIR + "Reporte_paquete_vuelo_" + Instant.now().toString().substring(0, 10) + r.nextInt(100000) + ".xlsx";
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
    public String enviosXfechas(LocalDate inicio, LocalDate fin, DataSession ds) {
        try {
            auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_ENVIOS_POR_FECHAS, ds);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            InputStream formato = this.getClass().getResourceAsStream("/templates/excel/auditoria_paquetes.xlsx");
            Workbook workbook = WorkbookFactory.create(formato);
            Sheet sheet = workbook.getSheet("Reporte");

            Persona personaSolicitante = personaRepository.getOne(ds.getPersona().getId());

            ExcelHelper.replaceVal(sheet, 2, 1, String.format("%s - %s", personaSolicitante.getNombreCompleto(), personaSolicitante.getDocumento()));
            ExcelHelper.replaceVal(sheet, 3, 1, df.format(LocalDateTime.now()));

            ExcelHelper.replaceVal(sheet, 5, 1, String.format("%s - %s", df.format(inicio.atStartOfDay()), fin.atTime(LocalTime.MAX)));

            List<Paquete> paquetes = paquetesRepository.findAllByRangoInicio(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));

            int cont = 8;

            for (Paquete paquete : paquetes) {
                Oficina oficinaOrigen = paquete.getOficinaOrigen();
                Oficina oficinaDestino = paquete.getOficinaDestino();

                ExcelHelper.replaceVal(sheet, cont, 0, dtf.format(paquete.getFechaIngreso()));
                ExcelHelper.replaceVal(sheet, cont, 1, paquete.getCodigoRastreo());
                ExcelHelper.replaceVal(sheet, cont, 2, oficinaOrigen.getCodigo());
                ExcelHelper.replaceVal(sheet, cont, 3, oficinaOrigen.getPais().getNombre());
                ExcelHelper.replaceVal(sheet, cont, 4, oficinaDestino.getCodigo());
                ExcelHelper.replaceVal(sheet, cont, 5, oficinaDestino.getPais().getNombre());
                ExcelHelper.replaceVal(sheet, cont, 6, paquete.getPersonaOrigen().getDocumento());
                ExcelHelper.replaceVal(sheet, cont, 7, paquete.getPersonaOrigen().getNombreCompleto());
                ExcelHelper.replaceVal(sheet, cont, 8, paquete.getPersonaDestino().getDocumento());
                ExcelHelper.replaceVal(sheet, cont, 9, paquete.getPersonaDestino().getNombreCompleto());

                cont++;
            }

            return write(workbook, "reporte_paquetes");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String paquetesXusuario(Long id, DataSession ds) {
        try {
            auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_PAQUETES_POR_USUARIO, ds);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            InputStream formato = this.getClass().getResourceAsStream("/templates/excel/auditoria_usuario.xlsx");
            Workbook workbook = WorkbookFactory.create(formato);
            Sheet sheet = workbook.getSheet("Reporte");

            Persona personaSolicitante = personaRepository.getOne(ds.getPersona().getId());

            ExcelHelper.replaceVal(sheet, 2, 1, String.format("%s - %s", personaSolicitante.getNombreCompleto(), personaSolicitante.getDocumento()));
            ExcelHelper.replaceVal(sheet, 3, 1, df.format(LocalDateTime.now()));

            Usuario usuarioAuditado = usuariosRepository.getOne(id);
            Persona personaAuditada = usuarioAuditado.getColaborador().getPersona();

            ExcelHelper.replaceVal(sheet, 5, 1, String.format("%s - %s", personaAuditada.getNombreCompleto(), personaAuditada.getDocumento()));

            List<Paquete> paquetes = paquetesRepository.allByIdUserRegistro(id);

            int cont = 8;

            for (Paquete paquete : paquetes) {
                Oficina oficinaOrigen = paquete.getOficinaOrigen();
                Oficina oficinaDestino = paquete.getOficinaDestino();

                ExcelHelper.replaceVal(sheet, cont, 0, dtf.format(paquete.getFechaIngreso()));
                ExcelHelper.replaceVal(sheet, cont, 1, paquete.getCodigoRastreo());
                ExcelHelper.replaceVal(sheet, cont, 2, oficinaOrigen.getCodigo());
                ExcelHelper.replaceVal(sheet, cont, 3, oficinaOrigen.getPais().getNombre());
                ExcelHelper.replaceVal(sheet, cont, 4, oficinaDestino.getCodigo());
                ExcelHelper.replaceVal(sheet, cont, 5, oficinaDestino.getPais().getNombre());
                ExcelHelper.replaceVal(sheet, cont, 6, paquete.getPersonaOrigen().getDocumento());
                ExcelHelper.replaceVal(sheet, cont, 7, paquete.getPersonaOrigen().getNombreCompleto());
                ExcelHelper.replaceVal(sheet, cont, 8, paquete.getPersonaDestino().getDocumento());
                ExcelHelper.replaceVal(sheet, cont, 9, paquete.getPersonaDestino().getNombreCompleto());

                cont++;
            }

            return write(workbook, "paquetes_usuario");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String enviosXoficina(LocalDate inicio, LocalDate fin, DataSession ds) {
        auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_ENVIOS_POR_OFICINAS, ds);

        Workbook workbook = new XSSFWorkbook();

        List<Paquete> paquetes = paquetesRepository.findAllByRangoInicio(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));
        Map<Oficina, List<Paquete>> map = paquetes.stream().collect(Collectors.groupingBy(Paquete::getOficinaOrigen));

        for (Map.Entry<Oficina, List<Paquete>> entry : map.entrySet()) {
            Oficina oficina = entry.getKey();
            List<Paquete> paqs = entry.getValue();

            Sheet sheet = workbook.createSheet(oficina.getCodigo());

            Integer cont = 1;

            for (Paquete paquete : paqs) {
                ExcelHelper.replaceVal(sheet, cont, 0, paquete.getCodigoRastreo());
                ExcelHelper.replaceVal(sheet, cont, 1, paquete.getFechaIngresoString());
                ExcelHelper.replaceVal(sheet, cont, 2, paquete.getOficinaDestino().getCodigo());
                cont++;
            }
        }

        return write(workbook, "envios_por_oficina");
    }

    @Override
    public String enviosFinalizados(LocalDate inicio, LocalDate fin, DataSession ds) {
        auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_ENVIOS_FINALIZADOS, ds);

        return null;
    }

    @Override
    public String auditoria(LocalDate inicio, LocalDate fin, Long idOficina, DataSession ds) {
        auditoriaService.auditar(AuditoriaTipoEnum.REPORTE_AUDITORIA, ds);
        List<AuditoriaView> list = auditoriaViewRepository.allByOficinaVentana(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), new Oficina(idOficina));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Auditoria");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Integer cont = 1;

        for (AuditoriaView au : list) {
            ExcelHelper.replaceVal(sheet, cont, 0, au.getMomento().format(dtf));
            ExcelHelper.replaceVal(sheet, cont, 1, au.getUsuario().getUsername());
            ExcelHelper.replaceVal(sheet, cont, 2, au.getUsuario().getColaborador().getPersona().getNombreCompleto());
            ExcelHelper.replaceVal(sheet, cont, 3, au.getOficina().getCodigo());
            ExcelHelper.replaceVal(sheet, cont, 4, au.getTipo().getDescripcion());
            cont++;
        }

        return write(workbook, "auditoria");
    }


    private String write(Workbook workbook, String prefifo) {
        try {
            String filename = String.format("%s%s_%s.xlsx", AppConstants.TMP_DIR, prefifo, System.currentTimeMillis());
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


}
