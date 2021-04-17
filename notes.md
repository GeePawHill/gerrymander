Link List of coords.

Placement A link A set of dead links. A set of backtrack links.

We know in advance that there are P placements in a tiling, where P = (W*H)/O (which must be an integer). Example:
pentominos in a 5x5 is 5 placements. trominos in a 6x6 is 12 placements.

While the solution doesn't have enough placements and links isn't empty Pick a link

	Remove each link with the same constituents and put it in dead links. 
		These are dead links, and will not come back. Either this tiling works, and doesn't come back, or we'll hit a dead end, and it means this placement isn't valid given the prior placements.
	
	Remove each link with *any* of the constituents and put it in backtrack links. 
		There are backtrackable links, and may come back. If we backtrack, these are still valid links, because they're different from the one we chose.

If we exit the loop because we have enough placements, we have a solution If we run out of links, we have an untilable
plane with the starting set of links. (Is that even possible?)



