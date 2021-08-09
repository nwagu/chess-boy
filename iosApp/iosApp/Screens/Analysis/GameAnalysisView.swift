//
//  GameAnalysisView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/08/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct GameAnalysisView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            TopBar(title: "Game review")
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

struct GameAnalysisView_Previews: PreviewProvider {
    static var previews: some View {
        GameAnalysisView()
    }
}
