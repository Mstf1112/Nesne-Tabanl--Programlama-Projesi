package com.example.logsignsql;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logsignsql.databinding.ActivityHomeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding2;
    PlayerDB playerDB;

    private ListView listView;
    private List<Player> playerList;

    private PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding2 = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());
        playerDB = new PlayerDB(this);

        listView = findViewById(R.id.listView);

        // Veritabanından oyuncu verilerini al
        playerList = playerDB.getPlayerList();

        //ADAPTERLERİ AYARLA
        playerAdapter = new PlayerAdapter();
        listView.setAdapter(playerAdapter);

        // LİSTWİEW İTEM CLİCK
        listView.setOnItemClickListener((parent, view, position, id) -> {
            final Player player = (Player) parent.getItemAtPosition(position);
            Intent intent = new Intent(Home.this, SakatlikList.class);
            intent.putExtra("playerName",player.getName());
            intent.putExtra("playerId",player.getId());
            startActivity(intent);
        });
        //FLOATİNG BT ID BULMA
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(binding2.fab.getContext());
                dialogBuilder.setTitle("Oyuncu Adı");

                // AD GİRMEK İÇİN EDİT TEXT BULMA
                final EditText nameEditText = new EditText(binding2.fab.getContext());
                nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(nameEditText);

                dialogBuilder.setPositiveButton("Ekle", (dialogInterface, i) -> {
                    String playerName = nameEditText.getText().toString();
                    boolean inserted = playerDB.insertData(playerName);
                    if (inserted) {
                        playerList = playerDB.getPlayerList();
                        playerAdapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.setNegativeButton("İptal", null);

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }
    private class PlayerAdapter extends ArrayAdapter<Player> {

        PlayerAdapter() {
            super(Home.this, R.layout.player_list_item, playerList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
            }

            Player player = getItem(position);

            TextView textViewName = convertView.findViewById(R.id.player_name);

            textViewName.setText(player.getName());

            return convertView;
        }

        @Override
        public int getCount() {
            return playerList.size();
        }

        @Nullable
        @Override
        public Player getItem(int position) {
            return playerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}



