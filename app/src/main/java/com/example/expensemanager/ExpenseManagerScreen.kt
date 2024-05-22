package com.example.expensemanager

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ExpenseManagerScreen() {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Expense Manager",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 30.sp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Enter expense name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Enter expense amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = expenseDate,
            onValueChange = { expenseDate = it },
            label = { Text("Enter expense date (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val amount = expenseAmount.toDoubleOrNull()
                val date = LocalDate.parse(expenseDate, DateTimeFormatter.ISO_DATE)
                if (expenseName.isNotEmpty() && amount != null) {
                    expenses = expenses + Expense(expenseName, amount, date)
                    Toast.makeText(
                        context,
                        "Expense added: $expenseName - $$amount",
                        Toast.LENGTH_SHORT
                    ).show()
                    expenseName = ""
                    expenseAmount = ""
                    expenseDate = LocalDate.now().toString()
                } else {
                    Toast.makeText(
                        context,
                        "Please enter a valid name, amount, and date",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Yeşil rengi
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Add Expense", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val groupedExpenses = groupExpensesByMonth(expenses)
            groupedExpenses.forEach { (month, monthlyExpenses) ->
                item {
                    MonthSection(month, monthlyExpenses)
                }
            }
        }
    }
}

@Composable
fun MonthSection(month: String, expenses: List<Expense>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = month,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
            color = Color.Black
        )

        val totalExpense = expenses.sumOf { it.amount }
        Text(
            text = "Total: $$totalExpense",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        expenses.forEach { expense ->
            ExpenseItem(expense)
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Name: ${expense.name}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
            Text(text = "Amount: $${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
            Text(text = "Date: ${expense.date}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
        }
    }
}

fun groupExpensesByMonth(expenses: List<Expense>): Map<String, List<Expense>> {
    return expenses.groupBy {
        it.date.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
    }
}
