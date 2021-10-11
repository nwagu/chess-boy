//
//  ViewAction.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import Foundation

struct ViewAction: Identifiable {
    var id = UUID()
    var displayName: String
    var action: () -> Void
}
