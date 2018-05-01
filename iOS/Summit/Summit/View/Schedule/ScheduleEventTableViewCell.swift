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
    @IBOutlet private weak var providerLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    @IBOutlet private weak var timeLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.shadowView.layer.masksToBounds = false
        self.shadowView.layer.shadowOffset = CGSize(width: 0, height: 8)
        self.shadowView.layer.shadowOpacity = 0.4
        self.shadowView.layer.shadowColor = UIColor(white: 0.8, alpha: 1.0).cgColor
        self.shadowView.layer.shadowRadius = 4
    }
    
    func configure(scheduleData: ScheduleData) {
        self.nameLabel.text = scheduleData.title
        self.providerLabel.text = scheduleData.provider
        self.descriptionLabel.text = scheduleData.description
        self.timeLabel.text = DateFormatter(dateFormat: "HH:mm").string(from: scheduleData.date)
    }
}
