//
//  ChessBoy.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 22/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
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
        }
    }
}
