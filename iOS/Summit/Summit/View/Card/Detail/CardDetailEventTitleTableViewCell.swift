//
//  CardDetailEventTitleTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardDetailEventTitleTableViewCell: UITableViewCell {

    @IBOutlet private weak var titleTextLabel: UILabel!
    
    func configure(title: String, count: Int) {
        
        self.titleTextLabel.text = title + " (\(count))"
    }
}
