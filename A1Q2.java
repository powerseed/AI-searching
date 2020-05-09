import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class A1Q2 
{
	public static void main(String[] args) throws IOException 
	{
		// Receive user input as the game board.
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the game board: ");
		
		String dimension = "";	
		dimension = scanner.nextLine();
		String[] split = dimension.split(",");
		
		// deal with the input and generate the game board, including getting the locations of the player and all bombs. 
		int number_rows = Integer.parseInt(split[0]);	
		int number_columns = Integer.parseInt(split[1]); 
		Tile[][] game_board = new Tile[number_rows][number_columns];
		ArrayList<Bomb> bombs = new ArrayList<Bomb>();
		
		Player player = null;
		
		// create corresponding object and populate the game board.
		for(int y = 0; y < number_rows; y++)
		{
			String line = scanner.nextLine();
			
			for(int x = 0; x < number_columns; x++)
			{
				int[] coordinate = {x, y};
				char character = line.charAt(x);

				if(character == '#')
				{
					game_board[y][x] = new Wall(coordinate);
				}
				else if( character >= 65 && character <= 90 )
				{
					Bomb bomb = new Bomb(character, coordinate); 
					game_board[y][x] = bomb;
					bombs.add(bomb);
				}
				else if(character == '@')
				{
					Player new_player = new Player(coordinate);
					game_board[y][x] = new_player;
					player = new_player;
				}
				else
				{
					game_board[y][x] = new Non_bomb(character, coordinate);
				}
			}
		}
		
		// print the solution
		System.out.println("The initial world: ");
		for(int y = 0; y < number_rows; y++)
		{
			for(int x = 0; x < number_columns; x++)
			{
				System.out.print(game_board[y][x].toString());
			}
			System.out.println();		
		}

		
		Queue<Move> open = new LinkedList<Move>();
		
		// PQ to store the solutions that don't disarm all bombs.
		PriorityQueue<Move> list_of_results = new PriorityQueue<Move>(new Move_comparator());
		
		// the very first step we take in this game.
		Move start_point = new Move(bombs, new ArrayList<Bomb>(), player, 0);		
		open.add(start_point);
		
		int states_examined = 0;

		// Use BFS to try all orders of disarming bombs.
		// for example, if there are 3 bombs, the orders will be 123, 132, 213, 231, 312, 321.
		// then we pick the best order, which leads to the largest number of bombs disarmed.
		while(!open.isEmpty())
		{
			Move current_move = open.poll();
			
			ArrayList<Bomb> bombs_remaining = current_move.get_bombs_remaining();
			ArrayList<Bomb> bombs_solved = current_move.get_bombs_solved();
			int time_consumed_overall = current_move.get_time_consumed_overall();
			
			// if there are still bombs remaining.
			if(bombs_remaining.size() > 0)
			{
				// we generate a sub-move for each of the remaining bombs.
				for(int i = 0; i < bombs_remaining.size(); i++)
				{
					// check if there is a path to solve this bomb.
					Path path = disarm(game_board, current_move.get_start_point(), bombs_remaining.get(i), time_consumed_overall);
					states_examined += path.get_states_examined();
					
					// if there is a path that disarm this bomb successfully.
					if(path.get_is_disarmed())
					{
						int time_consumed_for_this_bomb = path.get_time_consumed_for_one_bomb();	
						
						// we do remove and adding this bomb for next move
						ArrayList<Bomb> bomb_remaining_for_next_move = (ArrayList<Bomb>) bombs_remaining.clone();
						bomb_remaining_for_next_move.remove(i);
						
						ArrayList<Bomb> bomb_solved_for_next_move = (ArrayList<Bomb>)bombs_solved.clone();
						bomb_solved_for_next_move.add(bombs_remaining.get(i));
										
						// create the next move, add the existing paths into it.
						Move next_move = new Move(bomb_remaining_for_next_move, bomb_solved_for_next_move, bombs_remaining.get(i), 
													time_consumed_overall + time_consumed_for_this_bomb);
						
						for(int j = 0; j < current_move.get_paths().size(); j++)
						{
							next_move.add_new_path(current_move.get_paths().get(j));
						}
						
						next_move.add_new_path(path.get_path());
						
						// add next move to open.
						open.add(next_move);
					}
					// since there is a bomb can not be disarmed by this move, we add it into the list of unperfect results
					else
					{
						list_of_results.add(current_move);
					}
				}
			}
			else	// all bombs have been disarmed, it is a perfect_result, and we can stop searching.
			{
				list_of_results.add(current_move);
			}
		}
		
		// get the best move
		ArrayList<ArrayList<Passable>> best_paths = null;
		Move best_move = null;
		best_move = list_of_results.poll();
		best_paths = best_move.get_paths();
		
		// For each tile, use the times of passed to substitute its character.
		for(int i = 0; i < best_paths.size(); i++)
		{
			ArrayList<Passable> path = best_paths.get(i);
			for(int j = 0; j < path.size(); j++)
			{
				path.get(j).increment_times_passed();
			}
		}
			
		// print the solution
		System.out.println("The solution: ");
		
		for(int y = 0; y < number_rows; y++)
		{
			for(int x = 0; x < number_columns; x++)
			{
				System.out.print(game_board[y][x].toString());
			}
			System.out.println();		
		}
		
		System.out.println("bombs disarmed: " + best_move.get_bombs_solved_number());
		System.out.println("bombs exploded: " + (bombs.size() - best_move.get_bombs_solved_number()));
		System.out.println("cost of plan: " + best_move.get_time_consumed_overall());
		System.out.println("states examined: " + states_examined);
	}
	
	private static Path disarm(Tile[][] game_board, Tile player, Bomb bomb, int time_already_consumed)
	{		
		PriorityQueue<Passable> open = new PriorityQueue<Passable>(new Tile_comparator());
		ArrayList<Passable> close = new ArrayList<Passable>();
		
		// start with the current location of the player.
		open.add((Passable) player);
		
		boolean found_bomb = false;
		int time_consumed_for_this_bomb = 0;
		
		// we check all 8 adjacent tiles of every tile to find a path to the bomb.
		// among all of them, we pick the one with the shortest distance to the bomb as our next node.
		while(!open.isEmpty() && !found_bomb)
		{
			Passable passable = open.poll();
			close.add(passable);
			
			// found the goal
			if(passable == bomb)		
			{
				found_bomb = true;
			}
			else
			{				
				// get the coordinate of this tile
				int x = passable.get_coordinate()[0];
				int y = passable.get_coordinate()[1];
							
				// get all 8 adjacent tiles and insert them into an ArrayList
				ArrayList<Tile> all_8_tiles = new ArrayList<Tile>();
				
				Tile left_up = game_board[y-1][x-1];
				Tile middle_up = game_board[y-1][x];
				Tile right_up = game_board[y-1][x+1];
				Tile left_middle = game_board[y][x-1];
				Tile right_middle = game_board[y][x+1];
				Tile left_bottom = game_board[y+1][x-1];
				Tile middle_bottom = game_board[y+1][x];
				Tile right_bottom = game_board[y+1][x+1];
				
				all_8_tiles.add(left_up);
				all_8_tiles.add(middle_up);
				all_8_tiles.add(right_up);
				all_8_tiles.add(left_middle);
				all_8_tiles.add(right_middle);
				all_8_tiles.add(left_bottom);
				all_8_tiles.add(middle_bottom);
				all_8_tiles.add(right_bottom);
				
				// remove walls from the 8 adjacent tiles.
				int index = 0;
				while(index < all_8_tiles.size())
				{
					Tile tile = all_8_tiles.get(index);
					
					if(tile instanceof Wall)
					{
						all_8_tiles.remove(index);
						index--;
					}
					
					index++;
				}
				
				int cost_so_far = passable.get_cost_so_far() + passable.get_movement_cost();

				// push the 8 adjacent tiles(those not wall) into open
				for(int i = 0; i < all_8_tiles.size(); i++)
				{  
					Passable tile = (Passable)all_8_tiles.get(i);
					double new_distance = calcualte_distance(tile, bomb);
					
					if(!close.contains(tile))
					{						
						if(open.contains(tile))		// not in close but in open
						{						
							// update its cost_so_far variable if we found a better path getting to this tile.
							if(cost_so_far < ((Passable) tile).get_cost_so_far())
							{
								tile.set_cost_so_far(cost_so_far);
								tile.set_last_step(passable);
							}
						}
						else						// not in close nor in open, this is a new state
						{
							tile.set_last_step(passable);
							tile.set_cost_so_far(cost_so_far);
							tile.set_distance_to_a_bomb(new_distance);
							open.add((Passable)tile);
						}
					}
				}
			}
		}// while	
		
		// back track from the last tile to the start point to form a path.
		ArrayList<Passable> path = new ArrayList<Passable>();
		
		Passable last_step = close.get(close.size() - 1);
		
		while( last_step != close.get(0) )
		{
			path.add(last_step);
			time_consumed_for_this_bomb += last_step.get_movement_cost();
			last_step = last_step.get_last_step();
		}		
				
		// if we found a path that disarms a bomb successfully, we return a Path object with boolean value of true.
		// otherwise we return that with a value of false.
		if( found_bomb )
		{
			if( time_already_consumed + time_consumed_for_this_bomb <= bomb.get_time_explode() )	
			{
				return new Path(path, time_consumed_for_this_bomb, close.size(), true);
			}
			else
			{
				return new Path(null, 0, close.size(), false);
			}
		}
		else
		{
			return new Path(null, 0, close.size(), false);
		}	
	}
	
	// calculate the distance between 2 tiles.
	private static double calcualte_distance(Tile tile1, Tile tile2)
	{
		return Math.sqrt(Math.pow(tile1.get_coordinate()[0] - tile2.get_coordinate()[0], 2) + Math.pow(tile1.get_coordinate()[1] - tile2.get_coordinate()[1], 2));
	}
}

// To put all tiles in priority queue in ascending order of f(n).
class Tile_comparator implements Comparator<Passable>
{
	public int compare(Passable passable1, Passable passable2) 
	{
		if( passable1.get_fn() < passable2.get_fn() )
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}	
}

// To put all moves in priority queue in descending order of the number of disarmed bombs.
class Move_comparator implements Comparator<Move>
{
	public int compare(Move move1, Move move2) 
	{
		if( move1.get_bombs_solved_number() < move2.get_bombs_solved_number() )
		{
			return 1;
		}
		// if 2 moves have the same number of disarmed bombs, then we want the one with the smaller "cost of plan" to be place ahead the other.
		else if(move1.get_bombs_solved_number() == move2.get_bombs_solved_number())
		{
			if(move1.get_time_consumed_overall() > move2.get_time_consumed_overall())
			{
				return 1;
			}
			else 
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}	
}
