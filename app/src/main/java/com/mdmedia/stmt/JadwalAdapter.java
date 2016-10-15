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
public class JadwalAdapter extends BaseAdapter {
    Context context;
    ArrayList<Jadwal> listData;
    String semester;

    public JadwalAdapter(Context context,ArrayList<Jadwal> listData){
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
        private TextView textViewKodeMK;
        private TextView textViewKelas;
        private TextView textViewJam;
        private TextView textViewLecture;
        private TextView textViewSmt;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewMKName = (TextView)view.findViewById(R.id.mkname);
            viewHolder.textViewKodeMK = (TextView)view.findViewById(R.id.kdmk);
            viewHolder.textViewKelas = (TextView)view.findViewById(R.id.kelas);
            viewHolder.textViewJam = (TextView)view.findViewById(R.id.jam);
            viewHolder.textViewLecture = (TextView)view.findViewById(R.id.lecture2);
            viewHolder.textViewSmt = (TextView)view.findViewById(R.id.smt2);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Jadwal jadwal = listData.get(position);
        String jadwalMKName = jadwal.getMkName();
        String jadwalKodeMK = jadwal.getKodemk();
        String jadwalKelas = jadwal.getKelas();
        String jadwalJam = jadwal.getJam();
        String jadwalLecture = jadwal.getLecture();
        String jadwalSmt = jadwal.getSmt();

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
        viewHolder.textViewKodeMK.setText(jadwalKodeMK);
        viewHolder.textViewKelas.setText(jadwalKelas);
        viewHolder.textViewJam.setText(jadwalJam);
        viewHolder.textViewLecture.setText(jadwalLecture);
        viewHolder.textViewSmt.setText(semester);
        return view;
    }
}
