public class Bomb extends Passable
{
	private int time_explode;	// how much time before the bomb explodes.
	
	public Bomb(char character, int[] coordinate)
	{
		super(character, coordinate);
		this.time_explode = (character - 64) * 10;
	}
	
	public int get_time_explode()
	{
		return time_explode;
	}
}
