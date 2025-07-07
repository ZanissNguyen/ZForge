package org.zanissnguyen.ZForge.Files;

import java.util.List;

public interface zfileAPI 
{
	// constructor
	public void save();
	
	// get from file
	public List<String> getOptions(Boolean sorted, String... path);
	public String getString(String... path);
	public List<String> getList(String... path);
	public int getInt(String... path);
	public double getDouble(String... path);
	public Boolean getBool(String... path);
	public Object getObject(String... path);
}
