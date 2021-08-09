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
        switch viewRouter.currentScreen {
        case .home:
            HomeView()
        case .play:
            PlayView(playViewModel: environment.playViewModel)
                .transition(.move(edge: .trailing))
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
            GameAnalysisView()
                .transition(.move(edge: .trailing))
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView().environmentObject(ViewRouter())
    }
}
