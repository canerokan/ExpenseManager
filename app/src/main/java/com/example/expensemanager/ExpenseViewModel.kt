package com.example.expensemanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>(emptyList())
    val expenses: LiveData<List<Expense>> = _expenses

    fun addExpense(expense: Expense) {
        _expenses.value = _expenses.value?.plus(expense)
    }

    val totalExpense: Double
        get() = _expenses.value?.sumOf { it.amount } ?: 0.0
}
