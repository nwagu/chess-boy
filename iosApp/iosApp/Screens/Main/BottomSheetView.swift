//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 03/10/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

fileprivate enum Constants {
    static let radius: CGFloat = 16
    static let indicatorHeight: CGFloat = 6
    static let indicatorWidth: CGFloat = 60
    static let snapRatio: CGFloat = 0.25
    static let minHeightRatio: CGFloat = 0.5
}

struct BottomSheetView<Content: View>: View {
    @Binding var isOpen: Bool

    let maxHeight: CGFloat
    let peekHeight: CGFloat
    let content: Content

    @GestureState private var dragHeight: CGFloat = 0

    private var offset: CGFloat {
        isOpen ? 0 : maxHeight - peekHeight
    }

    private var indicator: some View {
        RoundedRectangle(cornerRadius: Constants.radius)
            .fill(Color.secondary)
            .frame(
                width: Constants.indicatorWidth,
                height: Constants.indicatorHeight
        ).onTapGesture {
            self.isOpen.toggle()
        }
    }

    init(isOpen: Binding<Bool>,
         maxHeight: CGFloat,
         peekHeight: CGFloat,
         @ViewBuilder content: () -> Content) {
        self.peekHeight = peekHeight
        self.maxHeight = maxHeight
        self.content = content()
        self._isOpen = isOpen
    }

    var body: some View {
        GeometryReader { geometry in
            self.content
                .frame(width: geometry.size.width, height: self.maxHeight, alignment: .top)
                .background(Color(.systemBackground))
                .cornerRadius(Constants.radius)
                .frame(height: geometry.size.height, alignment: .bottom)
                .offset(y: max(self.offset + self.dragHeight, 0))
                .animation(.interactiveSpring())
                .gesture(
                    DragGesture()
                        .updating(self.$dragHeight) { value, dragHeight, _ in
                            dragHeight = value.translation.height
                        }
                        .onEnded { value in
                            let snapDistance = self.maxHeight * Constants.snapRatio
                            guard abs(value.translation.height) > snapDistance else {
                                return
                            }
                            self.isOpen = value.translation.height < 0
                        }
                )
        }
    }
}

struct BottomSheetView_Previews: PreviewProvider {
    static var previews: some View {
        BottomSheetView(isOpen: .constant(false), maxHeight: 600, peekHeight: 64) {
            Rectangle().fill(Color.red)
        }.edgesIgnoringSafeArea(.all)
    }
}
