package tosatto.nextscanner.main;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
	
	private final static List<Thread> tList = new ArrayList<Thread>();
	
	private ThreadManager ()
	{
		
	}
	
	public static void addThread (Thread t)
	{
		tList.add(t);
	}
	
	public static void killAll ()
	{
		for (Thread t : tList)
		{
			try
			{
				t.stop();
			}
			catch (Exception ex)
			{
				
			}

		}
	}
	
	public static int getThreadNumber ()
	{
		return tList.size();
	}
}
