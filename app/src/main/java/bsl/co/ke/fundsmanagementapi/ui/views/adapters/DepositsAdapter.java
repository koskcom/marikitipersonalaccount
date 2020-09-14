package ics.co.ke.businessaccount.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.pojo.DManager;


public class DepositsAdapter extends BaseAdapter {
    Activity context;
    private ArrayList<DManager> dManagerlist;
    private LayoutInflater inflater = null;

    public DepositsAdapter(Context context, ArrayList<DManager> dManagerlist) {
        this.dManagerlist = dManagerlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return dManagerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.deposit_adapter, null) : itemview;

        TextView no = (TextView) itemview.findViewById(R.id.no);
        TextView textviewid = (TextView) itemview.findViewById(R.id.textviewid);

        DManager selecteditem = dManagerlist.get(position);
        no.setText("No:" + selecteditem.getId());
        textviewid.setText("Id No:" + selecteditem.getUser_national_id());

        return itemview;

    }
}