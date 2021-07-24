//
//  ChessBoard.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

// to match the typealias in the kotlin chess module
typealias Square = Int32

struct ChessBoardView: View {
    
    var playViewModel: PlayViewModel
    
    @ObservedObject var boardChanged: Collector<Int32>
    @ObservedObject var selectedSquare: Collector<Square?>
    @ObservedObject var possibleMoves: Collector<[Move]>
    
    @Namespace private var boardNameSpace
    
    init(playViewModel: PlayViewModel) {
        self.playViewModel = playViewModel
        boardChanged = playViewModel.boardUpdated.collectAsObservable(initialValue: playViewModel.boardUpdated.value as! Int32)
        selectedSquare = playViewModel.selectedSquare.collectAsObservable(initialValue: playViewModel.selectedSquare.value as? Square)
        possibleMoves = playViewModel.possibleMoves.collectAsObservable(initialValue: playViewModel.possibleMoves.value as! [Move])
    }
    
    var body: some View {
        
        let game = playViewModel.game
        let lastMove = game.board.movesHistory.lastObject as? Move
        
        let columns: [GridItem] = Array(
            repeating: .init(.flexible(), spacing: 0),
            count: Int(game.board.numberOfColumns)
        )
        
        let squares = game.board.squaresMap as! Dictionary<Int32, SquareOccupant>
        
        let positionsSorted = game.colorOnUserSideOfBoard == ChessPieceColor.white
            ? squares.sorted(by: {$0.0 < $1.0})
            : squares.sorted(by: {$0.0 > $1.0})
        
        LazyVGrid(columns: columns, spacing: 0) {
            ForEach(positionsSorted, id: \.key) { square, occupant in
                
                let squareColor = game.board.squareColor(square: square).colorResource()
                
                Button(action: {
                    withAnimation {
                        playViewModel.squareClicked(square: square)
                    }
                }) {
                    ZStack {
                        Rectangle().fill(Color(squareColor))
                        
                        if occupant is ChessPiece {
                            chessPieceView(for: occupant as! ChessPiece)
                        }
                        
                        if (possibleMoves.currentValue.map { $0.destination }.contains(square)) {
                            let color = (occupant is ChessPiece) ? Color.red : Color.gray
                            Circle().fill(color).padding().transition(AnyTransition.scale).zIndex(2)
                        }
                        
                        if (square == lastMove?.source) {
                            RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.gray, lineWidth: 1)
                        }

                        if (square == lastMove?.destination) {
                            RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.blue, lineWidth: 1)
                        }
                        
                        if ((square == Int32(game.board.blackKingPosition) && game.board.isOnCheck(color: .black)) ||
                                (square == Int32(game.board.whiteKingPosition) && game.board.isOnCheck(color: .white))
                        ) {
                            RoundedRectangle(cornerRadius: 0, style: .continuous).strokeBorder(Color.red, lineWidth: 2)
                        }

                    }
                    .frame(maxWidth: .infinity)
                    .aspectRatio(1, contentMode: .fit)
                }
            }
        }
    }
    
    private func chessPieceView(for chessPiece: ChessPiece) -> some View {
        Image(chessPiece.imageRes())
            .matchedGeometryEffect(id: chessPiece.id, in: boardNameSpace)
            .zIndex(1)
    }
}

struct ChessBoard_Previews: PreviewProvider {
    static var previews: some View {
        return ChessBoardView(playViewModel: PlayViewModel())
    }
}
