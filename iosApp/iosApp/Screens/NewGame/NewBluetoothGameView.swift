//
//  NewBluetoothGameView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 26/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct NewBluetoothGameView: View {
    
    var options: [SideChoice] {
        [
            SideChoice(displayName: "White", color: .white),
            SideChoice(displayName: "Black", color: .black)
        ]
    }
    
    var body: some View {
        VStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Text("Choose your side")
                        .font(.subheadline)
                        .fontWeight(.medium)
                        .foregroundColor(.black)
                        .padding(.vertical)
                    
                    WrappingHStack(models: options) { option in
                        RadioCard(text: option.displayName, isSelected: false) {
                            // newGameViewModel.selectedColor.value = option.color
                        }
                    }
                    
                    let whiteSelected = true
                    
                    let connectMessage = (whiteSelected) ? "As white player, you are responsible for initiating a connection to the device playing black. Please click on SCAN to start discovering devices."
                        : "As black player, you will accept connection from the device playing white. Please click on RECEIVE to ensure discoverability and start listening."
                    Text(connectMessage)
                        .italic()
                        .font(.body)
                        .fontWeight(.regular)
                        .foregroundColor(.black)
                        .padding(.vertical)
                    
                    HStack {
                        Spacer()
                        Button(action: {}, label: {
                            Text((whiteSelected) ? "Scan" : "Receive")
                        })
                        .padding()
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
            
            Button(action: {}, label: {
                Text("Connect")
            })
            .padding(.bottom)
        }
        .navigationBarTitle("Start a new bluetooth game", displayMode: .inline)
        
    }
}


struct NewBluetoothGameView_Previews: PreviewProvider {
    static var previews: some View {
        NewBluetoothGameView()
    }
}
