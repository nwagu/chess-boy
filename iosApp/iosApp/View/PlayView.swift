//
//  PlayView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct PlayView: View {
    // var game: Game
    
    
    var body: some View {
        VStack {
            
            PlayerDisplay()
            
            ChessBoardView(board: Board(numberOfRows: 8, numberOfColumns: 8).apply(block: {board in
                board.loadStandardStartingPosition()
            }))
            
            PlayerDisplay()
        }
    }
}

struct PlayView_Previews: PreviewProvider {
    static var previews: some View {
        PlayView()
    }
}
