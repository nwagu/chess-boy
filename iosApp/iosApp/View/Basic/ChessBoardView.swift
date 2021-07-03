//
//  ChessBoard.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

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
                                    Image(UtilsKt.getChessPieceImage(piece: occupant as! ChessPiece))
                                }
                            }
                        }
                    }
                }
            }
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
