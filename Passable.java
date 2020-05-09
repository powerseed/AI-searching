public abstract class Passable extends Tile
{
	protected int movement_cost;				// the cost of this tile
	protected int times_passed;					// the times of this tile being passed by the player
	protected double distance_to_a_bomb;		
	protected Passable last_step;				// the previous tile passed by the player in a path
	protected int cost_so_far;
	
	public Passable(char character, int[] coordinate)
	{
		super(character, coordinate);
		
		if( character == ' ' || (character >= 65 && character <= 90) )
		{
			this.movement_cost = 1;
		}
		else if(character == '.')
		{
			this.movement_cost = 2;
		}
		else if(character == ':')
		{
			this.movement_cost = 3;
		}
		else if(character == '!')
		{
			this.movement_cost = 4;
		}
		else if(character == '$')
		{
			this.movement_cost = 5;
		}
		
		times_passed = 0;
		distance_to_a_bomb = -1;
		cost_so_far = 0;
	}
	
	public void set_cost_so_far(int cost_so_far)
	{
		this.cost_so_far = cost_so_far;
	}
	
	public int get_cost_so_far()
	{
		return cost_so_far;
	}
	
	public Passable get_last_step()
	{
		return last_step;
	}
	
	public void set_last_step(Passable last_step)
	{
		this.last_step = last_step;
	}
	
	public int get_movement_cost()
	{
		return movement_cost;
	}
	
	public void increment_times_passed()
	{
		times_passed++;	
	}
	
	public double get_distance_to_a_bomb()
	{
		return distance_to_a_bomb;
	}
	
	public void set_distance_to_a_bomb(double distance_to_a_bomb)
	{
		this.distance_to_a_bomb = distance_to_a_bomb;
	}
	
	public double get_fn()
	{
		return cost_so_far + distance_to_a_bomb + this.movement_cost;
	}
	
	public String toString()
	{
		if(times_passed != 0)
		{
			char i = (char) (48 + times_passed);
			return i + "";
		}
		else
		{
			return character + "";
		}
	}
}