//
//  CardDetailUserInfoTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardDetailUserInfoTableViewCell: UITableViewCell {

    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var companyLabel: UILabel!
    @IBOutlet private weak var positionLabel: UILabel!
    @IBOutlet private weak var ageLabel: UILabel!
    @IBOutlet private weak var genderLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.userImageView)
    }
    
    func configure(userData: UserData) {
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + userData.userId, imageView: self.userImageView)
        self.nameLabel.text = userData.nameLast + " " + userData.nameFirst
        self.companyLabel.text = userData.company
        self.positionLabel.text = userData.position
        self.ageLabel.text = userData.age.toText()
        self.genderLabel.text = userData.gender.toText()
        self.messageLabel.set(text: userData.message, lineHeight: 16)
    }
}
