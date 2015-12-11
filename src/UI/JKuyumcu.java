/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Database.Materyal;
import Database.Musteri;
import Database.Satis;
import Database.Urun;
import Database.Konnekt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cynapsis
 */
public class JKuyumcu extends javax.swing.JFrame {
    
    Konnekt kon = new Konnekt();
    String pass;
    ArrayList<Urun> urunler = new ArrayList();
    ArrayList<Musteri> musteriler = new ArrayList();
    ArrayList<Satis> satislar = new ArrayList();
    DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
    DefaultComboBoxModel dcbmUrun = new DefaultComboBoxModel();
    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtmMusteri = new DefaultTableModel();
    DefaultTableModel dtmUrun = new DefaultTableModel();
    
    /**
     * Creates new form JKuyumcu
     * @param sifre
     */
    public JKuyumcu(String sifre) {
        initComponents();
        gizle();
        jSatis.setVisible(true);
        pass = sifre;
        urunOku();
        musteriOku();
        satisOku();
        setComboModel();
        setGecmisTableModel();
        setMusteriTableModel();
        setUrunTableModel();
    }
    
    public void musteriYaz() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("veriler/musteri.txt"));
            oos.writeObject(musteriler);
        } catch (IOException ex) {
            Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void urunYaz() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("veriler/urunler.txt"));
            oos.writeObject(urunler);
        } catch (IOException ex) {
            Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void satisYaz() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("veriler/satislar.txt"));
            oos.writeObject(satislar);
        } catch (IOException ex) {
            Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setUrunTableModel() {
        for (int i = 0; i < dtmUrun.getRowCount(); i++) {
            dtmUrun.removeRow(0);
            i--;
        }
        String[] columns = {"Ürün kodu","Ad","Materyal","Gramaj","Maliyet"};
        dtmUrun.setColumnIdentifiers(columns);
        for (Urun u1 : urunler) {
            String materyal=u1.getMateryal();
            Object[] rows = {u1.getUrunPK().getUrunKodu(),u1.getUrunPK().getAdi(),materyal,u1.getAgirlik(),u1.getMaliyet()};
            dtmUrun.addRow(rows);
        }
        jTable3.setModel(dtmUrun);
    }
    
    public void setMusteriTableModel() {
        for (int i = 0; i < dtmMusteri.getRowCount(); i++) {
            dtmMusteri.removeRow(0);
            i--;
        }
        String[] columns = {"Adı","Telefon No","Adres"};
        dtmMusteri.setColumnIdentifiers(columns);
        for (Musteri m1 : musteriler) {
            Object[] rows = {m1.getAdi(),m1.getTelNo(),m1.getAdres()};
            dtmMusteri.addRow(rows);
        }
        jTable2.setModel(dtmMusteri);
    }
    
    public void setGecmisTableModel() {
        for (int i = 0; i < dtm.getRowCount(); i++) {
            dtm.removeRow(0);
            i--;
        }
        String[] columns = {"Alıcı","Ürün Kodu","Adet","Tarih","Maliyet","Fiyat"};
        dtm.setColumnIdentifiers(columns);
        double maliyet=0;
        for (Satis satis : satislar) {
            for (int i = 0; i < urunler.size(); i++) {
                if(satis.getUrunKodu().equals(urunler.get(i).getUrunPK().getUrunKodu())) {
                    maliyet=urunler.get(i).getMaliyet()*satis.getAdet();
                }
            }
            Object[] rows = {satis.getMusteri(),satis.getSatilan().getUrunPK().getUrunKodu(),satis.getAdet()+"",satis.getDate(),maliyet,satis.getFiyat()};
            dtm.addRow(rows);
        }
        jTable1.setModel(dtm);
    }
    
    public void setComboModel() {
        dcbm.removeAllElements();
        for (Musteri musteriler1 : musteriler) {
            dcbm.addElement(musteriler1);
        }
        this.musteriCombo.setModel(dcbm);
    }
    
    public void satisOku() {
        List<Satis> satislar2 = kon.query("SELECT s FROM Satis s");
        satislar = new ArrayList();
        for (Satis s1 : satislar2) {
            satislar.add(s1);
        }
    }
    
    public void urunOku() {
        List<Urun> urunler2 = kon.query("SELECT u FROM Urun u");
        urunler = new ArrayList();
        for (Urun u2 : urunler2) {
            urunler.add(u2);
            dcbmUrun.addElement(u2.getUrunPK().getUrunKodu());
        }
        urunCombo.setModel(dcbmUrun);
    }
    
    public void musteriOku() {
        List<Musteri> musteriler2 = kon.query("SELECT m FROM Musteri m");
        musteriler = new ArrayList();
        for (Musteri m1 : musteriler2) {
            musteriler.add(m1);
        }
    }
    
    public void gizle() {
        jAyarlar.setVisible(false);
        jGecmis.setVisible(false);
        jSatis.setVisible(false);
        jKayit.setVisible(false);
        jMusteri.setVisible(false);
        onayla.setVisible(false);
        jUrunler.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbKayit = new javax.swing.JButton();
        jbSatisGecmisi = new javax.swing.JButton();
        jbSatis = new javax.swing.JButton();
        jbAyarlar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jbMusteriler = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jKayit = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        resim = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSatis = new javax.swing.JPanel();
        musteriCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        adet = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        urunResim = new javax.swing.JLabel();
        kontrolEt = new javax.swing.JButton();
        onayla = new javax.swing.JButton();
        fiyat = new javax.swing.JTextField();
        yeniMusteri = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        maliyet = new javax.swing.JLabel();
        urunCombo = new javax.swing.JComboBox();
        jGecmis = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        gelirLbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        maliyetLbl = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        karLbl = new javax.swing.JLabel();
        jMusteri = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jUrunler = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        urunDuzenle = new javax.swing.JButton();
        disaAktar = new javax.swing.JButton();
        jAyarlar = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jEskiSifre = new javax.swing.JPasswordField();
        jYeniSifre1 = new javax.swing.JPasswordField();
        jYeniSifre2 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jbKayit.setText("Ürün Kayıt");
        jbKayit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jbSatisGecmisi.setText("Satış Geçmişi");
        jbSatisGecmisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jbSatis.setText("Satış");
        jbSatis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jbAyarlar.setText("Ayarlar");
        jbAyarlar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jButton5.setText("Ürünler");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jbMusteriler.setText("Müşteriler");
        jbMusteriler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TabPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Ürün Adı");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Ürün Kodu");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Gramaj");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Materyal");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Altın", "Gümüş" }));

        resim.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resim.setText("Resim");

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton7.setText("Ürün Ekle");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urunEkle(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton8.setText("Resim Ekle");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resimEkle(evt);
            }
        });

        javax.swing.GroupLayout jKayitLayout = new javax.swing.GroupLayout(jKayit);
        jKayit.setLayout(jKayitLayout);
        jKayitLayout.setHorizontalGroup(
            jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jKayitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1)
                        .addComponent(jTextField3)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resim, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(338, Short.MAX_VALUE))
        );
        jKayitLayout.setVerticalGroup(
            jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jKayitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jKayitLayout.createSequentialGroup()
                        .addComponent(jSeparator3)
                        .addContainerGap())
                    .addGroup(jKayitLayout.createSequentialGroup()
                        .addGroup(jKayitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resim, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jKayitLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4))
                            .addGroup(jKayitLayout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)))
                        .addGap(82, 253, Short.MAX_VALUE))))
        );

        jPanel1.add(jKayit, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        musteriCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Ürün kodu");

        jLabel9.setText("Ürün Adet");

        jLabel10.setText("Müşteri");

        urunResim.setMaximumSize(new java.awt.Dimension(155, 159));
        urunResim.setPreferredSize(new java.awt.Dimension(155, 159));

        kontrolEt.setText("Kontrol Et");
        kontrolEt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kontrolEtActionPerformed(evt);
            }
        });

        onayla.setText("Onayla");
        onayla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onaylaActionPerformed(evt);
            }
        });

        fiyat.setMinimumSize(new java.awt.Dimension(30, 87));

        yeniMusteri.setText("Yeni Müsteri");
        yeniMusteri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yeniMusteriActionPerformed(evt);
            }
        });

        jLabel12.setText("Fiyat");

        jLabel13.setText("Maliyet");

        urunCombo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        urunCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jSatisLayout = new javax.swing.GroupLayout(jSatis);
        jSatis.setLayout(jSatisLayout);
        jSatisLayout.setHorizontalGroup(
            jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSatisLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jSatisLayout.createSequentialGroup()
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jSatisLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(musteriCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yeniMusteri, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jSatisLayout.createSequentialGroup()
                                .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addComponent(adet)
                                .addGap(262, 262, 262)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(urunResim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jSatisLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(maliyet, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kontrolEt, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(290, 290, 290)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(onayla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fiyat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jSatisLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(urunCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jSatisLayout.setVerticalGroup(
            jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSatisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSatisLayout.createSequentialGroup()
                        .addComponent(urunResim, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jSatisLayout.createSequentialGroup()
                                .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fiyat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(onayla, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 243, Short.MAX_VALUE))
                            .addGroup(jSatisLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(maliyet)
                                .addGap(4, 4, 4)
                                .addComponent(kontrolEt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jSatisLayout.createSequentialGroup()
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(yeniMusteri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(musteriCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)))
                        .addGap(18, 18, 18)
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(urunCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jSatisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(adet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1.add(jSatis, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel11.setText("Toplam Gelir");

        jLabel15.setText("Toplam Maliyet");

        jLabel17.setText("Kar");

        javax.swing.GroupLayout jGecmisLayout = new javax.swing.GroupLayout(jGecmis);
        jGecmis.setLayout(jGecmisLayout);
        jGecmisLayout.setHorizontalGroup(
            jGecmisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jGecmisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(gelirLbl)
                .addGap(126, 126, 126)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(maliyetLbl)
                .addGap(163, 163, 163)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(karLbl)
                .addContainerGap(220, Short.MAX_VALUE))
        );
        jGecmisLayout.setVerticalGroup(
            jGecmisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jGecmisLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jGecmisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(gelirLbl)
                    .addComponent(jLabel15)
                    .addComponent(maliyetLbl)
                    .addComponent(jLabel17)
                    .addComponent(karLbl))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jGecmis, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jMusteriLayout = new javax.swing.GroupLayout(jMusteri);
        jMusteri.setLayout(jMusteriLayout);
        jMusteriLayout.setHorizontalGroup(
            jMusteriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
        );
        jMusteriLayout.setVerticalGroup(
            jMusteriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );

        jPanel1.add(jMusteri, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jButton2.setText("Seçili Ürünü Sil");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urunSilPerformed(evt);
            }
        });

        urunDuzenle.setText("Seçili Ürünü Düzenle");
        urunDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urunDuzenleActionPerformed(evt);
            }
        });

        disaAktar.setText("Ürünleri Dışa Aktar");
        disaAktar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disaAktarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jUrunlerLayout = new javax.swing.GroupLayout(jUrunler);
        jUrunler.setLayout(jUrunlerLayout);
        jUrunlerLayout.setHorizontalGroup(
            jUrunlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jUrunlerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(disaAktar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(urunDuzenle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jUrunlerLayout.setVerticalGroup(
            jUrunlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUrunlerLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jUrunlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(urunDuzenle)
                    .addComponent(disaAktar))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel1.add(jUrunler, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        jLabel6.setText("Eski Şifre");

        jLabel7.setText("Yeni Şifre");

        jLabel8.setText("Yeni Şifre Tekrar");

        jButton1.setText("Şifre Değiştir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sifreDegistirPerformed(evt);
            }
        });

        javax.swing.GroupLayout jAyarlarLayout = new javax.swing.GroupLayout(jAyarlar);
        jAyarlar.setLayout(jAyarlarLayout);
        jAyarlarLayout.setHorizontalGroup(
            jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAyarlarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAyarlarLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(25, 25, 25)
                        .addComponent(jEskiSifre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jYeniSifre2)
                            .addComponent(jYeniSifre1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addContainerGap(206, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jAyarlarLayout.setVerticalGroup(
            jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAyarlarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jAyarlarLayout.createSequentialGroup()
                        .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jEskiSifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jYeniSifre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(10, 10, 10)
                        .addGroup(jAyarlarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jYeniSifre2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(405, Short.MAX_VALUE))
        );

        jPanel1.add(jAyarlar, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 493));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbKayit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSatisGecmisi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSatis, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbMusteriler, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAyarlar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbKayit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jbSatis, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSatisGecmisi, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbMusteriler, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAyarlar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TabPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TabPerformed
        // TODO add your handling code here:
        JButton jb = (JButton)evt.getSource();
        gizle();
        
        if(jb.equals(jbKayit)) {
            jKayit.setVisible(true);
        }
        else if(jb.equals(jbSatis)) {
            jSatis.setVisible(true);
        }
        else if(jb.equals(jbSatisGecmisi)) {
            jGecmis.setVisible(true);
            double gelir=0, maliyet=0, kar;
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                maliyet+=Double.parseDouble(jTable1.getValueAt(i, 4).toString());
                gelir+=Double.parseDouble(jTable1.getValueAt(i, 5).toString());
            }
            kar = gelir-maliyet;
            String gelirStr=String.format("%1$,.2f", gelir);
            String maliyetStr=String.format("%1$,.2f", maliyet);
            String karStr=String.format("%1$,.2f", kar);
            gelirLbl.setText(gelirStr);
            maliyetLbl.setText(maliyetStr);
            karLbl.setText(karStr);
        }
        else if(jb.equals(jbMusteriler)) {
            jMusteri.setVisible(true);
        }
        else if(jb.equals(jbAyarlar)) {
            jAyarlar.setVisible(true);
        }
        else {
            jUrunler.setVisible(true);
        }
        
        
    }//GEN-LAST:event_TabPerformed

    private void sifreDegistirPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sifreDegistirPerformed
        // TODO add your handling code here:
        if(!pass.equals(jEskiSifre.getText()) || !jYeniSifre1.getText().equals(jYeniSifre2.getText()) ) {
            JOptionPane.showMessageDialog(this, "Olmadı be şifre yanlış veya birbirini tutmuyor");
        }
        else {
            
            try {
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/sample","app","app");
                PreparedStatement st = con.prepareStatement("UPDATE AUT SET Pass=?");
                pass = jYeniSifre1.getText();
                st.setString(1, pass);
                st.executeUpdate();
                con.close();
                JOptionPane.showMessageDialog(this, "Başarıyla Güncellendi");
            } catch (SQLException ex) {
                Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
//            try {
//                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("veriler/aut.txt"));
//                pass = jYeniSifre1.getText();
//                char[] password = pass.toCharArray();
//                for (int i = 0; i < password.length; i++) {
//                    password[i] = (char)(password[i] + 58);
//                }
//                oos.writeObject(password);
//                oos.close();
//                
//            } catch (IOException ex) {
//                Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }//GEN-LAST:event_sifreDegistirPerformed

    private void resimEkle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resimEkle
        // TODO add your handling code here:
        File icon;
        JFileChooser jfc = new JFileChooser();
        int returnValue = jfc.showOpenDialog(this);
        if(returnValue==0) {
            try {
                icon = jfc.getSelectedFile();
                Path source = icon.toPath();
                Path hedef = Paths.get("veriler/urunfoto/" + jTextField2.getText() + ".png");
                Files.copy(source, hedef);
                ImageIcon ii = new ImageIcon(hedef.toString());
                resim.setIcon(ii);
            } catch (FileAlreadyExistsException faee) {
                JOptionPane.showMessageDialog(this, "Bu kod zaten kayıtlı.");
            } catch (IOException ex) {
                Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_resimEkle

    private void urunEkle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urunEkle
        // TODO add your handling code here:
        if(jTextField1.getText()!=null && jTextField2.getText()!=null && jTextField3.getText()!=null) {
            Materyal m1 = new Materyal(jComboBox1.getSelectedItem().toString());
            boolean varMi=false;
            for (Urun u1 : urunler) {
                if(u1.getUrunPK().getUrunKodu().equals(jTextField2.getText())) {
                    varMi=true;
                }
            }
            if(!varMi) {
                Urun u1 = new Urun(jTextField1.getText(),Double.parseDouble(jTextField3.getText()),jTextField2.getText(),m1);
                urunler.add(u1);
                kon.insert(u1);
                dcbmUrun.addElement(u1.getUrunPK().getUrunKodu());
                setUrunTableModel();
                JOptionPane.showMessageDialog(this, "Başarıyla Eklendi.");
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                resim.setIcon(null);   
            }
            else {
                JOptionPane.showMessageDialog(this, "Bu ürün kodu zaten kayıtlı.");
            }
        }
    }//GEN-LAST:event_urunEkle

    private void yeniMusteriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yeniMusteriActionPerformed
        // TODO add your handling code here:
        JYeniMusteri jym = new JYeniMusteri(this.musteriler,this);
        jym.setVisible(true);
    }//GEN-LAST:event_yeniMusteriActionPerformed

    private void kontrolEtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kontrolEtActionPerformed
        // TODO add your handling code here:
        boolean islemTamam=false;
        String urunK = urunCombo.getSelectedItem().toString();
        for (Urun urunler1 : urunler) {
            if(urunler1.getUrunPK().getUrunKodu().equals(urunK)) {
                maliyet.setText(urunler1.getMaliyet()*Integer.parseInt(adet.getText())+"");
                ImageIcon ii = new ImageIcon(urunler1.getResimPath());
                urunResim.setIcon(ii);
                onayla.setVisible(true);
                islemTamam=true;
            }
        }
        if(!islemTamam) {
            JOptionPane.showMessageDialog(this, "Bu kod ile kayıtlı ürün bulunmamaktadır.");
        }
    }//GEN-LAST:event_kontrolEtActionPerformed

    private void onaylaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onaylaActionPerformed
        // TODO add your handling code here:
        Musteri m1 = (Musteri)musteriCombo.getSelectedItem();
        Urun u=null;
        for (Urun u1 : urunler) {
            if(u1.getUrunPK().getUrunKodu().equals(urunCombo.getSelectedItem().toString())) {
                u=u1;
            }
        }
        Satis s1 = new Satis(m1,u,Double.parseDouble(fiyat.getText()),Integer.parseInt(adet.getText()));
        satislar.add(s1);
        kon.insert(s1);
        setGecmisTableModel();
        JOptionPane.showMessageDialog(this, "Satış başarılı.");
        adet.setText("");
        fiyat.setText("");
        maliyet.setText("");
        urunResim.setIcon(null);
    }//GEN-LAST:event_onaylaActionPerformed

    private void urunSilPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urunSilPerformed
        // TODO add your handling code here:
        String uK = jTable3.getValueAt(jTable3.getSelectedRow(), 0).toString();
        for (Urun u1 : urunler) {
            if(u1.getUrunPK().getUrunKodu().equals(uK)) {
                try {
                    Files.delete(Paths.get(u1.getResimPath()));
                } catch (IOException ex) {
                    Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
                }
                urunler.remove(u1);
                Query q = kon.getEm().createQuery("DELETE FROM Urun u WHERE u.urunPK.urunKodu =:uK");
                q.setParameter("uK", u1.getUrunPK().getUrunKodu());
                kon.deleteUpdate(q);
                break;
            }
        }
        setUrunTableModel();
    }//GEN-LAST:event_urunSilPerformed

    private void urunDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urunDuzenleActionPerformed
        // TODO add your handling code here:
        Urun pass = null;
        for (Urun u1 : urunler) {
            if(u1.getUrunPK().getUrunKodu().equals(dtmUrun.getValueAt(jTable3.getSelectedRow(), 0))) {
                pass = u1;
                break;
            }
        }
        JUrunDuzenle jud = new JUrunDuzenle(this,pass);
        jud.setVisible(true);
    }//GEN-LAST:event_urunDuzenleActionPerformed

    private void disaAktarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disaAktarActionPerformed
        try {
            // TODO add your handling code here:
            try {
                Files.delete(Paths.get("urunListe.txt"));
            } catch (NoSuchFileException nsfe) {
            }
                PrintWriter pw = new PrintWriter("urunListe.txt");
            for (Urun u1 : urunler) {
                String mater=u1.getMateryal();
                pw.format("Ürün adi : %s\tÜrün Kodu : %s\tMateryali : %s\tGramaj : %f\tMaliyet : %f"
                        , u1.getUrunPK().getAdi(), u1.getUrunPK().getUrunKodu(), mater, u1.getAgirlik(), u1.getMaliyet());
                pw.println();
                pw.flush();
            }
            
            pw.close();
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JKuyumcu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_disaAktarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adet;
    private javax.swing.JButton disaAktar;
    private javax.swing.JTextField fiyat;
    private javax.swing.JLabel gelirLbl;
    private javax.swing.JPanel jAyarlar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPasswordField jEskiSifre;
    private javax.swing.JPanel jGecmis;
    private javax.swing.JPanel jKayit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jMusteri;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jSatis;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel jUrunler;
    private javax.swing.JPasswordField jYeniSifre1;
    private javax.swing.JPasswordField jYeniSifre2;
    private javax.swing.JButton jbAyarlar;
    private javax.swing.JButton jbKayit;
    private javax.swing.JButton jbMusteriler;
    private javax.swing.JButton jbSatis;
    private javax.swing.JButton jbSatisGecmisi;
    private javax.swing.JLabel karLbl;
    private javax.swing.JButton kontrolEt;
    private javax.swing.JLabel maliyet;
    private javax.swing.JLabel maliyetLbl;
    private javax.swing.JComboBox musteriCombo;
    private javax.swing.JButton onayla;
    private javax.swing.JLabel resim;
    private javax.swing.JComboBox urunCombo;
    private javax.swing.JButton urunDuzenle;
    private javax.swing.JLabel urunResim;
    private javax.swing.JButton yeniMusteri;
    // End of variables declaration//GEN-END:variables
}
