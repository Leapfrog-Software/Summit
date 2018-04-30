//
//  CardKanaTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardKanaTableViewCell: UITableViewCell {

    @IBOutlet private weak var kanaLabel: UILabel!
    
    func configure(kana: String) {
        self.kanaLabel.text = kana
    }
}
