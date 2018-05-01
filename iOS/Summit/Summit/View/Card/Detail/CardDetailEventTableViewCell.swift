//
//  CardDetailEventTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardDetailEventTableViewCell: UITableViewCell {
    
    @IBOutlet private weak var shadowView: UIView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var providerLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!

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
        self.dateLabel.text = DateFormatter(dateFormat: "yyyy/M/d").string(from: scheduleData.date)
        self.providerLabel.text = scheduleData.provider
        self.descriptionLabel.set(text: scheduleData.description, lineHeight: 16)
    }
}
