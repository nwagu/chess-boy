//
//  FlowRow.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct FlowRow: View {
    @State var views = [QuickAction]()

    var body: some View {
        GeometryReader { geometry in
            self.generateContent(in: geometry)
        }
    }

    private func generateContent(in g: GeometryProxy) -> some View {
        var width = CGFloat.zero
        var height = CGFloat.zero

        return ZStack(alignment: .topLeading) {
            ForEach(views, id: \.self.displayName) { platform in
                
                Button(action: platform.action) {
                    QuickActionView(text: platform.displayName)
                        .frame(width: 120, height: 120, alignment: .center)
                        .aspectRatio(contentMode: /*@START_MENU_TOKEN@*/.fill/*@END_MENU_TOKEN@*/)
                }
                    .padding([.horizontal, .vertical], 8)
                    .alignmentGuide(.leading, computeValue: { d in
                        if (abs(width - d.width) > g.size.width)
                        {
                            width = 0
                            height -= d.height
                        }
                        let result = width
                        if platform == self.views.last! {
                            width = 0 //last item
                        } else {
                            width -= d.width
                        }
                        return result
                    })
                    .alignmentGuide(.top, computeValue: { d in
                        let result = height
                        if platform == self.views.last! {
                            height = 0 // last item
                        }
                        return result
                    })
            }
        }
    }

}


struct FlowRow_Previews: PreviewProvider {
    static var previews: some View {
        FlowRow(views: [
            QuickAction(displayName: "A", action: {  }),
            QuickAction(displayName: "B", action: {  }),
            QuickAction(displayName: "C", action: {  }),
            QuickAction(displayName: "D", action: {  })
        ])
    }
}
