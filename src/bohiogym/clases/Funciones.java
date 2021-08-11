/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.clases;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Juan
 */
public class Funciones {

    public ImageIcon CreateImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo " + path);
            return null;
        }
    }

    public static void setOcultarColumnas(JTable tbl, int columna[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }

    public static void setSizeColumnas(JTable tbl, int columna[], int sizeColumn[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(sizeColumn[i]);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(sizeColumn[i]);
        }
    }

    public static int calcularEdad(Calendar fechaNac) {
        Calendar today = Calendar.getInstance();
        int diff_year = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diff_month = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diff_day = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        //Si está en ese año pero todavía no los ha cumplido
        if (diff_month < 0 || (diff_month == 0 && diff_day < 0)) {
            diff_year = diff_year - 1; //no aparecían los dos guiones del postincremento :|
        }
        return diff_year;
    }

    /**
     * dd/MM/yyyy
     */
    public static SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * yyyy-MM-dd HH:mm
     */
    public static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * dd-MM-yyyy
     */
    public static SimpleDateFormat ddMMyyyy2 = new SimpleDateFormat("dd-MM-yyyy");
    /**
     * HH:mm
     */
    public static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
    /**
     * dd/MM/yyyy HH:mm
     */
    public static SimpleDateFormat yyyyMMddHHmm2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
}
