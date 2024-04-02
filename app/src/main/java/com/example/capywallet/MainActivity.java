package com.example.capywallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PROFILE = 101;

    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Expense> expenses;
    private ListView listView;
    private Button profileButton;
    private TextView budgetAmount;


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
                startActivityForResult(profileIntent, REQUEST_CODE_PROFILE);
            }
        });

        budgetAmount = findViewById(R.id.budgetAmount);
        updateBudgetAmount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBudgetAmount();
        displayExpenses();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PROFILE && resultCode == Activity.RESULT_OK) {
            // Update the budget amount
            updateBudgetAmount();
            Toast.makeText(this, "Income saved successfully", Toast.LENGTH_SHORT).show();
        }
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

            // Recalculate remaining budget
            updateBudgetAmount();
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

    private void updateBudgetAmount() {
        double income = dbHelper.getIncome();
        double totalExpenses = dbHelper.getTotalExpenses(); // Add this method to DatabaseHelper
        double remainingBudget = income - totalExpenses;

        budgetAmount.setText(String.valueOf(remainingBudget));

        // Set text color based on remaining budget
        if (remainingBudget < 0) {
            budgetAmount.setTextColor(getResources().getColor(R.color.negative_balance));
        } else {
            budgetAmount.setTextColor(getResources().getColor(R.color.app_color));
        }
    }


}