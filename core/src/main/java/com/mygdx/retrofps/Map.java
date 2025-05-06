package com.mygdx.retrofps;

public class Map {
	public static final int MAP_WIDTH = 10;
	public static final int MAP_HEIGHT = 10;
	
	public int[][] map = {
		{1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,0,0,1,0,0,1},
        {1,0,1,0,0,0,1,0,0,1},
        {1,0,0,0,1,0,0,0,0,1},
        {1,0,0,0,0,0,0,1,0,1},
        {1,0,1,0,0,1,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1}
	};
	
	public int get(int x, int y) {
		return map[y][x];
	}
}
