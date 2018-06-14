/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author KMINH
 */

public class ChatClient{
// frame
JFrame j;
Container c;
JTextArea txtaHoiThoai;
JTextField txtTinNhan;
JButton btnGui;

// Biến của socket
Socket s;
OutputStream os;
InputStream is;
ThreadNhap threadnhap;
PrintWriter pw;

public ChatClient(){
    GiaoDien(); // tạo giao diện
    KhoiTaoSocKet();   // tạo socket
}

private void GiaoDien(){

    j = new JFrame("Client");
    j.setBounds(10, 10, 520, 400);
    j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    txtaHoiThoai = new JTextArea();
    txtaHoiThoai.setBounds(10, 10, 480, 300);
    txtaHoiThoai.setEditable(false);
    txtTinNhan = new JTextField();
    txtTinNhan.setBounds(10, 320, 300, 30);
    entertxtTinNhan keyGui = new entertxtTinNhan();
    txtTinNhan.addKeyListener(keyGui);
    
    btnGui = new JButton("Gửi");
    btnGui.setBounds(320, 320, 80, 30);
    Gui gd = new Gui();
    btnGui.addActionListener(gd);
            
    c= j.getContentPane();
    c.setLayout(null);
    c.add(txtaHoiThoai);
    c.add(txtTinNhan);
    c.add(btnGui);
    
    j.setVisible(true);
}
private void KhoiTaoSocKet(){
    try {
        s = new Socket("127.0.0.1",1234);
        os = s.getOutputStream();
        is = s.getInputStream();
    } catch (IOException ex) {
        System.out.println("Loi phan socket: " +ex.getMessage());
        if("Connection refused".equals(ex.getMessage())){
            System.out.println("Chua bat server!");
            txtaHoiThoai.setText("Không có kết nối với SERVER!");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex1) {
            System.out.println("Bi loi: "+ex.getMessage());
        }
        System.exit(0);
    }
    
    threadnhap = new ThreadNhap();
    threadnhap.start(); 
}

// gửi text
private void NutGui(){
    
    pw =new PrintWriter(os);
    String noidung=txtTinNhan.getText();
    System.out.println("Gui: "+noidung);
    txtaHoiThoai.setText(txtaHoiThoai.getText()+"Me: "+noidung+"\n");
    txtTinNhan.setText("");
    pw.println(noidung); pw.flush();
}


// gõ phím enter để gủi.
class entertxtTinNhan implements KeyListener{
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

// cập nhật liên tục input trên Stream
class ThreadNhap extends Thread{

    @Override
    public void run() {
        Scanner sc = new Scanner(is); // Scanner lấy input liên tục trên stream
        String nhan ="";
        while(true){
            nhan = sc.nextLine();
            txtaHoiThoai.setText(txtaHoiThoai.getText()+"Server: "+nhan+"\n");
            System.out.println("\n---- Nhan: " + nhan);
        }
    }
    
}

class Gui implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        pw =new PrintWriter(os);
        String noidung=txtTinNhan.getText();
        System.out.println("Gui: "+noidung);
        txtaHoiThoai.setText(txtaHoiThoai.getText()+"Me: "+noidung+"\n");
        txtTinNhan.setText("");
        pw.println(noidung); pw.flush();
    }
}

public static void main(String[] args) {
    ChatClient b = new ChatClient();
}
}
