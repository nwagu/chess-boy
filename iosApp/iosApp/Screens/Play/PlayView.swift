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
    @StateObject var viewRouter: ViewRouter
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(viewRouter: viewRouter, title: "Active game")
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    
                    PlayerDisplay()
                        .padding(16)
                    
                    ChessBoardView(board: Board(numberOfRows: 8, numberOfColumns: 8).apply(block: {board in
                        board.loadStandardStartingPosition()
                    }))
                    
                    PlayerDisplay()
                        .padding(16)
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
    }
}

struct PlayView_Previews: PreviewProvider {
    static var previews: some View {
        PlayView(viewRouter: ViewRouter())
    }
}
