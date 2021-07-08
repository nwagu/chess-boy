//
//  MainView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    
    var body: some View {
        switch viewRouter.currentScreen {
        case .home:
            HomeView()
        case .play:
            PlayView()
                .transition(.move(edge: .bottom))
        case .history:
            HistoryView()
                .transition(.scale)
        case .newGame:
            NewGameView()
                .transition(.scale)
        case .newBluetoothGame:
            NewBluetoothGameView()
                .transition(.scale)
        case .settings:
            SettingsView()
                .transition(.scale)
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView().environmentObject(ViewRouter())
    }
}
