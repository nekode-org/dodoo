package com.nykniu.dodoo.poco;

public class ProviderField {
    private String tableName;
    private String columnName;
    private String filterName;

    public ProviderField(String tableName, String columnName, String filterName) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.filterName = filterName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String limitName) {
        this.filterName = limitName;
    }

}
