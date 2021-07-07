//
//  MainView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct MainView: View {
    @StateObject var viewRouter: ViewRouter
    
    var body: some View {
        switch viewRouter.currentScreen {
        case .home:
            HomeView(viewRouter: viewRouter)
        case .play:
            PlayView(viewRouter: viewRouter)
        case .history:
            HistoryView(viewRouter: viewRouter)
        case .newGame:
            NewGameView(viewRouter: viewRouter)
        case .newBluetoothGame:
            NewBluetoothGameView(viewRouter: viewRouter)
        case .settings:
            SettingsView(viewRouter: viewRouter)
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView(viewRouter: ViewRouter())
    }
}
