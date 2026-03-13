package com.example.btlmobile.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Adapter for displaying Room items in a RecyclerView. */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Room room);
        void onItemLongClick(Room room);
    }

    private List<Room> rooms = new ArrayList<>();
    private OnItemClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public RoomAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvRoomNumber.setText(room.getRoomNumber());
        holder.tvFloor.setText(room.getFloor());
        holder.tvPrice.setText(currencyFormat.format(room.getPrice()) + " đ");
        holder.tvArea.setText(room.getArea() + " m²");

        // Status text and color
        RoomStatus status = room.getStatus();
        if (status == RoomStatus.AVAILABLE) {
            holder.tvStatus.setText("Còn trống");
            holder.tvStatus.setTextColor(Color.parseColor("#FF4CAF50"));
        } else if (status == RoomStatus.OCCUPIED) {
            holder.tvStatus.setText("Đã thuê");
            holder.tvStatus.setTextColor(Color.parseColor("#FFF44336"));
        } else {
            holder.tvStatus.setText("Bảo trì");
            holder.tvStatus.setTextColor(Color.parseColor("#FFFF9800"));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(room));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(room);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    /** Replace the current data set and refresh the list. */
    public void updateData(List<Room> newRooms) {
        rooms.clear();
        rooms.addAll(newRooms);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvFloor, tvPrice, tvArea, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvFloor = itemView.findViewById(R.id.tvFloor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
