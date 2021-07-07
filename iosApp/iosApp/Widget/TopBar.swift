//
//  TopBar.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct TopBar: View {
    @StateObject var viewRouter: ViewRouter
    var title: String
    
    var body: some View {
        HStack {
            Button(action: {
                viewRouter.navigateUp()
            }) {
                HStack {
                    Image(systemName: "chevron.left")
                    Text(title)
                }
            }
        }
    }
}

struct TopBar_Previews: PreviewProvider {
    static var previews: some View {
        TopBar(viewRouter: ViewRouter(), title: "Back")
    }
}
