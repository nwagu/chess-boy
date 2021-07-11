//
//  SideChoice.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 10/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import sharedmodels

struct SideChoice: Identifiable {
    var id = UUID()
    var displayName: String
    var color: ChessPieceColor?
}
