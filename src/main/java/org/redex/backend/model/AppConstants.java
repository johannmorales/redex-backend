package org.redex.backend.model;

import pe.albatross.zelpers.miscelanea.OSValidator;

public interface AppConstants {

    String UTC = "UTC";
    String TMP_DIR = OSValidator.isWindows() ? "C:/tmp/" : "/tmp/";

}
