/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class dbCRUD {
    
    String jdbcURL ="jdbc:mysql://localhost:3306/koperasi";
    String username ="root";
    String password ="";
    
    Connection koneksi;
    
    public dbCRUD(){
        try (Connection Koneksi = DriverManager.getConnection(jdbcURL, username, password)){
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    
    public dbCRUD(String url, String user, String pass){
        
        try(Connection Koneksi = DriverManager.getConnection(url, user, pass)) {
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            System.out.println("Berhasil");
        } catch (Exception error) {
            System.err.println(error.toString());
        }
        
    }
    
    public Connection getKoneksi () throws SQLException{
        try { 
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver (mysqldriver);

        } catch (SQLException e) {

            System.err.println(e.toString());
        }

         return DriverManager.getConnection(this.jdbcURL, this.username, this.password);
    }
    
    public boolean duplicateKey(String table, String PrimaryKey, String value) { 
        boolean hasil=false;

        try {
            Statement sts = getKoneksi().createStatement();
            ResultSet rs = sts.executeQuery("SELECT * FROM "+table+" WHERE "+PrimaryKey+" ='"+value+"'");

            hasil = rs.next();

            rs.close();
            sts.close(); 
            getKoneksi().close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
        return hasil;
    }
    
    public void SimpanPinjaman(String kode_anggota, String jumlah_pinjaman, String tanggal_pinjaman, String status_pinjaman) {
        try {
            if (duplicateKey("Pinjaman", "kode_anggota", kode_anggota)) {
                JOptionPane.showMessageDialog(null, "Kode Anggota sudah Terdaftar");
            } else{ 
                String SQL ="INSERT INTO pinjaman (kode_anggota, jumlah_pinjaman, tanggal_pinjaman, status_pinjaman) VALUE (?, ?, ?, ?)";
                PreparedStatement simpan = getKoneksi ().prepareStatement (SQL);
                simpan.setString(1, kode_anggota);
                simpan.setString (2, jumlah_pinjaman);
                simpan.setString(3, tanggal_pinjaman);
                simpan.setString(4, status_pinjaman);
                 System.out.println("Data Berhasil Disimpan");
                simpan.executeUpdate();
                simpan.close(); getKoneksi ().close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void SimpanTabungan(String id_tabungan, String kode_anggota, String saldo, String tanggal_pembuatan) {
        try {
            if (duplicateKey("tabungan", "id_tabungan", id_tabungan)) {
                JOptionPane.showMessageDialog(null, "ID Tabungan sudah Terdaftar");
            } else{ 
                String SQL ="INSERT INTO tabungan (id_tabungan, kode_anggota, saldo, tanggal_pembuatan) VALUE (?, ?, ?, ?)";
                PreparedStatement simpan = getKoneksi ().prepareStatement (SQL);
                simpan.setString(1, id_tabungan);
                simpan.setString(2, kode_anggota);
                simpan.setString(3, saldo);
                simpan.setString(4, tanggal_pembuatan);
                System.out.println("Data Berhasil Disimpan");
                simpan.executeUpdate();
                simpan.close(); getKoneksi ().close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public String getFieldValueEdit(String[] Field, String[] value){
        String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }
    
    public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary,String[] Field, String[] Value){
        
        try {
           String SQLUbah = "UPDATE "+NamaTabel+" SET "+getFieldValueEdit(Field, Value)+" WHERE "+PrimaryKey+"='"+IsiPrimary+"'";
           Statement perintah = getKoneksi().createStatement();
           perintah.executeUpdate(SQLUbah);
           perintah.close();
           getKoneksi().close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
    }
    
    public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksi().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}