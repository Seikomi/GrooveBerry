package com.seikomi.grooveberry.dao;

import java.sql.Connection;

import com.seikomi.grooveberry.database.ConnectionH2Database;

public abstract class DAO<T> {
	public Connection connection = ConnectionH2Database.getInstance();

	public abstract T find(long id);
	public abstract T create(T object);
	public abstract T update(T object);
	public abstract void delete(T object);
}
