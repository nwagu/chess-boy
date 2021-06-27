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
                        
                        
//                        var square = board.square(row: Int32(row), column: Int32(column))
                        
                        let occupant = board.squaresMap[4]
                        
//                        Text("\(String(occupant))")
                        
                        if (occupant as? ChessPiece) != nil {
                            ChessPieceView(piece: occupant as! ChessPiece)
                        }
                        
                    }
                }
                
            }
            
        }
        .frame(
              minWidth: 0,
              maxWidth: .infinity,
              minHeight: 0,
              maxHeight: .infinity,
              alignment: .topLeading
            )
    }
}

struct ChessPieceView: View {
    let piece: ChessPiece
    
    var body: some View {
        Button(action: {}) {
            Text("\(piece.chessPieceType.fenSymbol)")
        }
    }
}

struct ChessBoard_Previews: PreviewProvider {
    static var previews: some View {
        let fff = Board(numberOfRows: 8, numberOfColumns: 8)
        fff.loadStandardStartingPosition()
        return ChessBoardView(board: fff)
    }
}
