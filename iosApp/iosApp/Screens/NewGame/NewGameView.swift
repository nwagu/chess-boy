//
//  NewGameView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct NewGameView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(title: "Start a new game against computer")
            Text("New Game")
            Spacer()
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .padding()
    }
}
