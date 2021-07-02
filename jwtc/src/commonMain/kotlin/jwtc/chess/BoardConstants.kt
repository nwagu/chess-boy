package jwtc.chess

object BoardConstants {

	const val PLAY = 1
	const val CHECK = 2
	const val INVALID = 3 // only occurs when king can be hit or when invalid number of pieces is on the board (more than one king). can be used when setting up a new position
	const val DRAW_MATERIAL = 4 // no one can win (ie KING against KING)
	const val DRAW_50 = 5 // after 25 full moves no hit or pawnmove occured
	const val MATE = 6
	const val STALEMATE = 7
	const val DRAW_REPEAT = 8 // draw by repetition (3 times same board position)
	
	// array index of data members that hold data for either black or white. these must be 0 and 1 cause arrays are of length 2
	const val BLACK = 0
	const val WHITE = 1
	
	// index and representation of the pieces array and values - must be from [0-5].
	const val PAWN = 0
	const val KNIGHT = 1
	const val BISHOP = 2
	const val ROOK = 3
	const val QUEEN = 4
	const val KING = 5
	// not a piece: a field
	const val FIELD = -1
	
	// not consequently used - 64 fields on a chess board, 6 pieces
	const val NUM_FIELDS = 64
	const val NUM_PIECES = 6
	const val MAX_MOVES = 255 // maximum number of moves possible from a position - not yet above 218 found documented
	
	// all bits set to one for the rows (index is row)
	val ROW_BITS = arrayOf(255L, 65280L, 16711680L, 4278190080L, 1095216660480L, 280375465082880L, 71776119061217280L, -72057594037927936L)
	// same as ROW_BITS but for the files
	val FILE_BITS = arrayOf(72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L)
											  
	// "enumeration" integer position values
	const val a8 = 0; const val b8 = 1; const val c8 = 2; const val d8 = 3; const val e8 = 4; const val f8 = 5; const val g8 = 6; const val h8 = 7
	const val a7 = 8; const val b7 = 9; const val c7 = 10; const val d7 = 11; const val e7 = 12; const val f7 = 13; const val g7 = 14; const val h7 = 15
	const val a6 = 16; const val b6 = 17; const val c6 = 18; const val d6 = 19; const val e6 = 20; const val f6 = 21; const val g6 = 22; const val h6 = 23
	const val a5 = 24; const val b5 = 25; const val c5 = 26; const val d5 = 27; const val e5 = 28; const val f5 = 29; const val g5 = 30; const val h5 = 31
	const val a4 = 32; const val b4 = 33; const val c4 = 34; const val d4 = 35; const val e4 = 36; const val f4 = 37; const val g4 = 38; const val h4 = 39
	const val a3 = 40; const val b3 = 41; const val c3 = 42; const val d3 = 43; const val e3 = 44; const val f3 = 45; const val g3 = 46; const val h3 = 47
	const val a2 = 48; const val b2 = 49; const val c2 = 50; const val d2 = 51; const val e2 = 52; const val f2 = 53; const val g2 = 54; const val h2 = 55
	const val a1 = 56; const val b1 = 57; const val c1 = 58; const val d1 = 59; const val e1 = 60; const val f1 = 61; const val g1 = 62; const val h1 = 63

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// instead of calling a method row(pos), position is index on array for the rows
	val ROW = arrayOf(
			0, 0, 0, 0, 0, 0, 0, 0,
			 1, 1, 1, 1, 1, 1, 1, 1, 
			 2, 2, 2, 2, 2, 2, 2, 2,
			 3, 3, 3, 3, 3, 3, 3, 3, 
			 4, 4, 4, 4, 4, 4, 4, 4, 
			 5, 5, 5, 5, 5, 5, 5, 5, 
			 6, 6, 6, 6, 6, 6, 6, 6, 
			 7, 7, 7, 7, 7, 7, 7, 7)

	// as with ROW, but for column
	val COL = arrayOf(
			0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7,
		 0, 1, 2, 3, 4, 5, 6, 7)

	// the row or rank seen from the oposite colors is mirrored, so also convenient use from an array
	// first index color, second index position
	val ROW_TURN = arrayOf(
		arrayOf(0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 3, 3, 3,
			4, 4, 4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6, 6, 6,
			7, 7, 7, 7, 7, 7, 7, 7)
			,
		arrayOf(7, 7, 7, 7, 7, 7, 7, 7,
			6, 6, 6, 6, 6, 6, 6, 6,
			5, 5, 5, 5, 5, 5, 5, 5,
			4, 4, 4, 4, 4, 4, 4, 4,
			3, 3, 3, 3, 3, 3, 3, 3,
			2, 2, 2, 2, 2, 2, 2, 2,
			1, 1, 1, 1, 1, 1, 1, 1,
			0, 0, 0, 0, 0, 0, 0, 0)
	)
	
}
