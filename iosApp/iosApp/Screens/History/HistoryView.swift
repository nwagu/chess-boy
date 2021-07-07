//
//  HistoryView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct HistoryView: View {
    @StateObject var viewRouter: ViewRouter
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(viewRouter: viewRouter, title: "History")
            Text("History")
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
