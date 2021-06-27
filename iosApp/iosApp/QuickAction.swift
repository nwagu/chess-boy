//
//  QuickAction.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

struct QuickAction: Equatable {
    static func == (lhs: QuickAction, rhs: QuickAction) -> Bool {
        lhs.displayName == rhs.displayName
    }
    
    var displayName: String
    var action: () -> Void
}
