//
//  MainView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var body: some View {
            GeometryReader { geometry in
                ZStack {
                    NavigationView {
                        HomeView()
                            .navigationBarTitle("")
                            .navigationBarHidden(true)
                    }
                    .navigationViewStyle(StackNavigationViewStyle())
                    .padding(.bottom, 64)
                    
                    BottomSheetView(
                        isOpen: self.$environment.playScreenUp,
                        maxHeight: geometry.size.height,
                        peekHeight: 64
                    ) {
                        PlayView(playViewModel: environment.playViewModel)
                    }
                }
            }
        }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
