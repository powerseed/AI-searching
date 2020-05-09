# Mission-Possible
This is a school assignment of AI, which calculates the optimal routine for an agent to take to resolve the largest number of bombs in a game world.
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

* @: the initial position of the secret agent (there will only be one of these)
<br>
terrain by movement cost:
* : (space) cost = 1
* .: cost = 2
* :: cost = 3
* !: cost = 4
* $: cost = 5
* #: wall (cannot move through)
* bombs: A to Z where the timer is set to 10 * the ordinal value of the letter (A is 1, and therefore has timer value 10)
<br>
<br>
Sample output:
<br>
![alt text](https://github.com/powerseed/Mission-Possible/blob/master/sample%20output.png "Sample output")
<br>
Ready-made inputs could be found in `Ready-made Input.txt`
