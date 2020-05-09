import java.util.ArrayList;

public class Move 
{
	private ArrayList<Bomb> bombs_remaining;
	private ArrayList<Bomb> bombs_solved;
	private Passable start_point;			// the start point of this move
	private ArrayList<ArrayList<Passable>> paths;	// the paths taken in this move
	private int time_consumed_overall;		// the time consumed before taking this move(from the beginning of the game)
	
	public Move(ArrayList<Bomb> bombs_remaining, ArrayList<Bomb> bombs_solved, Passable start_point, int time_consumed_overall)
	{
		this.bombs_remaining = bombs_remaining;
		this.bombs_solved = bombs_solved;
		this.start_point = start_point;
		paths = new ArrayList<ArrayList<Passable>>();
		this.time_consumed_overall = time_consumed_overall;
	}
	
	public ArrayList<Bomb> get_bombs_solved()
	{
		return bombs_solved;
	}
	
	public int get_bombs_solved_number()
	{
		return bombs_solved.size();
	}
	
	public int get_time_consumed_overall()
	{
		return time_consumed_overall;
	}
	
	public ArrayList<Bomb> get_bombs_remaining()
	{
		return bombs_remaining;
	}
	
	public Passable get_start_point()
	{
		return start_point;
	}
	
	public void disarm_a_bomb(Bomb bomb_disarmed)
	{
		bombs_remaining.remove(bomb_disarmed);
		bombs_solved.add(bomb_disarmed);
	}
	
	public void add_new_path(ArrayList<Passable> path)
	{
		paths.add(path);
	}
	
	public ArrayList<ArrayList<Passable>> get_paths()
	{
		return paths;
	}
}
