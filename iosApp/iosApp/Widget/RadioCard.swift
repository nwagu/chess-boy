//
//  RadioCard.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct RadioCard: View {
    let text: String
    let isSelected: Bool
    let onClickAction: () -> Void

    var body: some View {
        ZStack(alignment: Alignment(horizontal: .leading, vertical: .center)) {
            RoundedRectangle(cornerRadius: 8)
                .strokeBorder((isSelected) ? Color.red : Color.gray, lineWidth: 1)

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
