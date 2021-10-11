//
//  UIColorExtension.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 11/10/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import Foundation
import SwiftUI

// https://stackoverflow.com/a/38435309/8196427

extension Color {
    
    func lightenByMode(by percentage: CGFloat = 30.0) -> Color? {
        if isDarkModeEnabled() {
            return darker(by: percentage)
        } else {
            return lighter(by: percentage)
        }
    }

    func lighter(by percentage: CGFloat = 30.0) -> Color? {
        return self.adjust(by: abs(percentage) )
    }

    func darker(by percentage: CGFloat = 30.0) -> Color? {
        return self.adjust(by: -1 * abs(percentage) )
    }

    func adjust(by percentage: CGFloat = 30.0) -> Color? {
        var red: CGFloat = 0, green: CGFloat = 0, blue: CGFloat = 0, alpha: CGFloat = 0
        
        if UIColor(self).getRed(&red, green: &green, blue: &blue, alpha: &alpha) {
            return Color(UIColor(red: min(red + percentage/100, 1.0),
                           green: min(green + percentage/100, 1.0),
                           blue: min(blue + percentage/100, 1.0),
                           alpha: alpha))
        } else {
            return nil
        }
    }
}
