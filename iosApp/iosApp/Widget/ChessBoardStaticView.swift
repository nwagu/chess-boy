//
//  ChessBoardStaticView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/08/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

struct ChessBoardStaticView: View {
    var board: Board
    var boardChanged: Int32
    
    var body: some View {
        
        let lastMove = board.movesHistory.lastObject as? Move
        
        let columns: [GridItem] = Array(
            repeating: .init(.flexible(), spacing: 0),
            count: Int(board.numberOfColumns)
        )
        
        let squares = board.squaresMap as! Dictionary<Int32, SquareOccupant>
        let positions = squares.sorted(by: {$0.0 < $1.0})
        
        return LazyVGrid(columns: columns, spacing: 0) {
            ForEach(positions, id: \.key) { square, occupant in
                
                let squareColor = board.squareColor(square: square).colorResource()
                
                ZStack {
                    Rectangle().fill(Color(squareColor))
                    
                    if occupant is ChessPiece {
                        chessPieceView(for: occupant as! ChessPiece)
                    }
                    
                    if (square == lastMove?.source) {
                        RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.gray, lineWidth: 1)
                    }

                    if (square == lastMove?.destination) {
                        RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.blue, lineWidth: 1)
                    }
                    
                    if ((square == Int32(board.blackKingPosition) && board.isOnCheck(color: .black)) ||
                            (square == Int32(board.whiteKingPosition) && board.isOnCheck(color: .white))
                    ) {
                        RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.red, lineWidth: 2)
                    }

                }
                .frame(maxWidth: .infinity)
                .aspectRatio(1, contentMode: .fit)
            }
        }
    }
    
    private func chessPieceView(for chessPiece: ChessPiece) -> some View {
        Image(chessPiece.imageRes())
    }
}

struct ChessBoardStaticView_Previews: PreviewProvider {
    static var previews: some View {
        ChessBoardStaticView(board: Board(numberOfRows: 8, numberOfColumns: 8), boardChanged: 0)
    }
}
