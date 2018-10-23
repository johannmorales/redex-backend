/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.usuarios;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;

/**
 *
 * @author Oscar
 */
public interface UsuariosService {
    public CargaDatosResponse carga(Archivo archivo);
}
