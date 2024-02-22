/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author goredemac
 */
public class timeHost {

    public String curDate, curTime, internetDate, internetTime;
    public String hostName = "";
    SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void showDate() {
        Date d = new Date();
        curDate = formatter.format(d);
    }

    public void showTime() {
        new Timer(0, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                curTime = s.format(d);
           
            }
        }) {

        }.start();

    }

    public void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    public void intShowDate() throws IOException, InterruptedException {
        URL url = new URL("http://www.google.com");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // opening the connection
        HttpURLConnection httpCon
                = (HttpURLConnection) url.openConnection();

        // getting the date of URL connection
        long date = httpCon.getDate();

        internetDate = formatter.format(new Date(date));

    }

    public void intShowTime() throws IOException, InterruptedException {
        URL url = new URL("http://www.google.com");

        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
        // opening the connection
        HttpURLConnection httpCon
                = (HttpURLConnection) url.openConnection();

        // getting the date of URL connection
        long date = httpCon.getDate();

        internetTime = s.format(date);

    }
    
}
