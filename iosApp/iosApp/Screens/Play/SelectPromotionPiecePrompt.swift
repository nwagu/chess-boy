//
//  SelectPromotionPiecePrompt.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 21/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import SwiftUI
import sharedmodels


func selectPromotionPiecePrompt(playViewModel: PlayViewModel) -> ActionSheet {
    
    func onSelect(_ piece: ChessPieceType) {
        if let move = playViewModel.pendingPromotion.value as? Promotion {
            move.promotionType = piece
            playViewModel.makeUserMove(move: move)
        }
        playViewModel.pendingPromotion.setValue(nil)
    }
    
    return ActionSheet(title: Text("Select promotion piece"), buttons: [
        .default(Text("Queen")) { onSelect(.queen) },
        .default(Text("Knight")) { onSelect(.knight) },
        .default(Text("Bishop")) { onSelect(.bishop) },
        .default(Text("Rook")) { onSelect(.rook) },
        .cancel() { playViewModel.pendingPromotion.setValue(nil) }
    ])
}


