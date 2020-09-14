package bsl.co.ke.fundsmanagementapi.ui.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.pojo.WManager;

public class WithdrawalManagerAdapter extends BaseAdapter {
    Activity context;
    private ArrayList<WManager> wManagerlist;
    private LayoutInflater inflater = null;

    public WithdrawalManagerAdapter(Context context, ArrayList<WManager> wManagerlist) {
        this.wManagerlist = wManagerlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return wManagerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.activity_withdrawal_list, null) : itemview;

        TextView no = (TextView) itemview.findViewById(R.id.no);
        TextView textviewid = (TextView) itemview.findViewById(R.id.textviewid);

        WManager selecteditem = wManagerlist.get(position);
        textviewid.setText("NO: " + selecteditem.getId());
        no.setText("ID No: " + selecteditem.getId());

        return itemview;

    }
}

