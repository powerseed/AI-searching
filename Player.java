public class Player extends Passable
{
	protected char character;
	protected int[] coordinate;
	
	public Player(int[] coordinate)
	{
		super('@', coordinate);
	}
}
