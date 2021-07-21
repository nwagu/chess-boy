//
//  PlayerSelect.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 10/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct PlayerSelect: View {
    let players: [SelectablePlayer]
    let selectedPlayer: SelectablePlayer?
    let onSelect: (SelectablePlayer) -> Void
    
    var body: some View {
        VStack {
            ForEach(0..<players.count) { index in
                PlayerSelectItem(
                    player: players[index],
                    isSelected: (players[index] == selectedPlayer),
                    onSelect: onSelect
                )
            }
        }
    }
}

struct PlayerSelect_Previews: PreviewProvider {
    static var previews: some View {
        PlayerSelect(
            players: [JWTC(), Stockfish()],
            selectedPlayer: nil
        ) { _ in }
    }
}


struct PlayerSelectItem: View {
    let player: SelectablePlayer
    var isSelected: Bool
    let onSelect: (SelectablePlayer) -> Void
    
    let cornerRadius: CGFloat = 4
    
    var body: some View {
        ZStack(alignment: Alignment(horizontal: .leading, vertical: .center)) {
            RoundedRectangle(cornerRadius: cornerRadius)
                .strokeBorder((isSelected) ? Color(ColorsKt.getPrimaryColor()) : Color.gray, lineWidth: 1)
                .background(RoundedRectangle(cornerRadius: cornerRadius).fill((isSelected) ? Color(ColorsKt.getPrimaryColorLight()) : Color.white))

            VStack {
                Text(player.name)
                    .font(.body)
                    .foregroundColor(.black)
            }
            .padding(8)
            .multilineTextAlignment(.leading)
        }
        .frame(maxWidth: .infinity, maxHeight: 60, alignment: .leading)
        .contentShape(Rectangle())
        .onTapGesture {
            onSelect(player)
        }
    }
}
