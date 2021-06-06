package com.nwagu.chess.convention

import com.nwagu.chess.Game
import com.nwagu.chess.board.move
import java.util.regex.Pattern

private val headerPattern = Pattern.compile("\\[.* \".*\"\\]")
private val movePattern = Pattern.compile("(K|Q|R|B|N)?(a|b|c|d|e|f|g|h)?(1|2|3|4|5|6|7|8)?(x)?(a|b|c|d|e|f|g|h)(1|2|3|4|5|6|7|8)(=Q|=R|=B|=N)?(\\+|#)?([\\?\\!]*)?[\\s]*")
private val castlingPattern = Pattern.compile("(O\\-O|O\\-O\\-O)(\\+|#)?([\\?\\!]*)?")

const val PGN_HEADER_EVENT = "Event"
const val PGN_HEADER_SITE = "Site"
const val PGN_HEADER_DATE = "Date"
const val PGN_HEADER_ROUND = "Round"
const val PGN_HEADER_WHITE_PLAYER = "White"
const val PGN_HEADER_BLACK_PLAYER = "Black"
const val PGN_HEADER_BLACK_RESULT = "Result"

fun Game.exportPGN(): String {

    val sb = StringBuilder()

    // TODO Append other headers
    sb.append(buildHeader(PGN_HEADER_WHITE_PLAYER, whitePlayer.name))
    sb.append(buildHeader(PGN_HEADER_BLACK_PLAYER, blackPlayer.name))

    sb.append('\n')

    val historySan = board.getHistorySAN()

    historySan.forEachIndexed { index, san ->
        val moveNumber = (index / 2) + 1
        sb.append(san)
        sb.append(' ')
        if (index < historySan.size - 1 && index % 2 == 0 && index >= 2) {
            sb.append(moveNumber)
            sb.append(". ")
        }
    }

    // sb.append(getResult().getDescription())

    return sb.toString()
}

fun getHeaderValueFromPgn(name: String, pgn: String): String? {
    val lines = pgn.split("\n")
    for (line in lines) {
        if (headerPattern.matcher(line).matches()) {
            val header = parseHeader(line)
            if ((header.first) == name) {
                return header.second
            }
        }
    }
    return null
}

fun Game.importPGN(pgn: String) {

    board.loadStandardStartingPosition()

    val lines = pgn.split("\n")
    for (line in lines) {
        if (line.trim().isBlank()) {
            continue
        }
        else if (headerPattern.matcher(line).matches()) {
            val header = parseHeader(line)
            // TODO check the header name and add it to game
            println("PGN Header -> ${header.first}: ${header.second}")
        }
        else {
            // is 'moveslist'

            // remove all text between ( )
            var moves = line
            var newMoves = moves.replace("\\([^\\(\\)]*\\)", "")

            while (moves != newMoves) {
                moves = newMoves
                newMoves = moves.replace("\\([^\\(\\)]*\\)", "")
            }

            // remove all text between { }

            newMoves = moves.replace("\\{[^\\{\\}]*\\}", "")

            while (moves != newMoves) {
                moves = newMoves
                newMoves = moves.replace("\\{[^\\{\\}]*\\}", "")
            }

            // remove all text between [ ]

            newMoves = moves.replace("\\[[^\\[\\]]*\\]", "")

            while (moves != newMoves) {
                moves = newMoves
                newMoves = moves.replace("\\[[^\\[\\]]*\\]", "")
            }

            val sans = newMoves.split(" ").filter { !it.contains(".") && it.isNotBlank() }

            sans.forEach {
                if (movePattern.matcher(line).matches() || castlingPattern.matcher(line).matches()) {
                    board.move(board.sanToMove(it.trim()), false)
                }
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