//
//  GeneralUtils.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 11/10/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import Foundation
import SwiftUI

func isDarkModeEnabled() -> Bool {
    if #available(iOS 13.0, *) {
        if UITraitCollection.current.userInterfaceStyle == .dark {
            return true
        }
        else {
            return false
        }
    }
    return false
}
