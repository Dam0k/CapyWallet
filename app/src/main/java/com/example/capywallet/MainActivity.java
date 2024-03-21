package com.example.capywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Expense> expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentActivity_2 = new Intent(MainActivity.this, AddExpense.class);
                startActivity(intentActivity_2);
            }
        });

        displayExpenses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayExpenses();
    }

    private void displayExpenses() {
        expenses = dbHelper.getAllExpenses();
        ArrayList<String> expensesList = new ArrayList<>();
        for (Expense expense : expenses) {
            expensesList.add(expense.getName() + " - $" + expense.getAmount() + " (" + expense.getCategory() + ")");
        }

        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        listView.setAdapter(adapter);
    }
}
