package com.example.btlmobile.controller.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlmobile.R;
import com.example.btlmobile.adapter.RoomAdapter;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.repository.RoomRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/** Displays the full list of rooms with add/edit/delete actions. */
public class RoomListActivity extends AppCompatActivity {

    private RoomAdapter adapter;
    private RoomRepository roomRepository;

    private final ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        roomRepository = RoomRepository.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Danh sách phòng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RoomAdapter(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Room room) {
                Intent intent = new Intent(RoomListActivity.this, RoomDetailActivity.class);
                intent.putExtra("room_id", room.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Room room) {
                showDeleteDialog(room);
            }
        });

        recyclerView.setAdapter(adapter);
        refreshList();

        FloatingActionButton fab = findViewById(R.id.fabAddRoom);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditRoomActivity.class);
            addEditLauncher.launch(intent);
        });
    }

    private void refreshList() {
        adapter.updateData(roomRepository.getAll());
    }

    private void showDeleteDialog(Room room) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_room_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    roomRepository.delete(room.getId());
                    refreshList();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}
