//
//  RadioCard.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import sharedmodels

struct RadioCard: View {
    let text: String
    let isSelected: Bool
    let onClickAction: () -> Void
    
    let cornerRadius: CGFloat = 8

    var body: some View {
        ZStack(alignment: Alignment(horizontal: .leading, vertical: .center)) {
            RoundedRectangle(cornerRadius: cornerRadius)
                .strokeBorder((isSelected) ? Color(ColorsKt.getPrimaryColor()) : Color.gray, lineWidth: 1)
                .background(RoundedRectangle(cornerRadius: cornerRadius).fill((isSelected) ? Color(ColorsKt.getPrimaryColorLight()) : Color.white))

            VStack {
                Text(text)
                    .font(.body)
                    .foregroundColor(.black)
            }
            .padding(8)
            .multilineTextAlignment(.leading)
        }
        .frame(width: 100, height: 100, alignment: .center)
        .aspectRatio(contentMode: /*@START_MENU_TOKEN@*/.fill/*@END_MENU_TOKEN@*/)
        .contentShape(Rectangle())
        .onTapGesture {
            onClickAction()
        }
    }
}

struct RadioCard_Previews: PreviewProvider {
    static var previews: some View {
        RadioCard(text: "Hello", isSelected: false) {}
    }
}
