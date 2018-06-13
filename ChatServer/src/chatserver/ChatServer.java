/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author KMINH
 */

public class ChatServer {
// frame

    JFrame j;
    Container c;
    JTextArea txtaHoiThoai;
    JTextField txtTinNhan;
    JButton btnGui;

//
    Socket s;
    OutputStream os;
    InputStream is;
    ThreadNhap threadnhap;

    PrintWriter pw;
    ServerSocket ss;

    private void GiaoDien() {

        j = new JFrame("Server");
        j.setBounds(10, 10, 520, 400);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtaHoiThoai = new JTextArea();
        txtaHoiThoai.setBounds(10, 10, 480, 300);
        txtTinNhan = new JTextField();
        txtTinNhan.setBounds(10, 320, 300, 30);
        entertextgui entergui = new entertextgui();
        txtTinNhan.addKeyListener(entergui);

        btnGui = new JButton("Gui");
        btnGui.setBounds(320, 320, 80, 30);
        Gui gd = new Gui();
        btnGui.addActionListener(gd);

        c = j.getContentPane();
        c.setLayout(null);
        c.add(txtaHoiThoai);
        c.add(txtTinNhan);
        c.add(btnGui);

        j.setVisible(true);
    }

    public ChatServer() {
        GiaoDien(); // tạo giao diện
        KhoiTao();

    }

    private void KhoiTao() {

        try {
            ss = new ServerSocket(1234);
            System.out.println("Da tao server port 1234");
            s = ss.accept();

            System.out.println("Co client ket noi");
            os = s.getOutputStream();
            is = s.getInputStream();
            threadnhap = new ThreadNhap();
            threadnhap.start();
        } catch (IOException ex) {
            System.out.println("Loi phan socket: " + ex.getMessage());
        }
    }

// gửi text 
    private void NutGui() {

        try {

            pw = new PrintWriter(os);
            String noidung = txtTinNhan.getText();
            System.out.println("Gui: " + noidung);
            txtaHoiThoai.setText(txtaHoiThoai.getText() + "Server(me): " + noidung + "\n");
            txtTinNhan.setText("");
            pw.println(noidung);
            pw.flush();
        } catch (NullPointerException e) {
            System.out.println("Loi nhap xuat, do chua co client ket noi");
            JOptionPane.showMessageDialog(txtaHoiThoai, "Loi");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("Bi loi: "+ex.getMessage());
            }
            System.exit(0);
        }
    }

// nhấn enter để gủi.
    class entertextgui implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                NutGui();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        
    }

    class ThreadNhap extends Thread {

        @Override
        public void run() {
            Scanner sc = new Scanner(is);
            String nhan = "";
            while (true) {
                nhan = sc.nextLine();
                txtaHoiThoai.setText(txtaHoiThoai.getText() + "Client: " + nhan + "\n");
                System.out.println("\n---- Nhan: " + nhan);
            }
        }

    }

    class Gui implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            NutGui();
        }

    }

    public static void main(String[] args) {
        ChatServer b = new ChatServer();
    }
}
