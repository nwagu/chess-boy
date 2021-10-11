//
//  IdentifiableView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 08/10/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

struct IdentifiableView: Identifiable {
    var id = UUID()
    var view: AnyView
}
