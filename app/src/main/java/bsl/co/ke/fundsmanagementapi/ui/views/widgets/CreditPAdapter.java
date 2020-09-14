package bsl.co.ke.fundsmanagementapi.ui.views.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bsl.co.ke.fundsmanagementapi.R;
import bsl.co.ke.fundsmanagementapi.model.CreditP;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditPAdapter extends RecyclerView.Adapter<CreditPAdapter.CustomViewHolder> {
    private Context mContext;
    private List<CreditP> creditPurchaseList;

    @NonNull
    @Override
    public CreditPAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.credit_list_purchase_activity, null);

        CreditPAdapter.CustomViewHolder viewHolder = new CreditPAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditPAdapter.CustomViewHolder holder, int position) {
        CreditP creditPurchase = creditPurchaseList.get(position);

        holder.txtviewbankname.setText("Bank Name: " + creditPurchase.getBank__acc_namer());
        holder.txviewbankac.setText("Bank A/C:" + creditPurchase.getBank_acc_number());
        holder.txtviewmarikitino.setText("mar No: " + creditPurchase.getMarikiti_number());
        holder.txtviewamount.setText("Amount: " + creditPurchase.getAmount());
    }

    public CreditPAdapter(Context context) {
        this.mContext = context;
    }

    public void setItems(List<CreditP> creditPurchaseList) {
        this.creditPurchaseList = creditPurchaseList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != creditPurchaseList ? creditPurchaseList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtviewbankname)
        TextView txtviewbankname;
        @BindView(R.id.txviewbankac)
        TextView txviewbankac;
        @BindView(R.id.txtviewmarikitino)
        TextView txtviewmarikitino;
        @BindView(R.id.txtviewamount)
        TextView txtviewamount;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
