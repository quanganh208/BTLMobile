package com.example.btlmobile.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlmobile.R;
import com.example.btlmobile.adapter.RoomAdapter;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;

/** Search rooms by room number or description. */
public class SearchActivity extends AppCompatActivity {

    private RoomRepository roomRepository;
    private RoomAdapter adapter;
    private List<Room> displayedRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tìm kiếm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        roomRepository = RoomRepository.getInstance();
        displayedRooms = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RoomAdapter(displayedRooms, new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Room room) {
                Toast.makeText(SearchActivity.this,
                        "Phòng " + room.getRoomNumber(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Room room) {
                // no-op in search context
            }
        });
        recyclerView.setAdapter(adapter);

        EditText editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterRooms(s.toString().trim());
            }
        });

        // Show all rooms initially
        filterRooms("");
    }

    private void filterRooms(String keyword) {
        List<Room> all = roomRepository.getAll();
        displayedRooms.clear();
        if (keyword.isEmpty()) {
            displayedRooms.addAll(all);
        } else {
            String lower = keyword.toLowerCase();
            for (Room room : all) {
                boolean matchNumber = room.getRoomNumber() != null
                        && room.getRoomNumber().toLowerCase().contains(lower);
                boolean matchDesc = room.getDescription() != null
                        && room.getDescription().toLowerCase().contains(lower);
                if (matchNumber || matchDesc) {
                    displayedRooms.add(room);
                }
            }
        }
        adapter.updateData(new ArrayList<>(displayedRooms));
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
