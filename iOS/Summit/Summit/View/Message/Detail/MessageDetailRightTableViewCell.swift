//
//  MessageDetailRightTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageDetailRightTableViewCell: UITableViewCell {
    
    struct Const {
        static let bottomMargin = CGFloat(26)
    }
    
    @IBOutlet private weak var baloonView: UIView!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var coverView: UIView!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
    }
    
    func configure(data: MessageData, isTemporary: Bool) {
        
        self.messageLabel.text = data.message
        
        let today = Date()
        if data.datetime.isSameDay(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "HH:mm").string(from: data.datetime)
        } else if data.datetime.isSameYear(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "M月d日 HH:mm").string(from: data.datetime)
        } else {
            self.dateLabel.text = DateFormatter(dateFormat: "yyyy年M月d日 HH:mm").string(from: data.datetime)
        }
        
        self.coverView.isHidden = !isTemporary
    }
    
    func height(data: MessageData) -> CGFloat {
        
        self.configure(data: data, isTemporary: false)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.baloonView.frame.origin.y + self.baloonView.frame.size.height + Const.bottomMargin
    }
}
