//
//  PlayView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

struct PlayView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    
    private let playViewModel: PlayViewModel
    @ObservedObject private var gameChanged: Collector<Int32>
    @ObservedObject private var pendingPromotion: Collector<Promotion?>
    @State private var showPromotionDialog = false
    
    init(playViewModel: PlayViewModel) {
        self.playViewModel = playViewModel
        gameChanged = playViewModel.gameUpdated.collectAsObservable(initialValue: playViewModel.gameUpdated.value as! Int32)
        pendingPromotion = playViewModel.pendingPromotion.collectAsObservable(initialValue: playViewModel.pendingPromotion.value as? Promotion)
    }
    
    var body: some View {
        GeometryReader { geometry in
            VStack(alignment: .leading, spacing: 0) {
                VStack(alignment: .center, spacing: 0) {
                    HStack {
                        Spacer()
                        indicator
                        Spacer()
                    }
                    HStack {
                        Text(playViewModel.game.title)
                        Spacer()
                    }
                    .frame(height: 60, alignment: .center)
                }
                .onTapGesture {
                    viewRouter.showPlayScreen()
                }
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        PlayerDisplay(playViewModel: playViewModel, color: playViewModel.game.colorOnUserSideOfBoard.opposite()).padding(16)
                        ChessBoardView(playViewModel: playViewModel)
                        PlayerDisplay(playViewModel: playViewModel, color: playViewModel.game.colorOnUserSideOfBoard).padding(16)
                        Button(action: { withAnimation { playViewModel.undo() } }, label: {Text("Undo") }).padding()
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
    
    private var indicator: some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(Color.orange)
            .frame(width: 60, height: 4)
    }
}

struct PlayView_Previews: PreviewProvider {
    static var previews: some View {
        PlayView(playViewModel: PlayViewModel()).environmentObject(ViewRouter())
    }
}
