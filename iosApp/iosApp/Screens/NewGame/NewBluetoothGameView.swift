//
//  NewBluetoothGameView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct NewBluetoothGameView: View {
    @StateObject var viewRouter: ViewRouter
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(viewRouter: viewRouter, title: "Start a new bluetooth game")
            Text("New Bluetooth game")
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
