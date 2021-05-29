package com.ahmedalraziki.g_admin_final.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedalraziki.g_admin_final.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FListFinAdapter extends RecyclerView.Adapter<FListFinAdapter.ViewHolder>{

    private Context context;
    private int mode;
    private List<Income> incomes;
    private List<Outlay> outlays;
    private OnItemListener onItemListener;

    public FListFinAdapter(Context context, int mode, List<Income> incomes, List<Outlay> outlays,
                           OnItemListener onItemListener) {
        this.context = context;
        this.mode = mode;
        this.incomes = incomes;
        this.outlays = outlays;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fin_list_row,
                parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (mode == 1){
            Income tempIncome = incomes.get(position);

            holder.id.setText(tempIncome.getId());
            holder.am.setText(tempIncome.getAmount());
            holder.da.setText(tempIncome.getDate());
            holder.ty.setText(tempIncome.getType());
            holder.so.setText(tempIncome.getFromAny());
        } else {
            Outlay tempOutlay = outlays.get(position);

            holder.id.setText(tempOutlay.getId());
            holder.am.setText(tempOutlay.getAmount());
            holder.da.setText(tempOutlay.getDate());
            holder.ty.setText(tempOutlay.getType());
            holder.so.setText(tempOutlay.getFromAny());
        }
    }

    @Override
    public int getItemCount() {
        if (mode == 1){return incomes.size();}
        else { return outlays.size();}
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView id;
        TextView am;
        TextView da;
        TextView ty;
        TextView so;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            this.onItemListener = onItemListener;
            id = itemView.findViewById(R.id.finlistID);
            am= itemView.findViewById(R.id.finlistAm);
            da= itemView.findViewById(R.id.finlistDa);
            ty= itemView.findViewById(R.id.finlistTy);
            so= itemView.findViewById(R.id.finlistSo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
