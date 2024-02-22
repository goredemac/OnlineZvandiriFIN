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
import javax.swing.JOptionPane;

/**
 *
 * @author goredemac
 */
public class expireRequest {

    connCred c = new connCred();

    public String docNextVerExpire, SearchRef, requestUsr, createUsrNam, minDate, todayDate, sendToExpire, usrMail;
    PreparedStatement pst = null;

    public void findMinDate(String planRef, String usrNam) {
        try {
            SearchRef = planRef;
            requestUsr = usrNam;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select min(ACT_DATE) as minDate from (SELECT [ACT_DATE]FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and (EMP_NAM1 = '" + requestUsr + "'  or EMP_NAM2 = '" + requestUsr + "' "
                    + "or EMP_NAM3 = '" + requestUsr + "'  or EMP_NAM4 = '" + requestUsr + "')"
                    + "union SELECT [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] where PLAN_REF_NUM = " + SearchRef + " "
                    + "and ACT_REC_STA = 'C' "
                    + "  and (EMP_NAM1 = '" + requestUsr + "'  or EMP_NAM2 = '" + requestUsr + "' "
                    + "or EMP_NAM3 = '" + requestUsr + "'  or EMP_NAM4 = '" + requestUsr + "')"
                    + "union SELECT  [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and (EMP_NAM1 = '" + requestUsr + "'  or EMP_NAM2 = '" + requestUsr + "' "
                    + "or EMP_NAM3 = '" + requestUsr + "'  or EMP_NAM4 = '" + requestUsr + "')"
                    + "union SELECT [ACT_DATE] FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and (EMP_NAM1 = '" + requestUsr + "'  or EMP_NAM2 = '" + requestUsr + "' "
                    + "or EMP_NAM3 = '" + requestUsr + "'  or EMP_NAM4 = '" + requestUsr + "')"
                    + "union SELECT [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] where PLAN_REF_NUM = " + SearchRef + " and "
                    + "ACT_REC_STA = 'C' ) a");
            while (r.next()) {

                minDate = r.getString(1);
             
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

 



    public void updatePrevRecordExpire() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "STATUS ='E' where PLAN_REF_NUM =" + SearchRef + " and STATUS = 'A' "
                    + "and EMP_NAM ='"+requestUsr+"'";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

           

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    

}
