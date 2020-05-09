public abstract class Tile 
{
	protected char character;
	protected int[] coordinate;
	
	public Tile(char character, int[] coordinate)
	{
		this.character = character;
		this.coordinate = coordinate;
	}
	
	public char get_character()
	{
		return character;
	}
	
	public int[] get_coordinate()
	{
		return coordinate;
	}
	
	public String toString()
	{
		return "" + character;
	}
}
