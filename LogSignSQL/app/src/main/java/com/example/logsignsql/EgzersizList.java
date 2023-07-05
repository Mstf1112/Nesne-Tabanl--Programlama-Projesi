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

import com.example.logsignsql.databinding.ActivityEgzersizBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EgzersizList extends AppCompatActivity {
    //ACTİVİTY VE DB TANIMLAMA
    ActivityEgzersizBinding EgzersizBinding;
    EgzersizDB EgzersizDB;

    private int playerId;
    private String playerName;
    private List<Egzersiz> EgzersizList;
    private EgzersizAdapter egzersizAdapter;

    @Override
    //TELEFON DÖNÜNCE DEĞERLER KAYIYOR savedInstanceState ile yazarsan
    //Metot içindeki parametreler "bundle" tipindeki parametreye alınarak koruma altına alınır
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EgzersizBinding = ActivityEgzersizBinding.inflate(getLayoutInflater());
        //GETROOT İLE KÖK GÖRÜNÜMÜNDEN BELİRTİLEN AKTİVİTEYE GÖNDERİLİR
        setContentView(EgzersizBinding.getRoot());
        EgzersizDB = new EgzersizDB(this);
         //VERİ BAĞLAMA VE TIKLAMA BURADA GERÇEKLEŞİYOR
        //EGZERSİZLİSTİN ID SİNE SAHİP LİSTWİEWİ BULUYOR
        ListView listView = findViewById(R.id.EgzersizList);

        // Veritabanından oyuncu verilerini al
        Intent intent = getIntent();
        playerId =intent.getIntExtra("playerId",0);
        playerName = intent.getStringExtra("playerName");
        System.out.println(playerName);
        System.out.println(playerId);
        EgzersizList = EgzersizDB.getEgzersizList(playerId);

        // ADAPTER AYARLARI
        egzersizAdapter = new EgzersizAdapter();
        listView.setAdapter(egzersizAdapter);

        TextView playerText = (TextView)findViewById(R.id.player_name);
        playerText.setText(playerName);

        // Listview item click
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            final String item = (String) parent.getItemAtPosition(position);
//            playerList.remove();
//            playerAdapter.notifyDataSetChanged();
//        });
        //XML DEKİ FLOATİNGBUTTONLARI BULUYOR

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.egzersiz_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(EgzersizBinding.egzersizFab.getContext());
                dialogBuilder.setTitle("Egzersiz");

                // AD GİRMEK İÇİN EDİTTEXT OLUŞTURMA
                final EditText nameEditText = new EditText(EgzersizBinding.egzersizFab.getContext());
                nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(nameEditText);

                dialogBuilder.setPositiveButton("Ekle", (dialogInterface, i) -> {
                    String egzersiz = nameEditText.getText().toString();
                    System.out.println(playerId);
                    boolean inserted = EgzersizDB.insertData(playerId,egzersiz);
                    if (inserted) {
                        EgzersizList = EgzersizDB.getEgzersizList(playerId);
                        egzersizAdapter.notifyDataSetChanced();
                    }else{
                        System.out.println("Eklenmedi");
                    }
                });
                //EGZERİSZ GİRME DİYALOGU GİRMESİ İÇİN
                dialogBuilder.setNegativeButton("İptal", null);

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }
    private class EgzersizAdapter extends ArrayAdapter<Egzersiz> {
        //Bu kod parçası EgzersizAdapter sınıfının yapıcı metodunu tanımlayarak adapterin oluşturulduğu aktivite
        //liste öğesi görünümü ve veri kaynağını belirler. Bu adapter, belirtilen veri kaynağından verileri alacak
        //ve belirtilen görünüm dosyasını kullanarak ListView'de her bir liste öğesini oluşturacak
       EgzersizAdapter() {
             super(EgzersizList.this, R.layout.egzersiz_list_item, EgzersizList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.egzersiz_list_item, parent, false);
            }

            Egzersiz egzersiz = getItem(position);

            TextView textViewName = convertView.findViewById(R.id.Egzersiz_name);
            if (egzersiz != null) {
                textViewName.setText(egzersiz.getName());
            }else{
                textViewName.setText("NULL");
            }

            return convertView;
        }
        //CLASLAR
        @Override
        public int getCount() {
            return EgzersizList.size();
        }

        @Nullable
        @Override
        public Egzersiz getItem(int position) {
            return EgzersizList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void notifyDataSetChanced() {
        }
    }

}
