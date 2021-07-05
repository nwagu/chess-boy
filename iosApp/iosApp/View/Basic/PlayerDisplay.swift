//
//  PlayerDisplay.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 05/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct PlayerDisplay: View {
    
    var body: some View {
        HStack {
            Image("img_avatar_default")
                .resizable()
                .frame(width: 25, height: 25)
                .aspectRatio(contentMode: .fit)
            Text("Check")
        }
    }
}

struct PlayerDisplay_Previews: PreviewProvider {
    static var previews: some View {
        PlayerDisplay()
    }
}
