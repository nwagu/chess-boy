//
//  ChessBoyEnvironment.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 20/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

class ChessBoyEnvironment: ObservableObject {
    
    let mainViewModel = MainViewModel()
    let playViewModel = PlayViewModel()
    
    @Published var playScreenUp: Bool
    
    init() {
        playScreenUp = false
        initialize()
    }
    
    func initialize() {
        if (!playViewModel.isGameInitialized()) {
            let game = mainViewModel.getLastGameOrDefault()
            playViewModel.doInit(game: game)
        }
    }
    
    func startNewGame(whitePlayer: Player, blackPlayer: Player) {
        let game = createNewGame(whitePlayer: whitePlayer, blackPlayer: blackPlayer)
        saveCurrentGame()
        playViewModel.doInit(game: game)
    }
    
    func saveCurrentGame() {
        mainViewModel.saveGame(game: playViewModel.game)
    }
    
    func getGamesHistory() -> [SavedGame] {
        mainViewModel.getGamesHistory()
    }
    
    func createNewGame(whitePlayer: Player, blackPlayer: Player) -> Game {
        Game(whitePlayer: whitePlayer, blackPlayer: blackPlayer)
    }
    
    func showPlayScreen() {
        playScreenUp = true
    }
    
    func hidePlayScreen() {
        playScreenUp = false
    }
    
}
