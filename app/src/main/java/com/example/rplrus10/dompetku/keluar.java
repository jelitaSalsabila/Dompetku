package com.example.rplrus10.dompetku;

public class keluar extends keuangan {
    private int no;
    private String noToken;
    private String keluar;
    private String tanggal;
    private String keterangan;
    private String username;


    public String getNoToken() {
        return noToken;
    }

    public void setNoToken(String noToken) {
        this.noToken = noToken;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKeluar() {
        return keluar;
    }

    public void setKeluar(String keluar) {
        this.keluar = keluar;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
