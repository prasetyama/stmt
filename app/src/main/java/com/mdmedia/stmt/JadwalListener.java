package com.mdmedia.stmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arief on 09/10/2015.
 */
public interface JadwalListener {
    public void addJadwal(Jadwal jadwal);

    public ArrayList<Jadwal> getAllJadwal(String days, String spinnervalue, String smt, String ta);
    //public ArrayList<Jadwal> getAllJadwal(String spinnervalue, String setTanggal);
    public List<String> getAllProdi();
    public List<Jadwal> getProdiId(String prodi);
    public List<Jadwal> getPeriode(String date);
    public List<String> getPeriodedropdown(String date);

    public List<Jadwal> getapiprodi();

    public int getJadwalCount();

    public int getJadwalSmtCount(String smt, String ta);
    //public int getJadwalSmt(String days, String smt, String ta);

    public int getSMTJadwalCount(String days, String spinnervalue, String smt, String ta);

    public int getJadwalSmtUjianCount(String tanggal, String smt, String ta, String prodiname, String exam);
    public void addJadwalUjian(Jadwal jadwal);
    public ArrayList<Jadwal> getAllJadwalUjian(String tanggal, String spinnervalue, String smt, String ta, String exam);
}
