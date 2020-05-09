import java.util.ArrayList;

public class Path 
{
	private boolean is_disarmed;		// if this path disarm the specific bomb successfully.
	private ArrayList<Passable> path;	// the tiles passed in this path.
	private int time_consumed_for_one_bomb;	// the time consumed disarming the specific bomb if successfully.
	private int states_examined;
		
	public Path(ArrayList<Passable> path, int time_consumed_for_one_bomb, int states_examined, boolean is_disarmed)
	{
		this.path = path;
		this.time_consumed_for_one_bomb = time_consumed_for_one_bomb;
		this.states_examined = states_examined;
		this.is_disarmed = is_disarmed;
	}
	
	public ArrayList<Passable> get_path()
	{
		return path;
	}
	
	public int get_time_consumed_for_one_bomb()
	{
		return time_consumed_for_one_bomb;
	}
	
	public int get_states_examined()
	{
		return states_examined;
	}
	
	public boolean get_is_disarmed()
	{
		return is_disarmed;
	}
}
