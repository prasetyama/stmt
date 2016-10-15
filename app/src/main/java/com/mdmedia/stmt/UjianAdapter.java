package com.mdmedia.stmt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arief on 09/10/2015.
 */
public class UjianAdapter extends BaseAdapter {
    Context context;
    ArrayList<Jadwal> listData;
    String semester;

    public UjianAdapter(Context context,ArrayList<Jadwal> listData){
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder {
        private TextView textViewMKName;
        private TextView textViewJam;
        private TextView textViewLecture;
        private TextView textViewSmt;
        private TextView textViewRuang;
        private TextView textViewExam;
        //private TextView textViewTanggal;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_ujian,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewMKName = (TextView)view.findViewById(R.id.mkname);
            //viewHolder.textViewTanggal = (TextView)view.findViewById(R.id.tanggal);
            viewHolder.textViewJam = (TextView)view.findViewById(R.id.jam);
            viewHolder.textViewLecture = (TextView)view.findViewById(R.id.pengawasujian);
            viewHolder.textViewSmt = (TextView)view.findViewById(R.id.smt2);
            viewHolder.textViewRuang = (TextView)view.findViewById(R.id.ruangan);
            viewHolder.textViewExam = (TextView)view.findViewById(R.id.examan);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Jadwal jadwal = listData.get(position);
        String jadwalMKName = jadwal.getMkName();
        //String jadwaltanggal = jadwal.getTanggal();
        String jadwalJam = jadwal.getJam();
        String jadwalLecture = jadwal.getLecture();
        String jadwalSmt = jadwal.getSmt();
        String ruang = jadwal.getRuang();
        String exam = jadwal.getExam();

        if(jadwalSmt.equals("1")){
            semester = "Ganjil";
        } else if(jadwalSmt.equals("2")){
            semester = "Genap";
        }
        else if (jadwalSmt.equals("3")){
            semester = "Semester pendek ganjil";
        }
        else if (jadwalSmt.equals("4")){
            semester = "Semester pendek genap";
        }

        viewHolder.textViewMKName.setText(jadwalMKName);
        //viewHolder.textViewTanggal.setText(jadwaltanggal);
        viewHolder.textViewJam.setText(jadwalJam);
        viewHolder.textViewLecture.setText(jadwalLecture);
        viewHolder.textViewSmt.setText(semester);
        viewHolder.textViewRuang.setText(ruang);
        viewHolder.textViewExam.setText(exam);
        return view;
    }
}
