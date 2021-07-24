//
//  ScopeFunc.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 08/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI

protocol ScopeFunc {}

extension ScopeFunc {
    @inline(__always) func apply(block: (Self) -> ()) -> Self {
        block(self)
        return self
    }
    @inline(__always) func letIt<R>(block: (Self) -> R) -> R {
        return block(self)
    }
}

extension NSObject: ScopeFunc {}
