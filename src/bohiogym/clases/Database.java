/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.clases;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Camilo
 */
public class Database {

    private static final Logger logger = Logger.getLogger(Database.class.getName());
    public Connection conexion;
    public PreparedStatement sentencia;
    public ResultSet resultado;
    private String driver, url, user, pass;

    public Database(Properties props) {
        driver = props.getProperty("javax.persistence.jdbc.driver");
        url = props.getProperty("javax.persistence.jdbc.url");
        user = props.getProperty("javax.persistence.jdbc.user");
        pass = props.getProperty("javax.persistence.jdbc.password");
    }

    public void cerrarConexionYStatement(Connection conexion, PreparedStatement... statements) {
        try {
            conexion.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cerrando la conexion de la base de datos", e);
        } finally {
            for (Statement statement : statements) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e2) {
                        logger.log(Level.SEVERE, "Error cerrando la sentencia de la base de datos", e2);
                    }
                }
            }
        }
    }

    public void cerrarResultSet(ResultSet... results) {
        for (ResultSet rs : results) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error cerrando el ResultSet de la base de datos", e);
                }
            }
        }
    }

    public void ConectarBasedeDatos() {
        try {
            final String Controlador = driver;
            // "com.mysql.jdbc.Driver";
            Class.forName(Controlador);
            final String url_bd = url;
            conexion = (Connection) DriverManager.getConnection(url_bd, user, pass);
            //sentencia = (PreparedStatement) conexion.createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "10040:\n" + ex.getMessage(), Database.class.getName(), JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "10041:\n" + ex.getMessage(), Database.class.getName(), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "10042:\n" + ex.getMessage(), Database.class.getName(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void DesconectarBasedeDatos() {
        try {
            if (conexion != null) {
                if (sentencia != null) {
                    sentencia.close();
                }
                conexion.close();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error cerrando la sentencia y la conexion de la base de datos", ex);
        }
    }

}
