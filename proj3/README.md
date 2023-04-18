# Build Your Own World Design Document

**Partner 1: Hungwon Choi**

**Partner 2: Minwoo Kim**

## Classes and Data Structures

## Algorithm

World generation:

Step A. Create empty Block[][] world.

Step B - 1. For every Block, make at most 4 Weithed Edges that connect a Block and its neigbors with random weight.

Step B - 2. Create Undirected Graph.

// creating rooms, walls, door, start point, hallways means change of type of block into those types.

Step C. Set StartPoint, # of rooms, locations of the rooms randomly. Then, create rooms with walls and doors. 

Step D - 1. For every door, using Dijkstra's Algorithm, find a path that connects the startpoint and a door. The returned pathes will be the hallways.

Step D - 2. Surround the hallways with walls.

Step E. Returning TETile[][] corresponded to Block's type.

Then, render the TETile[][]. 


## Persistence
