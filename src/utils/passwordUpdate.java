/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author goredemac
 */
public class passwordUpdate {

    PreparedStatement pst = null;

    connCred c = new connCred();

    public void passupd(String resPass, String empNum) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sql1 = "update[HRLeaveSysZimTTECH].[dbo].[usrlogin] set "
                    + "updencrypass = ENCRYPTBYPASSPHRASE('password','" + resPass + "')"
                    + "where upper(EmpNum) =upper('" + empNum + "')";

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
