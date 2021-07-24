//
//  ChessBoy.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 22/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels

@main
struct ChessBoy: App {
    
    @StateObject var viewRouter = ViewRouter()
    @StateObject var environment = ChessBoyEnvironment()
    
    var body: some Scene {
        WindowGroup {
            MainView()
                .environmentObject(viewRouter)
                .environmentObject(environment)
                .onAppear { environment.initialize() }
                .onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification), perform: { _ in
                    environment.saveCurrentGame()
                 })
        }
    }
}
