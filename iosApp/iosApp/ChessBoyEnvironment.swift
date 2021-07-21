//
//  ChessBoyEnvironment.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 20/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

class ChessBoyEnvironment: ObservableObject {
    
    let playViewModel = PlayViewModel()
    
    func startNewGame(whitePlayer: Player, blackPlayer: Player) {
        let game = createNewGame(whitePlayer: whitePlayer, blackPlayer: blackPlayer)
//        saveGame(playViewModel.game)
        playViewModel.doInit(game: game)
    }
    
    func createNewGame(whitePlayer: Player, blackPlayer: Player) -> Game {

//        if (whitePlayer is BluetoothPlayer || blackPlayer is BluetoothPlayer) {
//            throw Error
//        }

        return Game(
            id: "hiukb",
            whitePlayer: whitePlayer,
            blackPlayer: blackPlayer
        )
    }
    
}
