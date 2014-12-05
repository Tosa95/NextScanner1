package tosatto.nextscanner.main.notifier;

import java.util.ArrayList;
import java.util.List;

public class EventCategory {
	
	private volatile int priority;
	private volatile String category;
	private volatile List<String> catList;
	
	private void createList ()
	{
		String [] lst = category.split (":");
		
		catList = new ArrayList<String>();
		
		for (int i = 0; i < lst.length; i++)
		{
			if (lst[i] != "") catList.add(lst[i]);
		}
	}
	
	public EventCategory (String category, int priority)
	{
		this.priority = priority;
		this.category = category;
		
		createList();
	}
	
	public EventCategory (String category)
	{
		this.priority = 0;
		this.category = category;
		
		createList();
	}
	
	public String getCategory ()
	{
		return category;
	}
	
	public int getPriority ()
	{
		return priority;
	}
	
	public boolean eventCompatible (EventCategory eCat)
	{
		if (categoryCompatible(eCat) && eCat.getPriority() >= this.getPriority())
			return true;
		else
			return false;
	}
	
	public boolean categoryCompatible (EventCategory eCat)
	{
		for (int i = 0; i < catList.size(); i++)
		{
			String c1 = eCat.catList.get(i);
			String c2 = catList.get(i);
			
			boolean b = !c1.equals(c2);
			
			if (i >= eCat.catList.size() || b)
				return false;
		}
		
		return true;
	}
	
	public boolean sameCategory (EventCategory eCat)
	{
		int i;
		
		for (i = 0; i < catList.size(); i++)
		{
			String c1 = eCat.catList.get(i);
			String c2 = catList.get(i);
			
			boolean b = !c1.equals(c2);
			
			if (i >= eCat.catList.size() || b)
				return false;
		}
		
		if (i == eCat.catList.size())
			return true;
		else
			return false;
	}
}
