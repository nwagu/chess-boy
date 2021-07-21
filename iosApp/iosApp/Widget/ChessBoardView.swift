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
    
    var playViewModel: PlayViewModel
    @ObservedObject
    var boardChanged: Collector<Int32>
    @ObservedObject
    var selectedSquare: Collector<Square?>
    @ObservedObject
    var possibleMoves: Collector<[Move]>
    
    init(playViewModel: PlayViewModel) {
        self.playViewModel = playViewModel
        boardChanged = playViewModel.boardUpdated.collectAsObservable(initialValue: playViewModel.boardUpdated.value as! Int32)
        selectedSquare = playViewModel.selectedSquare.collectAsObservable(initialValue: playViewModel.selectedSquare.value as? Square)
        possibleMoves = playViewModel.possibleMoves.collectAsObservable(initialValue: playViewModel.possibleMoves.value as! [Move])
    }
    
    var body: some View {
        
        let game = playViewModel.game
        let lastMove = game.board.movesHistory.lastObject
        
        let columns: [GridItem] = Array(
            repeating: .init(.flexible(), spacing: 0),
            count: Int(game.board.numberOfColumns)
        )
        
        LazyVGrid(columns: columns, spacing: 0) {
            ForEach(0..<game.board.squaresMap.count) { item in
                
                let square = (game.colorOnUserSideOfBoard == ChessPieceColor.white) ?
                    Int32(item) : (game.board.numberOfColumns * game.board.numberOfRows) - (Int32(item) + 1)
                
                let squareColor = game.board.squareColor(square: square).colorResource()
                let occupant = game.board.getSquareOccupantOrNull(square: square)
                
                Button(action: {
                    let move = playViewModel.squareClicked(square: square)

                    if (move is Promotion) {
                        // TODO Handle promotion piece selection
                    }
                }) {
                    ZStack {
                        Rectangle()
                            .fill(Color(squareColor))
                        
                        if let chessPiece = occupant {
                            Image(chessPiece.imageRes())
                        }
                        
                        if (possibleMoves.currentValue.map { $0.destination }.contains(square)) {
                            let color = (occupant != nil) ? Color.red : Color.gray
                            Circle().fill(color).padding()
                        }
                        
//                        if (square == lastMove?.source) {
//                            RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.gray, lineWidth: 1)
//                        }
//                        
//                        if (square == lastMove?.destination) {
//                            RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.blue, lineWidth: 1)
//                        }

                    }
                    .frame(maxWidth: .infinity)
                    .aspectRatio(1, contentMode: .fit)
                }
            }
        }
    }
}

struct ChessBoard_Previews: PreviewProvider {
    static var previews: some View {
        return ChessBoardView(playViewModel: PlayViewModel())
    }
}
