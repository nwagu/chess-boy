//
//  ViewRouter.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

class ViewRouter: ObservableObject {
    
    @Published var currentScreen: Screen = .home
    
//    let backStack = Stack
    
    func navigateUp() {
        currentScreen = .home
    }
    
    func navigate(screen: Screen) {
        currentScreen = screen
    }
    
}
