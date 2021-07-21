//
//  PlayView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

// to match the typealias in the kotlin chess module
typealias Square = Int32

struct PlayView: View {
    @EnvironmentObject
    var viewRouter: ViewRouter
    
    var playViewModel: PlayViewModel
    @ObservedObject
    var gameChanged: Collector<Int32>
    @ObservedObject
    var pendingPromotion: Collector<Promotion?>
    
    @State
    private var showPromotionDialog = false
    
    init(playViewModel: PlayViewModel) {
        self.playViewModel = playViewModel
        gameChanged = playViewModel.gameUpdated.collectAsObservable(initialValue: playViewModel.gameUpdated.value as! Int32)
        pendingPromotion = playViewModel.pendingPromotion.collectAsObservable(initialValue: playViewModel.pendingPromotion.value as? Promotion)
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(title: playViewModel.game.title)
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    PlayerDisplay(playViewModel: playViewModel, color: playViewModel.game.colorOnUserSideOfBoard.opposite()).padding(16)
                    ChessBoardView(playViewModel: playViewModel)
                    PlayerDisplay(playViewModel: playViewModel, color: playViewModel.game.colorOnUserSideOfBoard).padding(16)
                    Button(action: { playViewModel.undo() }, label: {Text("Undo") }).padding()
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
        .actionSheet(isPresented: $showPromotionDialog) {
            selectPromotionPiecePrompt(playViewModel: playViewModel)
        }
        .onReceive(pendingPromotion.$currentValue) { promotion in
            showPromotionDialog = (promotion != nil)
        }
    }
}

struct PlayView_Previews: PreviewProvider {
    static var previews: some View {
        PlayView(playViewModel: PlayViewModel()).environmentObject(ViewRouter())
    }
}
