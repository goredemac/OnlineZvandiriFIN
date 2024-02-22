/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.PreparedStatement;
import java.net.InetAddress;

/**
 *
 * @author goredemac
 */
public class passwordUpdAud {

    connCred c = new connCred();
    PreparedStatement pst = null;
    String hostName = "";

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    public void auditUpdate(String empNum) {

        try {
            computerName();
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
 System.out.println(formatterTime.format(date));
       System.out.println(formatterDate.format(date));
            String sqlupdate = "insert into [HRLeaveSysZvandiri].[dbo].[usrChgTab]  "
                    + "(Application,EmpNum,ChgTime,ChgDate,ComputerHost)"
                    + "values (?,?,?,?,?)";
            pst = conn.prepareStatement(sqlupdate);
            pst.setString(1, "LeaveApp");
            pst.setString(2, empNum);
            pst.setString(3, formatterTime.format(date));
            pst.setString(4, formatterDate.format(date));
            pst.setString(5, hostName);

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
