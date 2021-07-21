//
//  PlayerDisplay.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 05/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct PlayerDisplay: View {
    var playViewModel: PlayViewModel
    var color: ChessPieceColor
    
    @ObservedObject
    var boardChanged: Collector<Int32>
    
    init(playViewModel: PlayViewModel, color: ChessPieceColor) {
        self.playViewModel = playViewModel
        self.color = color
        boardChanged = playViewModel.boardUpdated.collectAsObservable(initialValue: playViewModel.boardUpdated.value as! Int32)
    }
    
    var body: some View {
        let player = (color == ChessPieceColor.black)
            ? playViewModel.game.blackPlayer as! GUIPlayer
            : playViewModel.game.whitePlayer as! GUIPlayer
        let isTurn = playViewModel.game.board.turn == color
        HStack {
            ZStack(alignment: .topTrailing) {
                Image(player.avatar)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 25, height: 25)
                if (isTurn) {
                    Circle().fill(Color.green).frame(width:12)
                }
            }
            if (isTurn) {
                Text(playViewModel.game.board.checkMessageForNextPlayer())
                    .foregroundColor(Color.red)
            }
            Spacer()
        }
        .onTapGesture {
            playViewModel.getNextMove()
        }
    }
}

struct PlayerDisplay_Previews: PreviewProvider {
    static var previews: some View {
        PlayerDisplay(playViewModel: PlayViewModel(), color: .white)
    }
}
