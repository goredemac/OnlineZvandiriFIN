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
public class expirePlan {

    connCred c = new connCred();

    public String docNextVerExpire, SearchRef, SearchStatus, createUsrNam, minDate, todayDate, sendToExpire, usrMail;
    PreparedStatement pst = null;

    public void findMinDate(String planRef) {
        try {
            SearchRef = planRef;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery(
                    "select min(ACT_DATE) as minDate from ( SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'"
                    + "union "
                    + "SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] where PLAN_REF_NUM =" + SearchRef + "and ACT_REC_STA = 'A'"
                    + "union "
                    + "SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "union "
                    + "SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "union "
                    + "SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ) a");
            while (r.next()) {

                minDate = r.getString(1);
               
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void findMeetMinDate(String planRef) {
        try {
            SearchRef = planRef;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select min(ACT_DAT_FRO) as minDate from (SELECT [ACT_DAT_FRO]FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanMeetWk1Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' "
                    + "union SELECT [ACT_DAT_FRO] FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk2Tab] where PLAN_REF_NUM = " + SearchRef + " "
                    + "and ACT_REC_STA = 'A' union SELECT  [ACT_DAT_FRO] FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk3Tab] "
                    + "where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' union SELECT [ACT_DAT_FRO] FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanMeetWk4Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' union "
                    + "SELECT [ACT_DAT_FRO] FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk5Tab] where PLAN_REF_NUM = " + SearchRef + " and "
                    + "ACT_REC_STA = 'A' ) a");
            while (r.next()) {

                minDate = r.getString(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void findUser(String logUsrNum) {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + logUsrNum + "'");

            while (r.next()) {

                sendToExpire = r.getString(2);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void findCreatorExpire() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select ACTION_BY,ACT_VER+1 from [ClaimsAppSysZvandiri].[dbo]."
                    + "[PlanActTab] where PLAN_REF_NUM =" + SearchRef + ""
                    + " and ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + ")");

            while (r.next()) {

                createUsrNam = r.getString(1);
                docNextVerExpire = r.getString(2);
            }
            Statement st1 = conn.createStatement();
            ResultSet r1 = st1.executeQuery("select EMP_MAIL from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where EMP_NAM ='" + createUsrNam + "'");

            while (r1.next()) {

                usrMail = r1.getString(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updatePrevRecordExpire() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanActTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

            String sqlPlanPeriod = "update [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanPeriod);
            pst.executeUpdate();

            String sqlPlanWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk1);
            pst.executeUpdate();

            String sqlPlanWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk2);
            pst.executeUpdate();

            String sqlPlanWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk3);
            pst.executeUpdate();

            String sqlPlanWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk4);
            pst.executeUpdate();

            String sqlPlanWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void updateWk1PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "SELECT SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,PRJ_TASK_CODE,ACT_SITE,"
                    + "ACT_DESC,ACT_JUSTIFCATION,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "ACC_PROV_AMT,EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER + 1,DOC_VER + 1,'E' FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk1plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateWk2PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk2plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "SELECT SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,PRJ_TASK_CODE,ACT_SITE,"
                    + "ACT_DESC,ACT_JUSTIFCATION,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "ACC_PROV_AMT,EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER + 1,DOC_VER + 1,'E' FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk2plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateWk3PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk3plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "SELECT SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,PRJ_TASK_CODE,ACT_SITE,"
                    + "ACT_DESC,ACT_JUSTIFCATION,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "ACC_PROV_AMT,EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER + 1,DOC_VER + 1,'E' FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk3plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateWk4PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk4plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "SELECT SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,PRJ_TASK_CODE,ACT_SITE,"
                    + "ACT_DESC,ACT_JUSTIFCATION,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "ACC_PROV_AMT,EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER + 1,DOC_VER + 1,'E' FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk4plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateWk5PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk5plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "SELECT SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,PRJ_TASK_CODE,ACT_SITE,"
                    + "ACT_DESC,ACT_JUSTIFCATION,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "ACC_PROV_AMT,EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER + 1,DOC_VER + 1,'E' FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk5plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateWkPlanPeriodExpire() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                    + "Select SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,ACT_VER+1,DOC_VER+1,'E' "
                    + "from [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlWkplanperiod);
            pst.executeUpdate();

//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void WkPlanActionApprovedExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "select SERIAL,PLAN_REF_NUM,'Plan - Expired','Expired',ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,"
                    + "SEND_TO_EMP_NUM,SEND_TO_NAM,ACTIONED_ON_DATE,ACTIONED_ON_TIME,ACTIONED_ON_COMPUTER,"
                    + "'Expired as some activities on the plan have passed ',ACT_VER+1,DOC_VER+1,'E' FROM ClaimsAppSysZimTTECH.dbo.PlanActTab"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "where PLAN_REF_NUM =" + SearchRef + ") and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlWkplanperiod);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
