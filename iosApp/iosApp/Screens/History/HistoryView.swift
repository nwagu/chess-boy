//
//  HistoryView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

typealias SavedGame = GameHistory

struct HistoryView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    @State var games: [SavedGame] = []
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(title: "History")
            List {
                ForEach(games, id: \.self.gameId) { game in
                    itemView(of: game)
                        .onTapGesture {
                            withAnimation {
                                environment.gameAnalysisViewModel.savedGame = game
                                viewRouter.navigate(screen: .gameAnalysis)
                            }
                        }
                }
            }
            Spacer()
        }
        .onAppear {
            games = environment.getGamesHistory()
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .padding()
    }
    
    private func itemView(of game: SavedGame) -> some View {
        let whitePlayer = PGNKt.getHeaderValueFromPgn(name: PGNKt.PGN_HEADER_WHITE_PLAYER, pgn: game.pgn) ?? ""
        let blackPlayer = PGNKt.getHeaderValueFromPgn(name: PGNKt.PGN_HEADER_BLACK_PLAYER, pgn: game.pgn) ?? ""
        return Text("\(whitePlayer)(W) vs \(blackPlayer)(B)").padding()
    }
}
