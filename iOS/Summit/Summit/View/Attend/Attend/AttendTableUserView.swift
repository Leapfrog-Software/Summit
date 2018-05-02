//
//  AttendTableUserView.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendTableUserView: UIView {

    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var faceImageViewWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var profileLabel: UILabel!

    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        let imageWidth = self.frame.size.width * 0.6
        self.faceImageViewWidthConstraint.constant = imageWidth
        self.faceImageView.layer.cornerRadius = imageWidth / 2
    }
    
    func set(userId: String) {
        
        self.faceImageView.image = nil
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + userId, imageView: self.faceImageView)
        
        if let userData = UserRequester.shared.query(userId: userId) {
            self.nameLabel.text = userData.nameLast + userData.nameFirst

            var profile = ""
            let profileList: [String] = [userData.company, userData.position, userData.age.toText(), userData.gender.toText()]
            let existProfiles = profileList.filter { !$0.isEmpty }
            if existProfiles.count >= 1 {
                profile += existProfiles[0]
            }
            if existProfiles.count >= 2 {
                profile += " "
                profile += existProfiles[1]
            }
            self.profileLabel.text = profile
        }
    }

}
