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

    @GestureState private var dragGestureState = DragGestureState(dragHeight: 0, backgroundBrightness: 0)

    private var offset: CGFloat {
        isOpen ? 0 : maxHeight - peekHeight
    }
    
    private var backgroundBrightness: Double {
        isOpen ? 1 : 0
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
                .background(Color("PrimaryLight")) //.lightenByMode(by: CGFloat((backgroundBrightness - self.dragGestureState.backgroundBrightness)) * 100))
                .cornerRadius(Constants.radius)
                .frame(height: geometry.size.height, alignment: .bottom)
                .offset(y: max(self.offset + self.dragGestureState.dragHeight, 0))
                .animation(.interactiveSpring())
                .gesture(
                    DragGesture()
                        .updating(self.$dragGestureState) { value, dragGestureState, _ in
                            dragGestureState = DragGestureState(
                                dragHeight: value.translation.height,
                                backgroundBrightness: Double(value.translation.height / self.maxHeight)
                            )
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

struct DragGestureState {
    let dragHeight: CGFloat
    let backgroundBrightness: Double
}

struct BottomSheetView_Previews: PreviewProvider {
    static var previews: some View {
        BottomSheetView(isOpen: .constant(false), maxHeight: 600, peekHeight: 64) {
            Rectangle().fill(Color.red)
        }.edgesIgnoringSafeArea(.all)
    }
}
