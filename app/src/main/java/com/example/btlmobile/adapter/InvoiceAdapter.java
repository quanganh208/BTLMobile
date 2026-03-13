package com.example.btlmobile.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.InvoiceStatus;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.repository.RoomRepository;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Adapter for displaying Invoice items in a RecyclerView. */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Invoice invoice);
        void onItemLongClick(Invoice invoice);
    }

    private List<Invoice> invoices = new ArrayList<>();
    private OnItemClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public InvoiceAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);

        // Show room number instead of raw invoice id
        Room room = RoomRepository.getInstance().getById(invoice.getRoomId());
        if (room != null) {
            holder.tvInvoiceId.setText("Phòng " + room.getRoomNumber());
        } else {
            holder.tvInvoiceId.setText(invoice.getId());
        }

        holder.tvTotalAmount.setText(currencyFormat.format(invoice.getTotalAmount()) + " đ");
        holder.tvDueDate.setText(invoice.getDueDate());

        // Status badge coloring
        InvoiceStatus status = invoice.getStatus();
        if (status == InvoiceStatus.PAID) {
            holder.tvStatus.setText("Đã thanh toán");
            holder.tvStatus.setTextColor(Color.parseColor("#FF4CAF50"));
        } else if (status == InvoiceStatus.OVERDUE) {
            holder.tvStatus.setText("Quá hạn");
            holder.tvStatus.setTextColor(Color.parseColor("#FFF44336"));
        } else {
            holder.tvStatus.setText("Chờ thanh toán");
            holder.tvStatus.setTextColor(Color.parseColor("#FFF44336"));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(invoice));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(invoice);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    /** Replace the current data set and refresh the list. */
    public void updateData(List<Invoice> newInvoices) {
        invoices.clear();
        invoices.addAll(newInvoices);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceId, tvTotalAmount, tvDueDate, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInvoiceId = itemView.findViewById(R.id.tvInvoiceId);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
