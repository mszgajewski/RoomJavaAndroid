package com.mszgajewski.roomjavaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.mszgajewski.roomjavaandroid.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PersonDatabase personDatabase;
    List<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        personDatabase = Room.databaseBuilder(getApplicationContext(),PersonDatabase.class,"PersonDB")
                        .addCallback(myCallback).build();

        binding.saveButton.setOnClickListener(v -> {
           String name = binding.nameEdit.getText().toString();
           String age = binding.ageEdit.getText().toString();

           Person p1 = new Person(name,age);

        });

        binding.getDataButton.setOnClickListener(v -> getPersonInBackground());
    }

    public void addPersonInBackground(Person person) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            personDatabase.getPersonDAO().addPerson(person);

            handler.post(() -> Toast.makeText(MainActivity.this, "Dodano do bazy ", Toast.LENGTH_SHORT).show());
        });
    }

    public void getPersonInBackground() {

        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            personList = personDatabase.getPersonDAO().getAllPerson();

            handler.post(() -> {
                for (Person p : personList) {
                    stringBuilder.append(p.getName()).append(" : ").append(p.getAge()).append("\n");
                }
                String finalData = stringBuilder.toString();
                Toast.makeText(MainActivity.this, "" + finalData, Toast.LENGTH_SHORT).show();
            });
        });
    }
}