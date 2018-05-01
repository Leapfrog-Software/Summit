//
//  PickerTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class PickerTableViewCell: UITableViewCell {
    
    @IBOutlet private weak var selectedImageView: UIImageView!
    @IBOutlet private weak var dataLabel: UILabel!
    
    func configure(title: String, isSelected: Bool) {
        
        self.selectedImageView.isHidden = !isSelected
        self.dataLabel.text = title
    }
}
