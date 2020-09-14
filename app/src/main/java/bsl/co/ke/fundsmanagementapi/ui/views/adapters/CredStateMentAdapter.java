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
import bsl.co.ke.fundsmanagementapi.pojo.BSendPojo;
import bsl.co.ke.fundsmanagementapi.pojo.CreditPurchase;

public class CredStateMentAdapter extends BaseAdapter {
    Activity context;
    private ArrayList<CreditPurchase> creditPurchaselist;
    private LayoutInflater inflater = null;

    public CredStateMentAdapter(Context context, ArrayList<CreditPurchase> creditPurchaselist) {
        this.creditPurchaselist = creditPurchaselist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return (null != creditPurchaselist ? creditPurchaselist.size() : 0);
        //return dManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return creditPurchaselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.credit_purchase_statement_list, null) : itemview;


        TextView customername = (TextView) itemview.findViewById(R.id.textviewcustomername);
        TextView txtpphonenumber = (TextView) itemview.findViewById(R.id.textviewphonenumber);
        TextView txtcredit = (TextView) itemview.findViewById(R.id.textviewcredit);
        TextView txtdebit = (TextView) itemview.findViewById(R.id.textviewdebit);
        TextView textdate = (TextView) itemview.findViewById(R.id.textviewdate);

        CreditPurchase selecteditem = creditPurchaselist.get(position);

        customername.setText(" Name: " + selecteditem.getBank__acc_namer());
        txtpphonenumber.setText("  " + selecteditem.getBank_acc_number());
        txtcredit.setText("  " + selecteditem.getMarikiti_number());
        txtdebit.setText("  " + selecteditem.getAmount());
        textdate.setText("  " + selecteditem.getTrans_type());

        return itemview;

    }
}

