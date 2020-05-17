# AI-searching
An AI assignment written in Java that uses searching algorithems to calculate the optimal route for an agent to take to resolve the largest number of bombs in a game world.
<br>
After running the `main` method, please enter the text-based game world in the console.
<br>
Sample input:
<br>
![alt text](https://github.com/powerseed/Mission-Possible/blob/master/sample%20input.png "Sample input")
<br>
where:
<br>
The first line of input will be the number of rows and columns in the world. The world will always be surrounded by walls. The following symbols are used:

@: the initial position of the secret agent (there will only be one of these)
<br>
Terrain by movement cost:
<br>
: (space) cost = 1
<br>
.: cost = 2
<br>
:: cost = 3
<br>
!: cost = 4
<br>
$: cost = 5
<br>
#: wall (cannot move through)
<br>
bombs: A to Z where the timer is set to 10 * the ordinal value of the letter (A is 1, and therefore has timer value 10)
<br>
<br>
Sample output:
<br>
![alt text](https://github.com/powerseed/Mission-Possible/blob/master/sample%20output.png "Sample output")
<br>
Ready-made inputs could be found in `Ready-made Inputs` folder. 
