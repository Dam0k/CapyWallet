package com.example.capywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Expense> expenses;
    private ListView listView;
    private Button profileButton;


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

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });
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
            expensesList.add(expense.getName() + " - " + expense.getAmount() + " Kč (" + expense.getCategory() + ")");
        }

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteExpense(position);
            }
        });

    }

    private void deleteExpense(int position) {
        if (position >= 0 && position < expenses.size()) {
            Expense expenseToDelete = expenses.get(position);
            dbHelper.deleteExpense(expenseToDelete.getId());
            expenses = dbHelper.getAllExpenses();
            updateListView(expenses);
        } else {
            Toast.makeText(this, "Invalid expense", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateListView(List<Expense> updatedExpenses) {
        ArrayList<String> expensesList = new ArrayList<>();
        for (Expense expense : updatedExpenses) {
            expensesList.add(expense.getName() + " - " + expense.getAmount() + " Kč (" + expense.getCategory() + ")");
        }
        adapter.clear();
        adapter.addAll(expensesList);
        adapter.notifyDataSetChanged();
    }


}
