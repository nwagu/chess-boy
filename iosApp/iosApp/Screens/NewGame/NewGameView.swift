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
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                TopBar(title: "Start a new game against computer")
                Text("Choose your side").padding(.vertical)
                
                let options = [
                    SideChoice(
                        displayName: "Random",
                        isSelected: true
                    ),
                    SideChoice(
                        displayName: "White",
                        isSelected: false
                    ),
                    SideChoice(
                        displayName: "Black",
                        isSelected: false
                    )
                ]
                
                WrappingHStack(models: options) { option in
                    
                    Button(action: { option.isSelected = !option.isSelected }) {
                        RadioCard(text: option.displayName, isSelected: option.isSelected)
                    }
                    
                }
                Spacer()
            }
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

struct NewGameView_Previews: PreviewProvider {
    static var previews: some View {
        NewGameView()
    }
}
