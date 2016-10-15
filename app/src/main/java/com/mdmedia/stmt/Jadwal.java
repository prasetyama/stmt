package com.mdmedia.stmt;

/**
 * Created by arief on 09/10/2015.
 */
public class Jadwal {
    private String days;
    private String kodemk;
    private String mkname;
    private String jam;
    private String kelas;
    private String lecture;
    private String prodiId;
    private String prodiname;
    private String smt;
    private String tahunajaran;
    private String smt2;
    private String date;
    private String ruang;
    private String exam;

    public Jadwal() {
    }

    public Jadwal(String days, String mkname, String kodemk, String prodiId,String prodiname, String kelas, String jam, String lecture, String smt, String tahunajaran, String smt2, String date, String ruang, String exam) {
        this.days = days;
        this.kodemk = kodemk;
        this.mkname = mkname;
        this.jam = jam;
        this.kelas = kelas;
        this.prodiId = prodiId;
        this.prodiname = prodiname;
        this.lecture = lecture;
        this.smt = smt;
        this.tahunajaran = tahunajaran;
        this.smt2 = smt2;
        this.date = date;
        this.ruang = ruang;
        this.exam = exam;
    }

    public String getTanggal() {
        return days;
    }

    public void setTanggal(String days) {
        this.days = days;
    }

    public String getKodemk() {
        return kodemk;
    }

    public void setKodemk(String kodemk) {
        this.kodemk = kodemk;
    }

    public String getMkName() {
        return mkname;
    }

    public void setMKName(String mkname) {
        this.mkname = mkname;
    }
    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }
    public String getProdiId() {
        return prodiId;
    }
    public void setProdiName(String prodiname) {
        this.prodiname = prodiname;
    }
    public String getProdiName() {
        return prodiname;
    }
    public void setProdiId(String prodiId) {
        this.prodiId = prodiId;
    }

    public void setSmt(String smt) {
        this.smt = smt;
    }
    public String getSmt() {
        return smt;
    }
    public void setTahunajaran(String tahunajaran) {
        this.tahunajaran = tahunajaran;
    }
    public String getTahunajaran() {
        return tahunajaran;
    }
    public void setSmt2(String smt2) {
        this.smt2 = smt2;
    }
    public String getSmt2() {
        return smt2;
    }
    public void setRuang(String ruang) {
        this.ruang = ruang;
    }
    public String getRuang() {
        return ruang;
    }
    public void setExam(String exam) {
        this.exam = exam;
    }
    public String getExam() {
        return exam;
    }
}
