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
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    @State private var showingDeleteGameActionSheet = false
    @State private var gameIdToDelete: Int64? = nil
    
    @State var games: [SavedGame] = []
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            List {
                ForEach(games, id: \.self.gameId) { game in
                    itemView(of: game)
                        .swipeActions(edge: .trailing) {
                                    Button {
                                        gameIdToDelete = game.id
                                        showingDeleteGameActionSheet = true
                                    } label: {
                                        Label("Delete", systemImage: "trash.fill")
                                    }
                                    .tint(.red)
                         
                                }
                }
            }
            Spacer()
        }
        .navigationBarTitle("History", displayMode: .inline)
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
        .actionSheet(isPresented: $showingDeleteGameActionSheet) {
            ActionSheet(title: Text("Confirm delete"), buttons: [
                .default(Text("Delete")) {
                    environment.mainViewModel.gamesHistoryRepository.deleteGame(id: gameIdToDelete ?? -1)
                    games = environment.getGamesHistory()
                },
                .cancel() {  }
            ])
        }
    }
    
    private func itemView(of game: SavedGame) -> some View {
        let whitePlayer = PGNKt.getHeaderValueFromPgn(name: PGNKt.PGN_HEADER_WHITE_PLAYER, pgn: game.pgn) ?? ""
        let blackPlayer = PGNKt.getHeaderValueFromPgn(name: PGNKt.PGN_HEADER_BLACK_PLAYER, pgn: game.pgn) ?? ""
        return NavigationLink(destination: GameAnalysisView(game: game)) {
            Text("\(whitePlayer)(W) vs \(blackPlayer)(B)").padding()
                .listRowSeparator(.hidden)
        }
    }
}
