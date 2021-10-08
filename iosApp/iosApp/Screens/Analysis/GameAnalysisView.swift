//
//  GameAnalysisView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/08/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

struct GameAnalysisView: View {    
    private let gameAnalysisViewModel: GameAnalysisViewModel
    
    @ObservedObject var boardChanged: Collector<Int32>
    
    init(game: SavedGame) {
        self.gameAnalysisViewModel = GameAnalysisViewModel().apply {viewModel in
            viewModel.savedGame = game
        }
        boardChanged = gameAnalysisViewModel.boardUpdated.collectAsObservable(initialValue: gameAnalysisViewModel.boardUpdated.value as! Int32)
    }
    
    var navActions: [ViewAction] {
        [
            ViewAction(
                displayName: "First",
                action: { gameAnalysisViewModel.first() }
            ),
            ViewAction(
                displayName: "Prev",
                action: { gameAnalysisViewModel.previous() }
            ),
            ViewAction(
                displayName: "Next",
                action: { gameAnalysisViewModel.next() }
            ),
            ViewAction(
                displayName: "Last",
                action: { gameAnalysisViewModel.last() }
            )
        ]
    }
    
    var otherActions: [ViewAction] {
        [
            ViewAction(
                displayName: "Share",
                action: {  }
            ),
            ViewAction(
                displayName: "Delete",
                action: {  }
            )
        ]
    }
    
    var body: some View {
        
        let lastMoveIndex = gameAnalysisViewModel.lastMoveIndex
        
        VStack(alignment: .leading, spacing: 0) {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Text("\(gameAnalysisViewModel.game.whitePlayer.name) vs \(gameAnalysisViewModel.game.blackPlayer.name)")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.orange)
                        .padding(.top)
                    ChessBoardStaticView(board: gameAnalysisViewModel.game.board, boardChanged: boardChanged.currentValue)
                    Text("Moves")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.orange)
                        .padding(.top)
                    WrappingHStack(models: navActions) { navAction in
                        Button(action: { navAction.action() }, label: {
                            Text(navAction.displayName)
                        })
                    }
                    WrappingHStack(models: gameAnalysisViewModel.sans.enumerated().map { index, text in
                        IdentifiableSan(index: Int32(index), san: text, isCurrent: index == lastMoveIndex)
                    }) { san in
                        Text(san.san)
                            .background(san.isCurrent ? Color.green : Color.clear)
                            .onTapGesture {
                                gameAnalysisViewModel.jumpToMove(index: san.index)
                            }
                    }
                    WrappingHStack(models: otherActions) { otherAction in
                        Button(action: { otherAction.action() }, label: {
                            Text(otherAction.displayName)
                        })
                    }
                }
                Spacer()
            }
            
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .padding()
        .navigationBarTitle("Game review", displayMode: .inline)
    }
}

struct IdentifiableSan: Identifiable {
    var id: Int32 {index}
    let index: Int32
    let san: String
    let isCurrent: Bool
}

//struct GameAnalysisView_Previews: PreviewProvider {
//    static var previews: some View {
//        GameAnalysisView(game: SavedGame())
//    }
//}
