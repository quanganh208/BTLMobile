package com.example.btlmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.Tenant;
import com.example.btlmobile.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

/** Adapter for displaying Tenant items in a RecyclerView. */
public class TenantAdapter extends RecyclerView.Adapter<TenantAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Tenant tenant);
        void onItemLongClick(Tenant tenant);
    }

    private List<Tenant> tenants = new ArrayList<>();
    private OnItemClickListener listener;

    public TenantAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tenant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tenant tenant = tenants.get(position);
        holder.tvName.setText(tenant.getName());
        holder.tvPhone.setText(tenant.getPhone());
        holder.tvEmail.setText(tenant.getEmail());

        // Look up room number from repository instead of showing raw roomId
        Room room = RoomRepository.getInstance().getById(tenant.getRoomId());
        if (room != null) {
            holder.tvRoomId.setText("Phòng " + room.getRoomNumber());
        } else {
            holder.tvRoomId.setText(tenant.getRoomId());
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(tenant));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(tenant);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tenants.size();
    }

    /** Replace the current data set and refresh the list. */
    public void updateData(List<Tenant> newTenants) {
        tenants.clear();
        tenants.addAll(newTenants);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvEmail, tvRoomId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
        }
    }
}
