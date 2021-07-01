//
//  ChessBoard.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import chess

struct ChessBoardView: View {
    var board: Board
    
    var body: some View {
        VStack {
            
            ForEach(0..<Int(board.numberOfRows)) { row in
                
                HStack {
                    
                    ForEach(0..<Int(board.numberOfColumns)) { column in
                        
                        let square = board.square(row: Int32(row), column: Int32(column))
                        
                        let occupant = board.squaresMap[square]
                        
                        Button(action: {}) {
                            ZStack {
                                Rectangle().fill(Color.red)
                                
                                if (occupant as? ChessPiece) != nil {
                                    getImageForChessPiece(piece: occupant as! ChessPiece)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

func getImageForChessPiece(piece: ChessPiece) -> Image {
    if piece.chessPieceColor == ChessPieceColor.white {
        switch piece.chessPieceType {
            case ChessPieceType.queen:
                return Image("img_white_queen")
            case ChessPieceType.king:
                return Image("img_white_king")
            case ChessPieceType.knight:
                return Image("img_white_knight")
            case ChessPieceType.bishop:
                return Image("img_white_bishop")
            case ChessPieceType.rook:
                return Image("img_white_rook")
            default:
                return Image("img_white_pawn")
        }
    } else {
        switch piece.chessPieceType {
            case ChessPieceType.queen:
                return Image("img_black_queen")
            case ChessPieceType.king:
                return Image("img_black_king")
            case ChessPieceType.knight:
                return Image("img_black_knight")
            case ChessPieceType.bishop:
                return Image("img_black_bishop")
            case ChessPieceType.rook:
                return Image("img_black_rook")
            default:
                return Image("img_black_pawn")
        }
    }

}

struct ChessBoard_Previews: PreviewProvider {
    static var previews: some View {
        let board = Board(numberOfRows: 8, numberOfColumns: 8)
        board.loadStandardStartingPosition()
        return ChessBoardView(board: board)
    }
}
