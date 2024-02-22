/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author goredemac
 */
public class savePDFToDB {

    public String fileName;
    public int len;

    public void runDB() {
        connSaveFile db = new connSaveFile();
        connCred c = new connCred();
        try {
          
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            System.out.println("con....");
            
            //db.getPDFData(conn, fileName);
            System.out.println("done....");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void pdfChooser() {
        JFileChooser chooser = new JFileChooser();
    //    chooser.setCurrentDirectory(new java.io.File("C://"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
//        chooser.addChoosableFileFilter(new FileNameExtensionFilter("MS Office Documents", "docx", "xlsx", "pptx"));
        // chooser .addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));

        chooser.setDialogTitle("Select File to Attach");

      //  chooser.setAcceptAllFileFilterUsed(false);
        File file = chooser.getSelectedFile();

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().toString();
          
        } else {
            System.out.println("No Selection ");
        }
    }

}
