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

import com.example.logsignsql.databinding.ActivitySakatliklarBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SakatlikList extends AppCompatActivity {

    ActivitySakatliklarBinding sakatliklarBinding;
    SakatlikDB sakatlikDB;

    private int playerId;
    private String playerName;
    private List<Sakatlik> sakatlikList;
    private SakatlikAdapter sakatlikAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sakatliklarBinding = ActivitySakatliklarBinding.inflate(getLayoutInflater());
        setContentView(sakatliklarBinding.getRoot());
        sakatlikDB = new SakatlikDB(this);

        ListView listView = findViewById(R.id.sataklikList);

        // DB DEN OYUNCU VERİSİ ALMA
        Intent intent = getIntent();
        playerId =intent.getIntExtra("playerId",0);
        playerName = intent.getStringExtra("playerName");
        System.out.println(playerName);
        System.out.println(playerId);
        sakatlikList = sakatlikDB.getSakatlikList(playerId);

        //ADAPTER AYARI YİNE
        sakatlikAdapter = new SakatlikAdapter();
        listView.setAdapter(sakatlikAdapter);

        TextView playerText = (TextView)findViewById(R.id.player_name);
        playerText.setText(playerName);

        // Listview item click
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            final String item = (String) parent.getItemAtPosition(position);
//            playerList.remove();
//            playerAdapter.notifyDataSetChanged();
//        });
        //YİNE ACTİON BUTOON İNDEKS CART CURT
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.sakatlik_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(sakatliklarBinding.sakatlikFab.getContext());
                dialogBuilder.setTitle("Sakatlik");

                // AD GİRMEK İÇİN EDİTTEXT OLUŞTURMA
                final EditText nameEditText = new EditText(sakatliklarBinding.sakatlikFab.getContext());
                nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(nameEditText);

                dialogBuilder.setPositiveButton("Ekle", (dialogInterface, i) -> {
                    String sakatlik = nameEditText.getText().toString();
                    System.out.println(playerId);
                    boolean inserted = sakatlikDB.insertData(playerId,sakatlik);
                    if (inserted) {
                        sakatlikList = sakatlikDB.getSakatlikList(playerId);
                        sakatlikAdapter.notifyDataSetChanged();
                    }else{
                        System.out.println("Eklenmedi");
                    }
                });
                //DİYALOG ÇIKMASI
                dialogBuilder.setNegativeButton("İptal", null);

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }
    //Sakatlık adapter oluştur listwiev de görünüsün
    //super ile array adaptere çağır get view ile görüntü al convert et null ise bir önceki görünüm döner
    //getItem ile listedeki belli bir konuma sakatlık ögesini alır
    private class SakatlikAdapter extends ArrayAdapter<Sakatlik> {

        SakatlikAdapter() {
            super(SakatlikList.this, R.layout.sakatlik_list_item, sakatlikList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sakatlik_list_item, parent, false);
            }

            Sakatlik sakatlik = getItem(position);

            TextView textViewName = convertView.findViewById(R.id.sakatlik_name);
            if (sakatlik != null) {
                textViewName.setText(sakatlik.getName());
            }else{
                textViewName.setText("NULL");
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return sakatlikList.size();
        }

        @Nullable
        @Override
        public Sakatlik getItem(int position) {
            return sakatlikList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}



