package game.tetris.lib;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;

public class TileTest {

	@Test
	public void construction_and_get_set_tests() {
		Tile toTest = new Tile();
		assertFalse(toTest.isLit());
		
		toTest.setColor(Color.BLACK);
		assertFalse(toTest.isLit());
		toTest.setColor(Color.GREEN);
		assertTrue(toTest.isLit());
	}
	
	@Test
	public void numTilesLit_test() {
		Tile[] array = new Tile[3];
		for (int i = 0; i < 3; i++)
			array[i] = new Tile();
		
		assertEquals(Tile.numTilesLit(array), 0);
		
		array[2].setColor(Color.GREEN);
		assertEquals(Tile.numTilesLit(array), 1);
	}
	
	@Test
	public void row_to_string_test() {
		Tile[] row = new Tile[10];
		for (int i = 0; i < 10; i++) {
			row[i] = new Tile();
			row[i].setColor(Color.GREEN);
		}
		String stringRepOfRow = Tile.rowOfBoardToString(row);
		assertEquals(stringRepOfRow, "GGGGGGGGGG");
	}
	
	@Test
	public void string_to_tile_array_test() {
		String stringRepOfRow = "GGGGGGGGGG";
		Tile[] row = Tile.stringToRowOfBoard(stringRepOfRow);
		
		for (int i = 0; i < 10; i++) {
			assertTrue(row[i].getColor() == Color.GREEN);
		}
	}
}
