package com.hortonworks.bench;

public class Query {

    public String name;
    public String sql;
    
    public Query(
        String name,
        String sql )
    {
        this.name=name;
	//Remove any ending semi-column
        this.sql=sql.replaceAll(";+\\s*$", "");
    }
}
