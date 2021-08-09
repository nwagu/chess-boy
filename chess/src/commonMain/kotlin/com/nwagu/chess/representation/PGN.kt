package com.nwagu.chess.representation

import com.nwagu.chess.model.Game
import com.nwagu.chess.model.Board
import com.nwagu.chess.gamelogic.move

private val headerPattern = Regex("""\[.* ".*"]""")
private val movePattern = Regex("""([KQRBN])?([abcdefgh])?([12345678])?(x)?([abcdefgh])([12345678])(=Q|=R|=B|=N)?([+#])?([?!]*)?[\\s]*""")
private val castlingPattern = Regex("""(O-O|O-O-O)([+#])?([?!]*)?""")

const val PGN_HEADER_EVENT = "Event"
const val PGN_HEADER_SITE = "Site"
const val PGN_HEADER_DATE = "Date"
const val PGN_HEADER_ROUND = "Round"
const val PGN_HEADER_WHITE_PLAYER = "White"
const val PGN_HEADER_BLACK_PLAYER = "Black"
const val PGN_HEADER_GAME_ID = "GameID"
const val PGN_HEADER_WHITE_PLAYER_ID = "WhiteID"
const val PGN_HEADER_BLACK_PLAYER_ID = "BlackID"
const val PGN_HEADER_RESULT = "Result"

fun Game.exportPGN(includeIds: Boolean = true): String {

    val sb = StringBuilder()

    // TODO Append other headers
    sb.append(buildHeader(PGN_HEADER_WHITE_PLAYER, whitePlayer.name))
    sb.append(buildHeader(PGN_HEADER_BLACK_PLAYER, blackPlayer.name))

    if (includeIds) {
        sb.append(buildHeader(PGN_HEADER_GAME_ID, id))
        sb.append(buildHeader(PGN_HEADER_WHITE_PLAYER_ID, whitePlayer.id))
        sb.append(buildHeader(PGN_HEADER_BLACK_PLAYER_ID, blackPlayer.id))
    }

    sb.append('\n')

    val historySan = board.getHistorySAN()

    historySan.forEachIndexed { index, san ->
        val fullMovesNumber = (index / 2) + 1
        if (index % 2 == 0) {
            sb.append(fullMovesNumber)
            sb.append(". ")
        }
        sb.append(san)
        sb.append(' ')
    }

    return sb.toString()
}

fun getHeaderValueFromPgn(name: String, pgn: String): String? {
    val lines = pgn.split("\n")
    for (line in lines) {
        if (headerPattern.matches(line)) {
            val header = parseHeader(line)
            if ((header.first) == name) {
                return header.second
            }
        }
    }
    return null
}

fun Board.importMovesFromPGN(pgn: String) {

    clear()
    loadStandardStartingPosition()

    val lines = pgn.split("\n")
    for (line in lines) {
        if (line.trim().isBlank()) {
            continue
        }
        else if (headerPattern.matches(line)) {
            val header = parseHeader(line)
            // TODO check the header name and add it to game
        }
        else {
            // is 'moveslist'

            // remove all text between ( )
            var moves = line
            var _moves = moves.replace("\\([^\\(\\)]*\\)", "")

            while (moves != _moves) {
                moves = _moves
                _moves = moves.replace("\\([^\\(\\)]*\\)", "")
            }

            // remove all text between { }

            _moves = moves.replace("\\{[^\\{\\}]*\\}", "")

            while (moves != _moves) {
                moves = _moves
                _moves = moves.replace("\\{[^\\{\\}]*\\}", "")
            }

            // remove all text between [ ]

            _moves = moves.replace("\\[[^\\[\\]]*\\]", "")

            while (moves != _moves) {
                moves = _moves
                _moves = moves.replace("\\[[^\\[\\]]*\\]", "")
            }

            val sans = _moves.split(" ").filter { !it.contains(".") && it.isNotBlank() }

            sans.forEach {
                move(sanToMove(it.trim()), false)
            }

        }
    }
}

private fun buildHeader(name: String, value: String): String {
    return "[$name \"$value\"]\n"
}

private fun parseHeader(header: String): Pair<String, String> {

    val trimmedHeader = header.removePrefix("[").removeSuffix("]")

    return Pair(
        trimmedHeader.substringBefore(" \""),
        trimmedHeader.substringAfter(" \"").removeSuffix("\"")
    )

}