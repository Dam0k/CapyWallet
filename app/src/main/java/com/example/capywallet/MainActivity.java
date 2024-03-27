package com.example.capywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Expense> expenses;
    private ListView listView;

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
            expensesList.add(expense.getName() + " - " + expense.getAmount() + " Kč (" + expense.getCategory() + ")");
        }

        listView = findViewById(R.id.listView); // Use the class-level listView variable
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
            // Fetch the updated list of expenses from the database
            expenses = dbHelper.getAllExpenses();
            // Update the ListView with the updated list of expenses
            updateListView(expenses);
        } else {
            Toast.makeText(this, "Invalid expense", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to update the ListView with a new list of expenses
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
