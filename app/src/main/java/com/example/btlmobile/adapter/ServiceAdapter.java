package com.example.btlmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Adapter for displaying Service items in a RecyclerView. */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Service service);
        void onItemLongClick(Service service);
    }

    private List<Service> services = new ArrayList<>();
    private OnItemClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public ServiceAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvName.setText(service.getName());
        holder.tvUnit.setText(service.getUnit());
        // Format price with unit: e.g. "3,500 đ/kWh"
        holder.tvPricePerUnit.setText(
                currencyFormat.format(service.getPricePerUnit()) + " đ/" + service.getUnit()
        );

        holder.itemView.setOnClickListener(v -> listener.onItemClick(service));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(service);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    /** Replace the current data set and refresh the list. */
    public void updateData(List<Service> newServices) {
        services.clear();
        services.addAll(newServices);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUnit, tvPricePerUnit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvPricePerUnit = itemView.findViewById(R.id.tvPricePerUnit);
        }
    }
}
