//
//  PlayView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import chess

struct PlayView: View {
    // var game: Game
    
    
    var body: some View {
        VStack {
            Text("Hello, World!")
            
            ChessBoardView(board: Board(numberOfRows: 8, numberOfColumns: 8).apply(block: {board in
                board.loadStandardStartingPosition()
            }))
            
            Text("Hello, World!")
        }
    }
}

struct PlayView_Previews: PreviewProvider {
    static var previews: some View {
        PlayView()
    }
}
