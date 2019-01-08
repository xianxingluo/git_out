package com.ziyun.borrow.model;

import com.ziyun.common.model.ParamsStatus;

public class BorrowParamsVo extends ParamsStatus {
    //借阅数量排序，0：升序，1：降序
    private Integer borrowNumSort;
    private String borrowNumSortStr;

    public String getBorrowNumSortStr() {
        return borrowNumSortStr;
    }

    public void setBorrowNumSortStr(String borrowNumSortStr) {
        this.borrowNumSortStr = borrowNumSortStr;
    }

    public Integer getBorrowNumSort() {
        return borrowNumSort;
    }

    public void setBorrowNumSort(Integer borrowNumSort) {
        this.borrowNumSort = borrowNumSort;
    }

}
