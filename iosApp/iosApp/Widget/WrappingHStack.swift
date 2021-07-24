//
//  WrappingHStack.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 09/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct WrappingHStack<Model, ModelView>: View where Model: Identifiable, ModelView: View {
    var models: [Model]
    var viewGenerator: (Model) -> ModelView
    var horizontalSpacing: CGFloat = 8
    var verticalSpacing: CGFloat = 8

    @State private var totalHeight = CGFloat.zero

    var body: some View {
        GeometryReader { geometry in
            var width = CGFloat.zero
            var height = CGFloat.zero
            
            ZStack(alignment: .topLeading) {
                ForEach(models) { models in
                    viewGenerator(models)
                        .padding(.horizontal, horizontalSpacing)
                        .padding(.vertical, verticalSpacing)
                        .alignmentGuide(.leading, computeValue: { dimension in
                            if (abs(width - dimension.width) > geometry.size.width)
                            {
                                width = 0
                                height -= dimension.height
                            }
                            let result = width
                            if models.id == self.models.last!.id {
                                width = 0 //last item
                            } else {
                                width -= dimension.width
                            }
                            return result
                        })
                        .alignmentGuide(.top, computeValue: {dimension in
                            let result = height
                            if models.id == self.models.last!.id {
                                height = 0 // last item
                            }
                            return result
                        })
                }
            }.background(viewHeightReader($totalHeight))
        }.frame(height: totalHeight)
    }

    private func viewHeightReader(_ binding: Binding<CGFloat>) -> some View {
        return GeometryReader { geometry -> Color in
            let rect = geometry.frame(in: .local)
            DispatchQueue.main.async {
                binding.wrappedValue = rect.size.height
            }
            return .clear
        }
    }
}
