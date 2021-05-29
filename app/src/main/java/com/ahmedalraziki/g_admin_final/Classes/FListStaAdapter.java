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

public class FListStaAdapter extends RecyclerView.Adapter<FListStaAdapter.ViewHolder>{

    Context context;
    private List<Staff> staffList;
    private OnItemListener onItemListener;

    public FListStaAdapter(Context context, List<Staff> staffList, OnItemListener onItemListener) {
        this.context = context;
        this.staffList = staffList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_list_row, parent
        , false);
        return new FListStaAdapter.ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Staff member = staffList.get(position);

        String level = String.valueOf(member.getLvl());
        holder.idTxt.setText(member.getId());
        holder.nameTxt.setText(member.getName());
        holder.levelTxt.setText(level);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemListener onItemListener;
        TextView idTxt;
        TextView nameTxt;
        TextView levelTxt;

         public ViewHolder(@NonNull @NotNull View itemView, OnItemListener onItemListener) {
             super(itemView);
             this.onItemListener = onItemListener;
             itemView.setOnClickListener(this);

             idTxt = itemView.findViewById(R.id.staLisRow_ID);
             nameTxt = itemView.findViewById(R.id.staLisRow_Name);
             levelTxt = itemView.findViewById(R.id.staLisRow_Level);

         }


         @Override
         public void onClick(View v) { onItemListener.onItemClick(getAdapterPosition());}
     }

    public interface OnItemListener {
        void onItemClick(int position);
    }


}
