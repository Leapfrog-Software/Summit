//
//  AttendMemberListTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendMemberListTableViewCell: UITableViewCell {

    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var companyLabel: UILabel!
    @IBOutlet private weak var positionLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(userData: UserData) {
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + userData.userId, imageView: self.faceImageView)
        
        self.nameLabel.text = userData.nameLast + " " + userData.nameFirst
        self.companyLabel.text = userData.company
        self.positionLabel.text = userData.position
        
        self.messageLabel.set(text: userData.message, lineHeight: 16)
    }
}
