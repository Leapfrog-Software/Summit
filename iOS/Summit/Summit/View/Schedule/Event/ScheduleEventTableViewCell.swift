//
//  ScheduleEventTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleEventTableViewCell: UITableViewCell {

    @IBOutlet private weak var shadowView: UIView!
    @IBOutlet private weak var nameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.shadowView.layer.masksToBounds = false
        self.shadowView.layer.shadowOffset = CGSize(width: 0, height: 10)
        self.shadowView.layer.shadowOpacity = 0.4
        self.shadowView.layer.shadowColor = UIColor(white: 0.8, alpha: 1.0).cgColor
        self.shadowView.layer.shadowRadius = 4
    }
    
    func configure(scheduleData: ScheduleData) {
        
        self.nameLabel.text = scheduleData.title
    }
}
