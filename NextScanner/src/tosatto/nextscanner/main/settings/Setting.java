package tosatto.nextscanner.main.settings;

public class Setting {
	String name;
	Object value;
	
	public Setting (String name, Object value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public Object getValue ()
	{
		return value;
	}
}
