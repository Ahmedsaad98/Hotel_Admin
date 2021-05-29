package com.ahmedalraziki.g_admin_final.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ahmedalraziki.g_admin_final.R;
import java.util.List;

public class FListResAdapter extends RecyclerView.Adapter<FListResAdapter.ViewHolder> {

    private Context context;
    private List<Reservation> Hitems;
    private OnItemListener onItemListener;

    public FListResAdapter(Context context, List<Reservation> Hitems, OnItemListener onItemListener){
        this.context = context;
        this.Hitems = Hitems;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_list_row, parent , false);
        return new FListResAdapter.ViewHolder(view , onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation m = Hitems.get(position);

        holder.ResID.setText(m.getId());
        if (m.getRoomNo() != null && !m.getRoomNo().equals("")){
        holder.Room.setText(m.getRoomNo());
        holder.ResID.setText(m.getId());
        } else {
            holder.Room.setText("Not Checked In");
            holder.ResID.setText(m.getId());
        }
    }


    @Override
    public int getItemCount() {
        return Hitems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ResID ;
        TextView Room;

        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView , OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener = onItemListener;
            ResID = itemView.findViewById(R.id.vlr_tri);
            Room = itemView.findViewById(R.id.vlr_trr);

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
