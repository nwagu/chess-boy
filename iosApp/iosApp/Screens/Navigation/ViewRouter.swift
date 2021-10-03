//
//  ViewRouter.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

class ViewRouter: ObservableObject {
    
    @Published var currentScreen: Screen
    @Published var playScreenUp: Bool
    
    var backStack: Stack<Screen> = Stack()
    
    init() {
        currentScreen = .home
        playScreenUp = false
        backStack.push(currentScreen)
    }
    
    func navigateUp() {
        currentScreen = backStack.pop() ?? currentScreen
    }
    
    func navigate(screen: Screen) {
        backStack.push(currentScreen)
        currentScreen = screen
    }
    
    func showPlayScreen() {
        playScreenUp = true
    }
    
    func hidePlayScreen() {
        playScreenUp = false
    }
}
