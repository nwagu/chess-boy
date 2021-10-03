//
//  MainView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var body: some View {
            GeometryReader { geometry in
                VStack {
                    switch viewRouter.currentScreen {
                        case .home:
                            HomeView()
                        case .newGame:
                            NewGameView()
                                .transition(.move(edge: .trailing))
                        case .newBluetoothGame:
                            NewBluetoothGameView()
                                .transition(.move(edge: .trailing))
                        case .history:
                            HistoryView()
                                .transition(.move(edge: .trailing))
                        case .gameAnalysis:
                            GameAnalysisView(gameAnalysisViewModel: environment.gameAnalysisViewModel)
                                .transition(.move(edge: .trailing))
                    }
                }.padding(.bottom, 64)
                BottomSheetView(
                    isOpen: self.$viewRouter.playScreenUp,
                    maxHeight: geometry.size.height,
                    peekHeight: 64
                ) {
                    PlayView(playViewModel: environment.playViewModel)
                }.edgesIgnoringSafeArea(.top)
            }
        }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView().environmentObject(ViewRouter())
    }
}
