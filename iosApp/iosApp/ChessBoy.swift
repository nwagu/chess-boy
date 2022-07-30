//
//  ChessBoy.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 22/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels
import Firebase

@main
struct ChessBoy: App {
    
    @StateObject var environment = ChessBoyEnvironment()
    
    init() {        
        FirebaseApp.configure()
    }
    
    var body: some Scene {
        WindowGroup {
            MainView()
                .environmentObject(environment)
                .onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification), perform: { _ in
                    environment.saveCurrentGame()
                 })
        }
    }
}
