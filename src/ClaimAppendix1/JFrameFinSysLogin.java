/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import utils.connCred;
import utils.timeHost;
import utils.passResetSelfService;
import utils.passwordGenerator;
import utils.passwordUpdate;
import utils.passwordUpdAud;

/**
 *
 * @author cgoredema
 */
public class JFrameFinSysLogin extends javax.swing.JFrame {

    connCred c = new connCred();
    timeHost tH = new timeHost();
    passResetSelfService pReset = new passResetSelfService();
    passwordGenerator pGen = new passwordGenerator();
    passwordUpdate pUpd = new passwordUpdate();
    passwordUpdAud pUsrAud = new passwordUpdAud();
    int counter = 0;
    int accSusp = 0;
    int countAccNo = 0;
    int countPwdExist = 0;
    String logYear, logDate, disEmpNam, usrNum;
    String hostName = "";

    /**
     * Creates new form JFrameLogin
     */
    PreparedStatement pst = null;

    public JFrameFinSysLogin() {
        initComponents();
        try {
            //  showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameFinSysLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameFinSysLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

        jLabelDate.setText(tH.internetDate);
        showTime();
        getDate();
        computerName();
        setResizable(false);
        jLabelUsrPass1.setVisible(false);
        jPasswordUsr1.setVisible(false);
        jLabelUsrPass2.setVisible(false);
        jPasswordUsr2.setVisible(false);
        jButtonPassUdate.setVisible(false);
//        jLabelPassViewOpen.setBounds(470, 165, 30, 25);
////        jLabelPassViewOpen.setVisible(false);
        jLabelPassViewClose.setVisible(false);
        jLabelPassChangeViewClose.setVisible(false);
        jLabelPassChangeViewOpen.setVisible(false);

    }

//    void showDate() {
//        Date d = new Date();
//        SimpleDateFormat s = new SimpleDateFormat("dd MMMM yyyy");
//        jLabelDate.setText(s.format(d));
//
//    }
    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                jLabelTime.setText(s.format(d));
            }
        }) {

        }.start();

    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
            //  JOptionPane.showMessageDialog(this, "CurBal" + curBalDays + "Days" + Double.parseDouble(jLabelDays1.getText())

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void getDate() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT YEAR(CONVERT(char(10), GetDate(),126)),CONVERT(char(10), GetDate(),126)");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                logYear = rs.getString(1);
                logDate = rs.getString(2);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insAudLog() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlInsLog = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[AudUsrRecTab] "
                    + "(EMP_NUM,LOG_NAM,ACT_DATE,ACT_TIME,ACT_TYPE) VALUES (?,?,?,?,?)";
            pst = conn.prepareStatement(sqlInsLog);
            pst.setString(1, usrNum);
            pst.setString(2, jTextUsrNam.getText());
            pst.setString(3, jLabelDate.getText());
            pst.setString(4, jLabelTime.getText());
            pst.setString(5, jLabelVerson.getText());
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createLogAudit(String logStatus) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlCreateLog = "insert into [HRLeaveSysZvandiri].[dbo].[auditLogin] "
                    + "VALUES (?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlCreateLog);

            pst.setString(1, logYear);
            pst.setString(2, usrNum);
            pst.setString(3, jTextUsrNam.getText());
            pst.setString(4, "Fin System");
            pst.setString(5, logStatus);
            pst.setString(6, logDate);
            pst.setString(7, jLabelTime.getText());
            pst.setString(8, hostName);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void pwdChangeLogAudit() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlStaUpd = "update[HRLeaveSysZimTTECH].[dbo].[usrLogPwdAudTab]  set "
                    + "ACT_REC_STA='P'"
                    + "where Emp_Num ='" + usrNum + "' ";

            pst = conn.prepareStatement(sqlStaUpd);
            pst.executeUpdate();

            String sqlCreateLog = "insert into [HRLeaveSysZvandiri].[dbo].[usrLogPwdAudTab] "
                    + "(EMP_NUM,ACT_DATE,ACT_TIME,COMPUTER,ACT_REC_STA)"
                    + "VALUES (?,?,?,?,?)";

            pst = conn.prepareStatement(sqlCreateLog);

            pst.setString(1, usrNum);
            pst.setString(2, logDate);
            pst.setString(3, jLabelTime.getText());
            pst.setString(4, hostName);
            pst.setString(5, "A");

            pst.executeUpdate();

            String pwdUpd = new String(jPasswordUsr1.getPassword());

            String sqlupdate = "update[HRLeaveSysZimTTECH].[dbo].[usrLogPwdAudTab]  set "
                    + "usrencrypass = ENCRYPTBYPASSPHRASE('password','" + pwdUpd + "')"
                    + "where Emp_Num ='" + usrNum + "' and ACT_REC_STA='A'";

            pst = conn.prepareStatement(sqlupdate);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkPwdExist() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            String pwdUpd = new String(jPasswordUsr1.getPassword());

            st.executeQuery("select COUNT(*) from ( select * from (SELECT top 5 * "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[usrLogPwdAudTab]   "
                    + "WHERE emp_num = '" + usrNum + "' ORDER BY ACT_DATE  DESC) x  ) y   "
                    + "where convert (varchar(50), DECRYPTBYPASSPHRASE('password',usrencrypass)) ='" + pwdUpd + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                countPwdExist = rs.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkAccNo(String empNo) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement stmt = conn.createStatement();

            String sql = "SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] "
                    + " where acc_num like '914%0000' and EMP_NUM ='" + empNo + "'";

            ResultSet r = stmt.executeQuery(sql);

            while (r.next()) {
                countAccNo = r.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void login() {

        jButtonLogin.setEnabled(false);

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sql = "select usrnam, convert (varchar(50), DECRYPTBYPASSPHRASE('password',usrencrypass))"
                    + "as usrencrypass,convert (varchar(50),DECRYPTBYPASSPHRASE('password',updencrypass)),"
                    + "EmpNum as updencrypass,empStatus from [HRLeaveSysZvandiri].[dbo].[usrlogin] where usrnam ='" + jTextUsrNam.getText() + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int tmp = 0;
            String user = jTextUsrNam.getText();
            String pwd = new String(jPasswordUsr.getPassword());

            while (rs.next()) {

                String uname = rs.getString(1);
                String password = rs.getString(2);
                String updPass = rs.getString(3);
                usrNum = rs.getString(4);
                accSusp = rs.getInt(5);
                checkAccNo(usrNum);
                if (accSusp == 0) {
                    JOptionPane.showMessageDialog(this, "Please note that your "
                            + "account is suspended. Please contact HR for more "
                            + "information or account activation");
                    createLogAudit("Account Suspended");
                    jTextUsrNam.setText("");
                    jPasswordUsr.setText("");
                    jTextUsrNam.requestFocusInWindow();
                    jButtonLogin.setEnabled(true);
                } else if (countAccNo > 0) {
                    JOptionPane.showMessageDialog(this, "Please note that your "
                            + "banking details need to be updated.Send your updated "
                            + "banking details to IT through the helpdesk");
                    tmp++;
                    createLogAudit("Banking Details Change Request");
                    jTextUsrNam.setText("");
                    jPasswordUsr.setText("");
                    jTextUsrNam.requestFocusInWindow();

                } else if ((user.equals(uname)) && (pwd.equals(updPass))) {
                    tmp++;

                    if ((updPass == null) || !(updPass.equals(password))) {
                        jLabelPassReset.setVisible(false);
//                        JOptionPane.showMessageDialog(this, " <html> " + jTextUsrNam.getText() + "Your password need to be updated.Password does not meet requirements. "
//                                + "Password should have minimum seven(8) characters including at least one uppercase,"
//                                + "one lowercase character, one digit (0-9) and one special characters.</html>");
                        JOptionPane.showMessageDialog(null, "<html> Your password need to be updated.<br>"
                                + "Password should have minimum <b>eight(8)</b> characters including at least <b>one uppercase,one lowercase</b> character, "
                                + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
                        jLabelUsrPass.setText("Current Password");
                        jPasswordUsr.setEnabled(false);
                        jLabelPassViewOpen.setVisible(false);
                        jLabelUsrPass1.setVisible(true);
                        jPasswordUsr1.setVisible(true);
                        jLabelUsrPass2.setVisible(true);
                        jPasswordUsr2.setVisible(true);
                        jButtonPassUdate.setVisible(true);
                        jButtonLogin.setVisible(false);
                        jLabelPassChangeViewOpen.setVisible(true);
                        createLogAudit("Change Password");

                    } else {

                        new JFrameMain(usrNum).setVisible(true);
                        setVisible(false);

                        createLogAudit("Login Sucessful");
                        tmp++;
                    }

                }
            }

            if (tmp == 0) {

                JOptionPane.showMessageDialog(null, "Username and/or Password invalid!");
                createLogAudit("Invalid Login Credentials");
//                jTextUsrNam.setText("");
//                jPasswordUsr.setText("");
                jTextUsrNam.requestFocusInWindow();
                jButtonLogin.setEnabled(true);
            }

        } catch (SQLException e) {

            int errConCode = e.getErrorCode();
            if (errConCode == 0) {

                JOptionPane.showMessageDialog(this, "<html>Connection failure to the Finsd System. "
                        + "Please check your network connection and try again.<br><br> If the problem persist conatact IT department</html>",
                        "Fin System - Connection Failure", JOptionPane.ERROR_MESSAGE);
                createLogAudit("Network Failure");
            }
            jButtonLogin.setEnabled(true);

        }

        jButtonLogin.setEnabled(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogPassReset = new javax.swing.JDialog();
        jLabelPassResetHeader = new javax.swing.JLabel();
        jLabelEmpNum = new javax.swing.JLabel();
        jTextResetEmpNum = new javax.swing.JTextField();
        jButtonResetCancel = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jButtonresetConfirm = new javax.swing.JButton();
        jLabelPassResetConfirm = new javax.swing.JLabel();
        jDialogWaiting = new javax.swing.JDialog();
        jDialogLoginMailWaiting = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelUsrNam = new javax.swing.JLabel();
        jLabelUsrPass = new javax.swing.JLabel();
        jTextUsrNam = new javax.swing.JTextField();
        jButtonLogin = new javax.swing.JButton();
        jPasswordUsr = new javax.swing.JPasswordField();
        jLabelDate = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabelHeading1 = new javax.swing.JLabel();
        jLabelUsrPass1 = new javax.swing.JLabel();
        jPasswordUsr1 = new javax.swing.JPasswordField();
        jPasswordUsr2 = new javax.swing.JPasswordField();
        jLabelUsrPass2 = new javax.swing.JLabel();
        jButtonPassUdate = new javax.swing.JButton();
        jLabelVerson = new javax.swing.JLabel();
        jLabelPassReset = new javax.swing.JLabel();
        jLabelPassViewClose = new javax.swing.JLabel();
        jLabelPassViewOpen = new javax.swing.JLabel();
        jLabelPassChangeViewClose = new javax.swing.JLabel();
        jLabelPassChangeViewOpen = new javax.swing.JLabel();

        jDialogPassReset.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogPassReset.setTitle("PASSWORD RESET");
        jDialogPassReset.setAlwaysOnTop(true);
        jDialogPassReset.setLocation(new java.awt.Point(450, 350));
        jDialogPassReset.setMinimumSize(new java.awt.Dimension(650, 215));
        jDialogPassReset.setResizable(false);
        jDialogPassReset.getContentPane().setLayout(null);

        jLabelPassResetHeader.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelPassResetHeader.setText("Password Reset Request");
        jDialogPassReset.getContentPane().add(jLabelPassResetHeader);
        jLabelPassResetHeader.setBounds(100, 0, 311, 38);

        jLabelEmpNum.setText("Employee No.");
        jDialogPassReset.getContentPane().add(jLabelEmpNum);
        jLabelEmpNum.setBounds(10, 60, 90, 30);

        jTextResetEmpNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextResetEmpNumKeyTyped(evt);
            }
        });
        jDialogPassReset.getContentPane().add(jTextResetEmpNum);
        jTextResetEmpNum.setBounds(120, 60, 60, 30);

        jButtonResetCancel.setText("Cancel");
        jButtonResetCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetCancelActionPerformed(evt);
            }
        });
        jDialogPassReset.getContentPane().add(jButtonResetCancel);
        jButtonResetCancel.setBounds(330, 125, 70, 30);

        jButtonReset.setText("<html> Password Reset </html>");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });
        jDialogPassReset.getContentPane().add(jButtonReset);
        jButtonReset.setBounds(220, 60, 120, 30);

        jButtonresetConfirm.setText("Confirm");
        jButtonresetConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonresetConfirmActionPerformed(evt);
            }
        });
        jDialogPassReset.getContentPane().add(jButtonresetConfirm);
        jButtonresetConfirm.setBounds(220, 125, 80, 30);
        jDialogPassReset.getContentPane().add(jLabelPassResetConfirm);
        jLabelPassResetConfirm.setBounds(10, 120, 200, 40);

        jDialogWaiting.setTitle("                                     Please Wait.Processing.");
        jDialogWaiting.setAlwaysOnTop(true);
        jDialogWaiting.setBackground(new java.awt.Color(51, 255, 51));
        jDialogWaiting.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogWaiting.setIconImages(null);
        jDialogWaiting.setLocation(new java.awt.Point(650, 400));
        jDialogWaiting.setMinimumSize(new java.awt.Dimension(500, 50));
        jDialogWaiting.setResizable(false);

        javax.swing.GroupLayout jDialogWaitingLayout = new javax.swing.GroupLayout(jDialogWaiting.getContentPane());
        jDialogWaiting.getContentPane().setLayout(jDialogWaitingLayout);
        jDialogWaitingLayout.setHorizontalGroup(
            jDialogWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        jDialogWaitingLayout.setVerticalGroup(
            jDialogWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jDialogLoginMailWaiting.setTitle("                                     Please Wait.... Sending you reset password.");
        jDialogLoginMailWaiting.setAlwaysOnTop(true);
        jDialogLoginMailWaiting.setBackground(new java.awt.Color(51, 255, 51));
        jDialogLoginMailWaiting.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogLoginMailWaiting.setIconImages(null);
        jDialogLoginMailWaiting.setLocation(new java.awt.Point(650, 400));
        jDialogLoginMailWaiting.setMinimumSize(new java.awt.Dimension(500, 50));
        jDialogLoginMailWaiting.setResizable(false);

        javax.swing.GroupLayout jDialogLoginMailWaitingLayout = new javax.swing.GroupLayout(jDialogLoginMailWaiting.getContentPane());
        jDialogLoginMailWaiting.getContentPane().setLayout(jDialogLoginMailWaitingLayout);
        jDialogLoginMailWaitingLayout.setHorizontalGroup(
            jDialogLoginMailWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        jDialogLoginMailWaitingLayout.setVerticalGroup(
            jDialogLoginMailWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Log In");
        setIconImages(null);

        jPanel1.setBackground(new java.awt.Color(246, 203, 20));
        jPanel1.setAlignmentX(30.5F);
        jPanel1.setAutoscrolls(true);
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setLayout(null);

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanel1.add(jLabelLogo);
        jLabelLogo.setBounds(560, 70, 220, 220);

        jLabelUsrNam.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelUsrNam.setText("Username");
        jPanel1.add(jLabelUsrNam);
        jLabelUsrNam.setBounds(90, 120, 100, 30);

        jLabelUsrPass.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelUsrPass.setText("Password");
        jPanel1.add(jLabelUsrPass);
        jLabelUsrPass.setBounds(90, 160, 120, 30);

        jTextUsrNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUsrNamActionPerformed(evt);
            }
        });
        jTextUsrNam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextUsrNamKeyTyped(evt);
            }
        });
        jPanel1.add(jTextUsrNam);
        jTextUsrNam.setBounds(290, 120, 180, 30);

        jButtonLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonLogin.setText("Log In");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonLogin);
        jButtonLogin.setBounds(320, 210, 90, 40);

        jPasswordUsr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordUsrActionPerformed(evt);
            }
        });
        jPasswordUsr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordUsrKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordUsrKeyReleased(evt);
            }
        });
        jPanel1.add(jPasswordUsr);
        jPasswordUsr.setBounds(290, 160, 180, 30);

        jLabelDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel1.add(jLabelDate);
        jLabelDate.setBounds(560, 0, 150, 30);

        jLabelTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel1.add(jLabelTime);
        jLabelTime.setBounds(710, 0, 90, 30);

        jLabelHeading1.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabelHeading1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHeading1.setText("Zvandiri FIN SYSTEM (TEST SYSTEM)");
        jPanel1.add(jLabelHeading1);
        jLabelHeading1.setBounds(60, 40, 630, 40);

        jLabelUsrPass1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelUsrPass1.setText("New Password");
        jPanel1.add(jLabelUsrPass1);
        jLabelUsrPass1.setBounds(90, 200, 90, 30);

        jPasswordUsr1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordUsr1ActionPerformed(evt);
            }
        });
        jPasswordUsr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordUsr1KeyPressed(evt);
            }
        });
        jPanel1.add(jPasswordUsr1);
        jPasswordUsr1.setBounds(290, 200, 180, 30);

        jPasswordUsr2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordUsr2ActionPerformed(evt);
            }
        });
        jPasswordUsr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordUsr2KeyPressed(evt);
            }
        });
        jPanel1.add(jPasswordUsr2);
        jPasswordUsr2.setBounds(290, 240, 180, 30);

        jLabelUsrPass2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelUsrPass2.setText("Confirm Password");
        jPanel1.add(jLabelUsrPass2);
        jLabelUsrPass2.setBounds(90, 240, 130, 30);

        jButtonPassUdate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonPassUdate.setForeground(new java.awt.Color(204, 0, 51));
        jButtonPassUdate.setText("Confirm Password Change");
        jButtonPassUdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPassUdateActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPassUdate);
        jButtonPassUdate.setBounds(270, 290, 180, 30);

        jLabelVerson.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVerson.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVerson.setText("Cloud Ver.  4.1.0");
        jPanel1.add(jLabelVerson);
        jLabelVerson.setBounds(670, 320, 110, 20);

        jLabelPassReset.setFont(new java.awt.Font("Tahoma", 2, 8)); // NOI18N
        jLabelPassReset.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPassReset.setText("<html>Forgot Password? <u>Password Reset.</u></html>");
        jLabelPassReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPassResetMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelPassReset);
        jLabelPassReset.setBounds(160, 220, 150, 20);

        jLabelPassViewClose.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPassViewClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/passViewClose.png"))); // NOI18N
        jLabelPassViewClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPassViewCloseMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelPassViewClose);
        jLabelPassViewClose.setBounds(520, 165, 30, 25);

        jLabelPassViewOpen.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPassViewOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/passViewOpen.png"))); // NOI18N
        jLabelPassViewOpen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jLabelPassViewOpenFocusLost(evt);
            }
        });
        jLabelPassViewOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPassViewOpenMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabelPassViewOpenMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabelPassViewOpenMouseReleased(evt);
            }
        });
        jPanel1.add(jLabelPassViewOpen);
        jLabelPassViewOpen.setBounds(470, 165, 30, 25);

        jLabelPassChangeViewClose.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPassChangeViewClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/passViewClose.png"))); // NOI18N
        jLabelPassChangeViewClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPassChangeViewCloseMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelPassChangeViewClose);
        jLabelPassChangeViewClose.setBounds(520, 205, 30, 25);

        jLabelPassChangeViewOpen.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPassChangeViewOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/passViewOpen.png"))); // NOI18N
        jLabelPassChangeViewOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPassChangeViewOpenMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabelPassChangeViewOpenMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabelPassChangeViewOpenMouseReleased(evt);
            }
        });
        jPanel1.add(jLabelPassChangeViewOpen);
        jLabelPassChangeViewOpen.setBounds(470, 205, 30, 25);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(816, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextUsrNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUsrNamActionPerformed

    }//GEN-LAST:event_jTextUsrNamActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        jButtonLogin.setEnabled(false);
        login();
        jButtonLogin.setEnabled(true);

    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void jPasswordUsrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordUsrKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();

        }
    }//GEN-LAST:event_jPasswordUsrKeyPressed

    private void jPasswordUsrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordUsrActionPerformed

    }//GEN-LAST:event_jPasswordUsrActionPerformed

    private void jPasswordUsr1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordUsr1ActionPerformed

    }//GEN-LAST:event_jPasswordUsr1ActionPerformed

    private void jPasswordUsr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordUsr1KeyPressed

    }//GEN-LAST:event_jPasswordUsr1KeyPressed

    private void jPasswordUsr2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordUsr2ActionPerformed

    }//GEN-LAST:event_jPasswordUsr2ActionPerformed

    private void jPasswordUsr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordUsr2KeyPressed

    }//GEN-LAST:event_jPasswordUsr2KeyPressed

    private void jButtonPassUdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPassUdateActionPerformed
        try {
            if ((jPasswordUsr1.getPassword().length) < 8) {

                JOptionPane.showMessageDialog(null, "<html> Password does not meet requirements.<br>"
                        + "Password should have minimum <b>eight(8)</b> characters including at least <b>one uppercase,one lowercase</b> character, "
                        + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
                createLogAudit("Password does not meet requirements");
                jTextUsrNam.setEditable(false);
                jPasswordUsr.setEditable(false);
//                jPasswordUsr1.setText("");
//                jPasswordUsr2.setText("");
                jPasswordUsr1.requestFocusInWindow();

            } else if ((!(jPasswordUsr1.getText().equals(jPasswordUsr2.getText())))) {

                JOptionPane.showMessageDialog(this, "Confirm password is not the same as password. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                createLogAudit("Password mismatch on password change");
                jTextUsrNam.setEditable(false);
                jPasswordUsr.setEditable(false);
//                jPasswordUsr1.setText("");
//                jPasswordUsr2.setText("");
                jPasswordUsr1.requestFocusInWindow();

            } else {

                int passwordLength = 8, upChars = 0, lowChars = 0;
                int special = 0, digits = 0;
                countPwdExist = 0;
                char ch;

                String password = jPasswordUsr1.getText();

                int total = password.length();
                if (total < passwordLength) {
                    System.out.println("\nThe Password's Length has to be of 8 characters or more.");
                    return;
                } else {
                    for (int i = 0; i < total; i++) {
                        ch = password.charAt(i);
                        if (Character.isUpperCase(ch)) {
                            upChars++;
                        } else if (Character.isLowerCase(ch)) {
                            lowChars++;
                        } else if (Character.isDigit(ch)) {
                            digits++;
                        } else {

                            special++;

                        }
                    }
                }

                if (upChars == 0) {
                    JOptionPane.showMessageDialog(null, "<html> Password should have  at least <b>one uppercase,one lowercase</b> character, "
                            + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
//                    jPasswordUsr1.setText("");
//                    jPasswordUsr2.setText("");
                    jPasswordUsr1.requestFocusInWindow();
                } else if (lowChars == 0) {
                    JOptionPane.showMessageDialog(null, "<html> Password should have  at least <b>one uppercase,one lowercase</b> character, "
                            + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
//                    jPasswordUsr1.setText("");
//                    jPasswordUsr2.setText("");
                    jPasswordUsr1.requestFocusInWindow();
                } else if (digits == 0) {
                    JOptionPane.showMessageDialog(null, "<html> Password should have  least <b>one uppercase,one lowercase</b> character, "
                            + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
//                    jPasswordUsr1.setText("");
//                    jPasswordUsr2.setText("");
                    jPasswordUsr1.requestFocusInWindow();
                } else if (special == 0) {
                    JOptionPane.showMessageDialog(null, "<html> Password should have  at least <b>one uppercase,one lowercase</b> character, "
                            + "<b>one digit</b> (0-9) and <b>one special character</b>.</html>");
//                    jPasswordUsr1.setText("");
//                    jPasswordUsr2.setText("");
                    jPasswordUsr1.requestFocusInWindow();
                }

                checkPwdExist();
                if (upChars > 0 && lowChars > 0 && digits > 0 && special > 0 && countPwdExist > 0) {

                    JOptionPane.showMessageDialog(null, "<html>Hello <b>" + jTextUsrNam.getText() + "</b><br>Please note that "
                            + "we have seen this password before.Try a different password.</html>");
//                    jPasswordUsr1.setText("");
//                    jPasswordUsr2.setText("");
                    jPasswordUsr1.requestFocusInWindow();
                } else if (upChars > 0 && lowChars > 0 && digits > 0 && special > 0 && countPwdExist == 0) {
                    String sql1 = "update [HRLeaveSysZvandiri].[dbo].[usrlogin] set updencrypass = ENCRYPTBYPASSPHRASE "
                            + "('password', '" + jPasswordUsr1.getText() + "'), usrencrypass = ENCRYPTBYPASSPHRASE "
                            + "('password', '" + jPasswordUsr1.getText() + "') where usrnam ='" + jTextUsrNam.getText() + "'";

                    Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                            + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                    pst = conn.prepareStatement(sql1);
                    pst.executeUpdate();

                    createLogAudit("Password Successfully Changed - Login Sucessful");
                    pwdChangeLogAudit();
                    JOptionPane.showMessageDialog(null, "Password Successfully Changed. Click Ok to continue.");
                    new JFrameMain(usrNum).setVisible(true);
                    setVisible(false);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_jButtonPassUdateActionPerformed

    private void jTextUsrNamKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUsrNamKeyTyped
        jButtonLogin.setEnabled(true);
    }//GEN-LAST:event_jTextUsrNamKeyTyped

    private void jLabelPassResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassResetMouseClicked
        if (evt.getClickCount() == 1) {
            jTextResetEmpNum.setText("");
            jLabelPassResetConfirm.setText("");
            jDialogPassReset.setVisible(true);

        }
    }//GEN-LAST:event_jLabelPassResetMouseClicked

    private void jTextResetEmpNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextResetEmpNumKeyTyped
//        char c = evt.getKeyChar();
//        if (!(Character.isDigit(c))) {
//            getToolkit().beep();
//            evt.consume();
//        }
    }//GEN-LAST:event_jTextResetEmpNumKeyTyped

    private void jButtonResetCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCancelActionPerformed
        jDialogPassReset.setVisible(false);
    }//GEN-LAST:event_jButtonResetCancelActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        if (jTextResetEmpNum.getText().length() == 0) {
            JOptionPane.showMessageDialog(jDialogPassReset, "Employee Number cannot be blank.");
            jTextResetEmpNum.setText("");
            jTextResetEmpNum.requestFocusInWindow();
        } else {
            jDialogWaiting.setVisible(true);
            pReset.getDetails(jTextResetEmpNum.getText());
            disEmpNam = "<html> Good day <b>" + pReset.empNam + "</b> <br>Please confirm password reset.</html>";
            jLabelPassResetConfirm.setVisible(true);
            jButtonresetConfirm.setVisible(true);
            jButtonResetCancel.setVisible(true);
            jLabelPassResetConfirm.setText(disEmpNam);
            jDialogWaiting.setVisible(false);
        }
    }//GEN-LAST:event_jButtonResetActionPerformed

    private void jButtonresetConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonresetConfirmActionPerformed
        try {
            pGen.randomGenerator();
            pUpd.passupd(pGen.genPass, jTextResetEmpNum.getText().toUpperCase());
            pUsrAud.auditUpdate(jTextResetEmpNum.getText());
            jDialogLoginMailWaiting.setVisible(true);
            pReset.sendMail(pGen.genPass);
            jDialogPassReset.setVisible(false);
            jDialogLoginMailWaiting.setVisible(false);
            JOptionPane.showMessageDialog(null, "Password successfully reset and sent to " + pReset.empMail);

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonresetConfirmActionPerformed

    private void jLabelPassViewOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassViewOpenMouseClicked
        if (evt.getClickCount() == 1) {

//            jLabelPassViewOpen.setVisible(false);
//            jLabelPassViewClose.setVisible(true);
//            jLabelPassViewOpen.setBounds(520, 165, 30, 25);
//            jLabelPassViewClose.setBounds(470, 165, 30, 25);
//            jPasswordUsr.setEchoChar((char) 0);
        }
    }//GEN-LAST:event_jLabelPassViewOpenMouseClicked

    private void jLabelPassViewCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassViewCloseMouseClicked
        if (evt.getClickCount() == 1) {
            jLabelPassViewOpen.setVisible(true);
            jLabelPassViewClose.setVisible(false);
            jLabelPassViewOpen.setBounds(470, 165, 30, 25);
            jLabelPassViewClose.setBounds(520, 165, 30, 25);
            jPasswordUsr.setEchoChar('*');

        }
    }//GEN-LAST:event_jLabelPassViewCloseMouseClicked

    private void jPasswordUsrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordUsrKeyReleased
//        String pwd = new String(jPasswordUsr.getPassword());
//
//        if (pwd.length() >= 3) {
//            jLabelPassViewOpen.setVisible(true);
//            jLabelPassViewClose.setVisible(false);
//
//        } else {
//            jLabelPassViewOpen.setVisible(false);
//            jLabelPassViewClose.setVisible(false);
//        }
    }//GEN-LAST:event_jPasswordUsrKeyReleased

    private void jLabelPassChangeViewCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassChangeViewCloseMouseClicked
        if (evt.getClickCount() == 1) {
            jLabelPassChangeViewOpen.setVisible(true);
            jLabelPassChangeViewClose.setVisible(false);
            jLabelPassChangeViewOpen.setBounds(470, 205, 30, 25);
            jLabelPassChangeViewClose.setBounds(520, 205, 30, 25);
            jPasswordUsr1.setEchoChar('*');
            jPasswordUsr2.setEchoChar('*');

        }
    }//GEN-LAST:event_jLabelPassChangeViewCloseMouseClicked

    private void jLabelPassChangeViewOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassChangeViewOpenMouseClicked
//        if (evt.getClickCount() == 1) {
//
//            jLabelPassChangeViewOpen.setVisible(false);
//            jLabelPassChangeViewClose.setVisible(true);
//            jLabelPassChangeViewOpen.setBounds(520, 205, 30, 25);
//            jLabelPassChangeViewClose.setBounds(470, 205, 30, 25);
//            jPasswordUsr1.setEchoChar((char) 0);
//            jPasswordUsr2.setEchoChar((char) 0);
//        }
    }//GEN-LAST:event_jLabelPassChangeViewOpenMouseClicked

    private void jLabelPassViewOpenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelPassViewOpenFocusLost

    }//GEN-LAST:event_jLabelPassViewOpenFocusLost

    private void jLabelPassViewOpenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassViewOpenMouseReleased
        jPasswordUsr.setEchoChar('*');
    }//GEN-LAST:event_jLabelPassViewOpenMouseReleased

    private void jLabelPassViewOpenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassViewOpenMousePressed
        jPasswordUsr.setEchoChar((char) 0);
    }//GEN-LAST:event_jLabelPassViewOpenMousePressed

    private void jLabelPassChangeViewOpenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassChangeViewOpenMousePressed
        jPasswordUsr1.setEchoChar((char) 0);
        jPasswordUsr2.setEchoChar((char) 0);
    }//GEN-LAST:event_jLabelPassChangeViewOpenMousePressed

    private void jLabelPassChangeViewOpenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPassChangeViewOpenMouseReleased
        jPasswordUsr1.setEchoChar('*');
        jPasswordUsr2.setEchoChar('*');
    }//GEN-LAST:event_jLabelPassChangeViewOpenMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameFinSysLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameFinSysLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameFinSysLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameFinSysLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameFinSysLogin().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonPassUdate;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonResetCancel;
    private javax.swing.JButton jButtonresetConfirm;
    private javax.swing.JDialog jDialogLoginMailWaiting;
    public javax.swing.JDialog jDialogPassReset;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelHeading1;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelPassChangeViewClose;
    private javax.swing.JLabel jLabelPassChangeViewOpen;
    private javax.swing.JLabel jLabelPassReset;
    private javax.swing.JLabel jLabelPassResetConfirm;
    private javax.swing.JLabel jLabelPassResetHeader;
    private javax.swing.JLabel jLabelPassViewClose;
    private javax.swing.JLabel jLabelPassViewOpen;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelUsrNam;
    private javax.swing.JLabel jLabelUsrPass;
    private javax.swing.JLabel jLabelUsrPass1;
    private javax.swing.JLabel jLabelUsrPass2;
    private javax.swing.JLabel jLabelVerson;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordUsr;
    private javax.swing.JPasswordField jPasswordUsr1;
    private javax.swing.JPasswordField jPasswordUsr2;
    private javax.swing.JTextField jTextResetEmpNum;
    private javax.swing.JTextField jTextUsrNam;
    // End of variables declaration//GEN-END:variables
}
