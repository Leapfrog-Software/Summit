//
//  CardIndexView.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardIndexView: UIView {

    @IBOutlet private var kanaLabels: [UILabel]!
    
    private var currentIndex = -1

    func set(index: Int) {
        
        if index == self.currentIndex {
            return
        }
        self.kanaLabels.forEach {
            $0.textColor = ($0.tag == index) ? .cardIndexActive : .cardIndexPassive
        }
        self.currentIndex = index
    }
}
