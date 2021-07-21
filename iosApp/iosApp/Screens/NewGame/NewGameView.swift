//
//  NewGameView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct NewGameView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    let newGameViewModel: NewGameViewModel = NewGameViewModel()
    @ObservedObject var selectedColor: Collector<ChessPieceColor?>
    @ObservedObject var selectedOpponent: Collector<MoveGenerator?>
    
    init() {
        selectedColor = newGameViewModel.selectedColor.collectAsObservable(initialValue: newGameViewModel.selectedColor.value as? ChessPieceColor)
        selectedOpponent = newGameViewModel.selectedOpponent.collectAsObservable(initialValue: newGameViewModel.selectedOpponent.value as? MoveGenerator)
    }
    
    var options: [SideChoice] {
        [
            SideChoice(displayName: "Random", color: nil),
            SideChoice(displayName: "White", color: .white),
            SideChoice(displayName: "Black", color: .black)
        ]
    }
    
    var body: some View {
        VStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    TopBar(title: "Start a new game against computer")
                    Text("Choose your side")
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.black)
                        .padding(.vertical)
                    
                    WrappingHStack(models: options) { option in
                        RadioCard(
                            text: option.displayName,
                            isSelected: (option.color == selectedColor.currentValue)
                        ) {
                            newGameViewModel.selectedColor.setValue(option.color)
                        }
                    }
                    Text("Select opponent")
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.black)
                        .padding(.vertical)
                    PlayerSelect(
                        players: newGameViewModel.opponents,
                        selectedPlayer: selectedOpponent.currentValue
                    ) { player in
                        newGameViewModel.selectedOpponent.setValue(player)
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
            
            Button(action: {
                if let players = newGameViewModel.getSelectedPlayers() {
                    environment.startNewGame(whitePlayer: players.first!, blackPlayer: players.second!)
                    viewRouter.navigateUp()
                    viewRouter.navigate(screen: .play)
                }
            }, label: {
                Text("Start Game")
            })
            .padding(.bottom)
        }
    }
}

struct NewGameView_Previews: PreviewProvider {
    static var previews: some View {
        NewGameView()
    }
}
