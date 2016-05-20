/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bench;


import java.util.List;

/**
 *
 * @author Muhammad
 */
public class TransactionalData {
    
private int totalCount;
private int page;
private List<Transaction> transactions;

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
  
  
}
