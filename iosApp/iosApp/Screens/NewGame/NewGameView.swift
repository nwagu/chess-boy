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
                        RadioCard(text: option.displayName, isSelected: false) {
                            // newGameViewModel.selectedColor.value = option.color
                        }
                    }
                    Text("Select opponent")
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.black)
                        .padding(.vertical)
                    PlayerSelect(players: [JWTC(), Stockfish(), RandomMoveGenerator()]) { _ in }
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
            
            Button(action: /*@START_MENU_TOKEN@*/{}/*@END_MENU_TOKEN@*/, label: {
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
